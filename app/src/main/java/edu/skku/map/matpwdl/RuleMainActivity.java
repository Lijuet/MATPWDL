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

public class RuleMainActivity extends AppCompatActivity {

    private DatabaseReference rPostReference;
    FloatingActionButton fab_addRule;
    ListView listView;
    ArrayList<ListViewRuleItem> rules = new ArrayList<>();
    RuleListAdapter ruleListAdapter;
    ListViewRuleItem testRule;
    String biggest_rule_id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_main);

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
                /*요일에 맞춰 정렬(미구현)*/
                ruleListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };

        rPostReference.child("ROOM").child("room1").child("rule").addValueEventListener(postListener);
    }
}
