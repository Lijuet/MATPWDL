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

        myServiceHandler handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
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
                        Log.d("time",time+" AND "+ruleTime);
                        if (ruleTime.equals(time)) {
                            Log.d("time","SAMEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                            notifiTitle = rules.get(i).title;
                            notifiContent = rules.get(i).content;
                            handler.sendEmptyMessage(0);
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
        rPostReference.child("ROOM").child("room1").child("rule").addValueEventListener(postListener);
    }

}
