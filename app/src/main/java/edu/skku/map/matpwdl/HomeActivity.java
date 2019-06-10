package edu.skku.map.matpwdl;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Intent serviceIntent;

    ConstraintLayout constraint_notice;
    ConstraintLayout constraint_rule;
    ConstraintLayout constraint_knock;
    Button toMemberList;
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
    MyInformation myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        constraint_notice = findViewById(R.id.constraint_notice);
        constraint_rule = findViewById(R.id.constraint_rule);
        constraint_knock = findViewById(R.id.constraint_knock);
        toMemberList = findViewById(R.id.button_toMemberList);
        textView_notice = findViewById(R.id.textView_notice);
        textView_notice2 = findViewById(R.id.textView_notice2);
        textView_notice3 = findViewById(R.id.textView_notice3);
        textView_rule1=findViewById(R.id.textView_rule1);
        textView_rule2=findViewById(R.id.textView_rule2);
        textView_rule3=findViewById(R.id.textView_rule3);
        textView_knock1=findViewById(R.id.textView_knock1);
        textView_knock2=findViewById(R.id.textView_knock2);
        textView_knock3=findViewById(R.id.textView_knock3);


        //initialize basic information
        InitForTest(); //for TEST TODO delete it after logi section

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
                Intent intent = new Intent(HomeActivity.this, RuleMainActivity.class);
                startActivity(intent);
                intent.putExtra("myInfo",myInfo);
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

        //봉룡학사 사이트에서 공지 크롤링
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        //알림 테스트
        Intent sIntent = new Intent(HomeActivity.this,RuleNoticeService.class);
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

            if(notice1.length()>30){
                notice1 = notice1.substring(0, 30);
                notice1 += "…";
            }
            if(notice2.length()>30){
                notice2 = notice2.substring(0, 30);
                notice2 += "…";
            }
            if(notice3.length()>30){
                notice3 = notice3.substring(0, 30);
                notice3 += "…";
            }

            textView_notice.setText(notice1);
            textView_notice2.setText(notice2);
            textView_notice3.setText(notice3);
        }
    }

    private void InitForTest(){
        Map<Integer,String> temp = new HashMap<>();
        temp.put(1, "김초롱");
        temp.put(2, "박달러");
        temp.put(3, "황모바");
        myInfo = new MyInformation();
        myInfo.setMemberid("1");
        myInfo.setName("김초롱");
        myInfo.setRoomID("1");
        myInfo.setStatus("재실");
        myInfo.setRoommatessID(temp);
    }
}


