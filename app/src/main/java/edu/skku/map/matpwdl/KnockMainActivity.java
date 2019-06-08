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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//myKnocklist와 mainKnockList를 설정할 adpater입니다.
//item은 Knock.activity입니다.
//todo 마지막 항목이 잘림

public class KnockMainActivity extends AppCompatActivity {
    private DatabaseReference kPostReference;
    ListView lvAllKnockList, lvMyKnockList;
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
        lvAllKnockList = findViewById(R.id.listView_MainKnockList);
        lvMyKnockList = findViewById(R.id.listView_MyKnockList);
        btnMakeKnock = findViewById(R.id.button_MakeKnock);

        //getInstance of Firebase
        kPostReference = FirebaseDatabase.getInstance().getReference();

        //makr Comparator
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o, Object t) {
                Knock oK = (Knock)o, tK = (Knock)t;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
                try {
                    Date oDate = sdf.parse(oK.getDate());
                    Date tDate = sdf.parse(tK.getDate());

                    long oDateValue = oDate.getTime(), tDateValue = tDate.getTime();

                    Log.d("sortKnock",String.valueOf(oDateValue));
                    Log.d("sortKnock",String.valueOf(tDateValue));
                    Log.d("sortKnock","before compare");
                    return (int)(oDateValue - tDateValue);

                } catch (ParseException e) {
                    Log.d("sortKnock",e.getMessage());
                }

                return 0;
            }
        };

        //make knockAdapter and set List
        allKnocks = new ArrayList<>();
        myKnocks = new ArrayList<>();

        allKnockAdapter = new KnockAdapter(this, allKnocks, myInfo ,1/*allKnockMode*/);
        myKnockAdapter = new KnockAdapter(this, myKnocks, myInfo,2)/*myKnockMode*/;

        lvAllKnockList.setAdapter(allKnockAdapter);
        lvMyKnockList.setAdapter(myKnockAdapter);


        //Popup message를 위한 async 설정 todo mainAPP 화면으로 이동



        //if we click the list my knock list, go to KnockDetailActivity
        lvMyKnockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMyKnock = new Intent(KnockMainActivity.this, KnockDetailActivity.class);
                intentMyKnock.putExtra("myInfo", myInfo);
                intentMyKnock.putExtra("knocks", myKnocks);
                intentMyKnock.putExtra("mode","myKnocks");
                startActivity(intentMyKnock);
            }
        });

        //if we click the list my knock list, go to KnockDetailActivity
        lvAllKnockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentAllKnock = new Intent(KnockMainActivity.this, KnockDetailActivity.class);
                intentAllKnock.putExtra("myInfo", myInfo);
                intentAllKnock.putExtra("knocks", allKnocks);
                intentAllKnock.putExtra("mode","allKnocks");
                startActivity(intentAllKnock);
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
                    Knock newKnock = new Knock(info[0], info[1], info[2], info[3], info[4]);
                    newKnock.setRead(getKnock.getRead());
                    allKnocks.add(newKnock);

                    // todo : 받은거랑 보낸거랑 구분하기
                    if(info[1].equals(myInfo.getMemberid()) || info[2].equals(myInfo.getMemberid())){
                        myKnocks.add(newKnock);

                    }
                    //가장 큰 key 값 찾기
                    if(Integer.parseInt(getKnock.getKnockID())>Integer.parseInt(biggest_knock_id)){
                        biggest_knock_id = getKnock.getKnockID();
                    }

                    Log.d("sendMessage","before if");
                    //나에게 새로 들어온 message 라면
                    if(info[2].equals(myInfo.getMemberid()) && getKnock.getRead() == 0){
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
                allKnockAdapter.SortKnocks();
                myKnockAdapter.SortKnocks();
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

