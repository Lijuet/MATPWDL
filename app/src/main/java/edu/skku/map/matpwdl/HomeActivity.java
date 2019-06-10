package edu.skku.map.matpwdl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Intent serviceIntent;
    private DatabaseReference rPostReference  = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference kPostReference = FirebaseDatabase.getInstance().getReference();
    ArrayList<ListViewRuleItem> rules = new ArrayList<>();
    ArrayList<Knock> allKnocks = new ArrayList<>();
    ArrayList<Knock> newKnocks = new ArrayList<>();
    Calendar calender = Calendar.getInstance();

    LinearLayout constraint_notice;
    LinearLayout constraint_rule;
    LinearLayout constraint_knock;
    Button toMemberList;
    Button logout;
    TextView textView_notice;
    TextView textView_notice2;
    TextView textView_notice3;
    TextView textView_rule1;
    TextView textView_rule2;
    TextView textView_rule3;
    TextView textView_knock1;
    TextView textView_knock2;
    TextView textView_knock3;

    String notice1 = " · ";
    String notice2 = " · ";
    String notice3 = " · ";
    String knock1 = " · ";
    String knock2 = " · ";
    String knock3 = " · ";
    String room_id;
    MyInformation myInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.HomeBlue)));
        setTitle( "HOME" );
        constraint_notice = findViewById(R.id.constraint_notice);
        constraint_rule = findViewById(R.id.constraint_rule);
        constraint_knock = findViewById(R.id.constraint_knock);
        toMemberList = findViewById(R.id.button_toMemberList);
        logout = findViewById(R.id.button_logout);
        textView_notice = findViewById(R.id.textView_notice);
        textView_notice2 = findViewById(R.id.textView_notice2);
        textView_notice3 = findViewById(R.id.textView_notice3);
        textView_rule1=findViewById(R.id.textView_rule1);
        textView_rule2=findViewById(R.id.textView_rule2);
        textView_rule3=findViewById(R.id.textView_rule3);
        textView_knock1=findViewById(R.id.textView_knock1);
        textView_knock2=findViewById(R.id.textView_knock2);
        textView_knock3=findViewById(R.id.textView_knock3);

        //읽음 후 이동으로 왔다면 popupActivity 종료
        KnockPopupActivity KPA = (KnockPopupActivity)KnockPopupActivity._KnockPopupActivity;
        if(KPA != null) KPA.finish();

        //initialize basic information
        final Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");
        Log.d("LoginTest","Home Activity :" + myInfo.getName());
        room_id = myInfo.getRoomID();


        //공지
        constraint_notice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Uri url = Uri.parse("https://dorm.skku.edu/skku/notice/notice_all.jsp");
                Intent intent = new Intent(Intent.ACTION_VIEW,url);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
                return false;
            }
        });

        //규칙
        constraint_rule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Map<Integer,String> roommatessID = myInfo.getRoommatessID();
                String[] arr = new String[roommatessID.size()];
                Iterator<Integer> keys = roommatessID.keySet().iterator();
                int i= 0;
                while(keys.hasNext()){
                    int key = keys.next();
                    arr[i] = roommatessID.get(key);
                    i++;
                }
                Intent intent = new Intent(HomeActivity.this, RuleMainActivity.class);
                intent.putExtra("arr",arr);
                intent.putExtra("room_id",myInfo.getRoomID());
                startActivity(intent);
                return false;
            }
        });

        //똑똑똑
        constraint_knock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(HomeActivity.this, KnockMainActivity.class);
                intent.putExtra("myInfo",myInfo);
                startActivity(intent);
                return false;
            }
        });

        //멤버 관리 버튼
        toMemberList.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, RoommateListActivity.class);
                intent.putExtra("myInfo",myInfo);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                LoginActivity LA = (LoginActivity)LoginActivity._LoginActivity;
                if(LA != null) LA.finish();
                Log.d("LogoutTest","click");

                SharedPreferences prefs = getSharedPreferences("loginFile",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();

                editor.commit();
                Intent intentLogout = new Intent(HomeActivity.this, LoginActivity.class);
                intentLogout.putExtra("logout", true);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentLogout);
            }
        });

        //봉룡학사 사이트에서 공지 크롤링
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        //3개 보여주기
        getFirebaseDatabaseRule();
        getFirebaseDatabaseKnock();

        //알림
        Intent sIntent = new Intent(HomeActivity.this,RuleNoticeService.class);
        sIntent.putExtra("room_id",myInfo.getRoomID());
        startService(sIntent);
    }

    //공지 크롤링 AsyncTask
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... params){
            try {
                Document doc = Jsoup.connect("https://dorm.skku.edu/skku/notice/notice_all.jsp").get();

                Elements contents = doc.getElementsByClass("list_wrap").first()
                        .getElementsByTag("tbody").first().getElementsByTag("tr");

                int check = 0;

                for(Element e: contents){
                    if(!e.hasAttr("style")){
                        Element element = e.getElementsByTag("a").first();
                        if(check==0)
                            notice1 += element.text();
                        else if (check==1)
                            notice2 += element.text();
                        else if (check==2)
                            notice3 += element.text();
                        else break;
                        check++;
                    }
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            textView_notice.setText(notice1);
            textView_notice2.setText(notice2);
            textView_notice3.setText(notice3);
        }
    }


    public void getFirebaseDatabaseRule(){
        final ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("onDataChange","Data is Updated");
                rules.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    ListViewRuleItem get = postSnapshot.getValue(ListViewRuleItem.class);
                    rules.add(get);
                    Log.d("getFirebaseDatabase","key: "+ key);
                    Log.d("getFirebaseDatabase",get.title);
                }
                //오늘 요일이 포함된 규칙을 앞으로
                int nWeek = calender.get(Calendar.DAY_OF_WEEK);
                for(int i = rules.size() - 1; i>0; i--){
                    if(rules.get(i).repeat!=-1){
                        if(matchWeek(rules.get(i),nWeek)){
                            Collections.swap(rules,i,0);
                        }
                    }
                }
                String ruleHead = " · ";
                for(int i=0; i<rules.size(); i++){
                    if(i==0){
                        textView_rule1.setText(ruleHead+rules.get(i).title);
                    }
                    else if(i==1){
                        textView_rule2.setText(ruleHead+rules.get(i).title);
                    }
                    else if(i==2){
                        textView_rule3.setText(ruleHead+rules.get(i).title);
                    }
                    else break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };
        rPostReference.child("ROOM").child("room"+room_id).child("rule").addValueEventListener(postListener);
    }

    private void getFirebaseDatabaseKnock() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                allKnocks.clear();
                Log.d("onDataChange", "before Sanpshot");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Knock getKnock = postSnapshot.getValue(Knock.class);

                    String[] info = {getKnock.getContent(), getKnock.getSender(), getKnock.getReceiver().trim(), getKnock.getDate(), getKnock.getKnockID()};
                    Knock knock = new Knock(info[0], info[1], info[2], info[3], info[4]);
                    knock.setRead(getKnock.getRead());
                    allKnocks.add(knock);
                    if(info[2].equals(myInfo.getMemberid()) && getKnock.getRead() == 0){
                        newKnocks.add(knock);
                    }
                }
                for(int i=allKnocks.size()-1; i>0; i--){
                    for(int j=0; j<i; j++){
                        if(isKnockEarlier(allKnocks.get(j).getDate(),allKnocks.get(j+1).getDate())){
                            Collections.swap(allKnocks,j,j+1);
                        }

                    }
                }
                for(int i=0; i<allKnocks.size(); i++){
                    if(i==0){
                        knock1+=allKnocks.get(i).getContent();
                    }
                    else if(i==1){
                        knock2+=allKnocks.get(i).getContent();
                    }
                    else if(i==2){
                        knock3+=allKnocks.get(i).getContent();
                    }
                    else break;
                }

                textView_knock1.setText(knock1);
                textView_knock2.setText(knock2);
                textView_knock3.setText(knock3);

                if(newKnocks.size() != 0){
                    for(Knock temp : newKnocks){
                        PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                        boolean bScreenOn = manager.isScreenOn();

                        Intent intentPopup = new Intent(getApplicationContext(), KnockPopupActivity.class);
                        intentPopup.putExtra("NEWKNOCK", temp);
                        intentPopup.putExtra("myInfo",myInfo);
                        if(bScreenOn){
                            Log.d("sendMessage", "Screen ON");
                            intentPopup.putExtra("SCREENON",true);
                            intentPopup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentPopup);
                        }else{
                            Log.d("sendMessage", "Screen OFF");
                            intentPopup.putExtra("SCREENON",false);
                            intentPopup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentPopup);
                        }
                        newKnocks.remove(temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        Log.d("onDataChange", "reference change");
        kPostReference.child("ROOM").child("room"+room_id).child("knock").addValueEventListener(postListener);
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

    //knock 날짜 비교
    public boolean isKnockEarlier(String date1, String date2){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        try {
            Date oDate = sdf.parse(date1);
            Date tDate = sdf.parse(date2);

            long oDateValue = oDate.getTime(), tDateValue = tDate.getTime();

            if(oDateValue > tDateValue) return false;
            else return true;

        } catch (ParseException e) {
            Log.d("sortKnock", e.getMessage());
            return false;
        }
    }

}




