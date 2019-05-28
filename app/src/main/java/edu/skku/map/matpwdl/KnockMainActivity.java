package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//myKnocklist와 mainKnockList를 설정할 adpater입니다.
//item은 Knock.activity입니다.

public class KnockMainActivity extends AppCompatActivity {
    private DatabaseReference kPostReference;
    ListView lvMainKnockList, lvMyKnockList;
    FloatingActionButton btnMakeKnock;

    KnockAdapter allKnockAdapter;
    KnockAdapter myKnockAdapter;
    ArrayList<Knock> allKnocks;
    ArrayList<Knock> myKnocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knock_main);


        //find View by ID
        lvMainKnockList = findViewById(R.id.listView_MainKnockList);
        lvMyKnockList = findViewById(R.id.listView_MyKnockList);
        btnMakeKnock = findViewById(R.id.button_MakeKnock);

        //initialize firebase
        kPostReference = FirebaseDatabase.getInstance().getReference();


        //make knockAdapter and set List
        allKnocks = new ArrayList<>();
        myKnocks = new ArrayList<>();

        allKnockAdapter = new KnockAdapter(this, allKnocks);
        myKnockAdapter = new KnockAdapter(this, myKnocks);

        lvMainKnockList.setAdapter(allKnockAdapter);
        lvMyKnockList.setAdapter(myKnockAdapter);


        //initialize basic information



        //if we click the list my knock list, go to KnockDetailActivity
        lvMyKnockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMyKnock = new Intent(KnockMainActivity.this, KnockDetailActivity.class);
                startActivity(intentMyKnock);
            }
        });

        //if we click button, go to SendKnockActivity
        btnMakeKnock.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendKnock = new Intent(KnockMainActivity.this, SendKnockActivity.class);
                startActivity(intentSendKnock);
            }
        });

        //get Firebase Database
        getFirebaseDatabase();

    }

    private void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                allKnocks.clear();
                myKnocks.clear();
                Log.d("onDataChange", "before Sanpshot");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Knock getKnock = postSnapshot.getValue(Knock.class);
                    String[] info = {getKnock.getContent(), getKnock.getSender(), getKnock.getReceiver(), getKnock.getDate()};
                    allKnocks.add(new Knock(info[0], info[1], info[2], info[3]));

                    // todo : 사용자 정보를 가져와 info[2]와 같으면 myKnocks에 추가하기
                    if(info[2].equals("1234")){
                        myKnocks.add(new Knock(info[0], info[1], info[2], info[3]));
                    }
                }
                Log.d("onDataChange", "finish add data");
                allKnockAdapter.notifyDataSetChanged();
                myKnockAdapter.notifyDataSetChanged();
                Log.d("onDataChange", "Update data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        Log.d("onDataChange", "reference change");


    kPostReference.child("ROOM").child("room1"/* todo : 초반에 방 정보등 초기화하여 이용*/).child("knock").addValueEventListener(postListener);

    }
}
