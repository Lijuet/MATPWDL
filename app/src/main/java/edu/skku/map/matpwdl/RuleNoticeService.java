package edu.skku.map.matpwdl;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RuleNoticeService extends Service {
    NotificationManager notificationManager;
    ServiceThread thread;
    Notification notification;
    private DatabaseReference rPostReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ListViewRuleItem> rules = new ArrayList<>();
    boolean gotData = false;
    boolean readingData = false;
    String notifiTitle="";
    String notifiContent="";
    String room_id;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    public RuleNoticeService() {
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("id","name",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        room_id = intent.getStringExtra("room_id");

        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();

        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class myServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(RuleNoticeService.this,HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(RuleNoticeService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                notification = new Notification.Builder(getApplicationContext(),"id")
                        .setContentTitle(notifiTitle)
                        .setContentText(notifiContent)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setTicker("알림")
                        .setContentIntent(pendingIntent)
                        .build();
                notificationManager.notify(0,notification);
            }
            else{
                notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle(notifiTitle)
                        .setContentText(notifiContent)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setTicker("알림")
                        .setContentIntent(pendingIntent)
                        .build();
                notificationManager.notify(0,notification);
            }

            //super.handleMessage(msg);
        }
    }

    public class ServiceThread extends Thread{
        Handler handler;
        boolean isRun = true;

        public ServiceThread(Handler handler){
            this.handler = handler;
        }

        public void stopForever(){
            synchronized (this){
                this.isRun = false;
            }
        }

        public void run(){
            while(isRun){
                getFirebaseDatabase();
                while(!gotData);
                //알림 보내는법 : handler.sendEmptyMessage(0);
                Date date = new Date();
                String time = simpleDateFormat.format(date.getTime());
                readingData = true;
                for(int i=0; i<rules.size(); i++){
                    try {
                        Date ruleDate = simpleDateFormat.parse(rules.get(i).time);
                        String ruleTime = simpleDateFormat.format(ruleDate);
                        //Log.d("time",time+" AND "+ruleTime);
                        Calendar calender = Calendar.getInstance();
                        int nWeek = calender.get(Calendar.DAY_OF_WEEK);
                        if ((ruleTime.equals(time))&&(rules.get(i).repeat!=-1)&&(matchWeek(rules.get(i),nWeek))) {
                            notifiTitle = rules.get(i).title;
                            notifiContent = rules.get(i).content;
                            handler.sendEmptyMessage(0);
                            if(rules.get(i).repeat==0){
                                String header = "[종료]";
                                header+=rules.get(i).title;
                                postFirebaseDatabase(true,header,rules.get(i).content,
                                        rules.get(i).day, rules.get(i).member,-1,rules.get(i).rule_id,rules.get(i).time);
                            }
                        }
                    } catch(ParseException e) {e.printStackTrace();}
                }
                readingData = false;

                try {
                    Thread.sleep(60000);
                }catch (Exception e){}

            }
        }
    }

    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("onDataChange","Data is Updated");
                while(readingData);
                rules.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    ListViewRuleItem get = postSnapshot.getValue(ListViewRuleItem.class);
                    rules.add(get);
                    Log.d("getFirebaseDatabase","key: "+ key);
                    Log.d("getFirebaseDatabase",get.title);
                }
                gotData = true;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };
        rPostReference.child("ROOM").child("room"+room_id).child("rule").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add, String title, String content, String day, String member, int repeat, String rule_id, String time){
        Map<String,Object> childUpdates = new HashMap<>();
        Map<String,Object> postValues = null;
        if(add){
            ListViewRuleItem post = new ListViewRuleItem(content,day,member,repeat,rule_id,time,title);
            postValues = post.toMap();
        }
        childUpdates.put("ROOM/room"+room_id+"/rule/rule"+rule_id , postValues);
        rPostReference.updateChildren(childUpdates);
    }

    //규칙이 오늘 해야 하는것인지 확인
    public boolean matchWeek(ListViewRuleItem rule, int week){
        String weekInfo = rule.getWeek();
        if(week == 1){
            if(weekInfo.contains("일")) return true;
            else return false;
        }
        else if(week == 2){
            if(weekInfo.contains("월")) return true;
            else return false;
        }
        else if(week == 3){
            if(weekInfo.contains("화")) return true;
            else return false;
        }
        else if(week == 4){
            if(weekInfo.contains("수")) return true;
            else return false;
        }
        else if(week == 5){
            if(weekInfo.contains("목")) return true;
            else return false;
        }
        else if(week == 6){
            if(weekInfo.contains("금")) return true;
            else return false;
        }
        else if(week == 7){
            if(weekInfo.contains("토")) return true;
            else return false;
        }
        return false;
    }
}
