package edu.skku.map.matpwdl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoRoomActivity extends AppCompatActivity {

    Button button_makeRoom, btn_logout;
    EditText editText_roomName;
    private DatabaseReference rPostReference;
    private DatabaseReference mPostReference;
    String biggest_room_id = "1";
    ArrayList<Room> rooms = new ArrayList<>();
    MyInformation myInfo;
    String user_ID;
    boolean gotData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_room);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.DefaultGray)));
        setTitle( "NO ROOM" );

        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");
        user_ID = myInfo.getID();

        rPostReference = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference();
        editText_roomName = findViewById(R.id.editText_roomName);
        button_makeRoom = findViewById(R.id.button_makeRoom);
        btn_logout = findViewById(R.id.button_logoutNoRoom);
        button_makeRoom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String roomName = editText_roomName.getText().toString();
                if(roomName.length()>0){
                    if(gotData);{
                        String room_id = String.valueOf(Integer.parseInt(biggest_room_id)+1);
                        postFirebaseDatabase(true,room_id,roomName);
                        postMemberFirebaseDatabase(true, myInfo.getID(),myInfo.getPW(),myInfo.getMemberid(),
                                myInfo.getName(),room_id,myInfo.getStatus());

                        Toast toast = Toast.makeText(getApplicationContext(),"방 생성이 완료되었습니다",Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                }
            }
        });
        btn_logout.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                SharedPreferences loginPref = getSharedPreferences("loginFile",MODE_PRIVATE);
                loginPref.edit().remove("firstLoginFlag").commit();
                loginPref.edit().remove("ID").commit();
                loginPref.edit().remove("PW").commit();
                Intent intent = new Intent(NoRoomActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("onDataChange","Data is Updated");
                rooms.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    Room get = postSnapshot.getValue(Room.class);
                    rooms.add(get);
                    if(Integer.parseInt(get.getRoom_id())>Integer.parseInt(biggest_room_id)){
                        biggest_room_id = get.getRoom_id();
                    }
                    Log.d("getFirebaseDatabase","key: "+ key);
                    //Log.d("getFirebaseDatabase",get.title);
                    gotData = true;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };
        rPostReference.child("ROOM").addValueEventListener(postListener);
    }

    public void postFirebaseDatabase(boolean add, String room_id, String room_name){
        Map<String,Object> childUpdates = new HashMap<>();
        Map<String,Object> postValues = null;
        if(add){
            Room post = new Room(room_id,room_name);
            postValues = post.toMap();
        }
        childUpdates.put("ROOM/room"+room_id, postValues);
        rPostReference.updateChildren(childUpdates);
    }

    public void postMemberFirebaseDatabase(boolean add, String ID, String PW, String memberid, String name, String room_id, String status){
        Map<String,Object> childUpdates = new HashMap<>();
        Map<String,Object> postValues = null;
        if(add){
            Roommatelistitem post = new Roommatelistitem(ID,PW,memberid,name,room_id,status);
            postValues = post.toMap();
        }
        childUpdates.put("MEMBER/member"+memberid, postValues);
        mPostReference.updateChildren(childUpdates);
    }
}
