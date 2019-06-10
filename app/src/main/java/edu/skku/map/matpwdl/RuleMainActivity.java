package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
//https://yeolco.tistory.com/85
public class RuleMainActivity extends AppCompatActivity {

    private DatabaseReference rPostReference;
    FloatingActionButton fab_addRule;
    ListView listView;
    ArrayList<ListViewRuleItem> rules = new ArrayList<>();
    RuleListAdapter ruleListAdapter;
    ListViewRuleItem testRule;
    String biggest_rule_id = "0";
    Calendar calender;
    MyInformation myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_main);

        //Initialize MyInformation
        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");

        //규칙들을 표시할 ListView
        listView = findViewById(R.id.ListView_rules);

        rPostReference = FirebaseDatabase.getInstance().getReference();

        //Floating Action Button: 새로운 글 작성용
        fab_addRule = findViewById(R.id.floatingActionButton_addrule);
        fab_addRule.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                String rule_id = Integer.toString((Integer.parseInt(biggest_rule_id) + 1));
                Intent intent = new Intent(RuleMainActivity.this,AddEditRuleActivity.class);
                intent.putExtra("rule_id",rule_id);
                intent.putExtra("content","");
                intent.putExtra("day","");
                intent.putExtra("member","");
                intent.putExtra("time","");
                intent.putExtra("title","");
                intent.putExtra("repeat",0);
                startActivity(intent);
            }
        });

        ruleListAdapter = new RuleListAdapter(this, rules);
        listView.setAdapter(ruleListAdapter);

        calender = Calendar.getInstance(); //요일 확인용

        getFirebaseDatabase();
    }

    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("onDataChange","Data is Updated");
                rules.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    ListViewRuleItem get = postSnapshot.getValue(ListViewRuleItem.class);
                    rules.add(get);
                    if(Integer.parseInt(get.rule_id)>Integer.parseInt(biggest_rule_id)){
                        biggest_rule_id = get.rule_id;
                    }
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

                ruleListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };

        rPostReference.child("ROOM").child("room1").child("rule").addValueEventListener(postListener);
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
