package edu.skku.map.matpwdl;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

//myKnocklist와 mainKnockList를 설정할 adpater입니다.
//item은 Knock.activity입니다.
//todo 마지막 항목이 잘림

public class KnockMainActivity extends AppCompatActivity {
    private DatabaseReference kPostReference;
    ListView lvMainKnockList, lvMyKnockList;
    FloatingActionButton btnMakeKnock;

    KnockAdapter allKnockAdapter;
    KnockAdapter myKnockAdapter;
    ArrayList<Knock> allKnocks;
    ArrayList<Knock> myKnocks;
    String biggest_knock_id = "0";
    MyInformation myInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knock_main);

        //initialize basic information
        InitForTest(); //for TEST TODO delete it after logi section


        //find View by ID
        lvMainKnockList = findViewById(R.id.listView_MainKnockList);
        lvMyKnockList = findViewById(R.id.listView_MyKnockList);
        btnMakeKnock = findViewById(R.id.button_MakeKnock);

        //getInstance of Firebase
        kPostReference = FirebaseDatabase.getInstance().getReference();



        //make knockAdapter and set List
        allKnocks = new ArrayList<>();
        myKnocks = new ArrayList<>();

        allKnockAdapter = new KnockAdapter(this, allKnocks, 1/*allKnockMode*/);
        myKnockAdapter = new KnockAdapter(this, myKnocks, 0)/*myKnockMode*/;

        lvMainKnockList.setAdapter(allKnockAdapter);
        lvMyKnockList.setAdapter(myKnockAdapter);


        //Popup message를 위한 async 설정 todo mainAPP 화면으로 이동



        //if we click the list my knock list, go to KnockDetailActivity
        lvMyKnockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMyKnock = new Intent(KnockMainActivity.this, KnockDetailActivity.class);
                intentMyKnock.putExtra("myInfo", myInfo);
                startActivity(intentMyKnock);
            }
        });

        //if we click button, go to SendKnockActivity
        btnMakeKnock.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendKnock = new Intent(KnockMainActivity.this, SendKnockActivity.class);

                String knock_id = Integer.toString((Integer.parseInt(biggest_knock_id) + 1));
                intentSendKnock.putExtra("rule_id",knock_id);
                intentSendKnock.putExtra("myInfo", myInfo);
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

                    String[] info = {getKnock.getContent(), getKnock.getSender(), getKnock.getReceiver().trim(), getKnock.getDate(), getKnock.getKnockID()};
                    allKnocks.add(new Knock(info[0], info[1], info[2], info[3], info[4]));

                    // todo : 받은거랑 보낸거랑 구분하기
                    if(info[1].equals(myInfo.getMyID()) || info[2].equals(myInfo.getMyID())){
                        myKnocks.add(new Knock(info[0], info[1], info[2], info[3], info[4]));

                    }
                    //가장 큰 key 값 찾기
                    if(Integer.parseInt(getKnock.getKnockID())>Integer.parseInt(biggest_knock_id)){
                        biggest_knock_id = getKnock.getKnockID();
                    }

                    Log.d("sendMessage","before if");
                    //나에게 새로 들어온 message 라면
                    if(info[2].equals(myInfo.getMyID()) && getKnock.getRead() == 0){
                        Log.d("sendMessage","notice");
                        PowerManager manager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                        boolean bScreenOn = manager.isScreenOn();
                        Log.d("sendMessage","check SCREENON");

                        Intent intentPopup = new Intent(getApplicationContext(), KnockPopupActivity.class);
                        intentPopup.putExtra("NEWKNOCK", getKnock);
                        intentPopup.putExtra("myinfo",myInfo);
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
        kPostReference.child("ROOM").child("room"+myInfo.getRoomID()/* todo : 초반에 방 정보등 초기화하여 이용*/).child("knock").addValueEventListener(postListener);
    }

    private void InitForTest(){
        ArrayList<String> temp = new ArrayList<>();
        temp.add("1234");
        temp.add("4822");
        temp.add("1111");
        myInfo = new MyInformation();
        myInfo.setMyID("1234");
        myInfo.setName("member1_name");
        myInfo.setRoomID("1");
        myInfo.setStatus("재실");
        myInfo.setMembersID(temp);
    }
}
