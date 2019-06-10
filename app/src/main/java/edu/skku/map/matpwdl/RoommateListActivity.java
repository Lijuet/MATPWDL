package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoommateListActivity extends AppCompatActivity{
    private DatabaseReference mPostReference;
    String ID,name,status = "";
    EditText stET, newmemberET;
    Button stbtn, newbtn;
    String mystatus = "";
    MyInformation myInfo;
    ListView listView;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    private String mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_roommate_list );
        data = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        stET = (EditText) findViewById( R.id.stET );
        newmemberET = (EditText) findViewById( R.id.idET );
        stbtn = (Button) findViewById( R.id.button );
        newbtn = (Button) findViewById( R.id.listbtn );
        listView = (ListView) findViewById( R.id.roomlist );
        mPostReference = FirebaseDatabase.getInstance().getReference();

        //Initialize MyInformation
        myInfo = new MyInformation();
        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");

        //set Adapter
        //make knockAdapter and set List
        listView.setAdapter(arrayAdapter);

        Log.d("RoommateTest", myInfo.getRoomID());
        stbtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mystatus = stET.getText().toString();
                if (mystatus.length() == 0) {
                    Toast.makeText( RoommateListActivity.this, "Data is missing", Toast.LENGTH_SHORT ).show();
                } else {
                    postmystatusDatabase( true );
                }
            }
        } );
        newbtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ID = newmemberET.getText().toString();
                if (ID.length() == 0) {
                    Toast.makeText( RoommateListActivity.this, "Data is missing", Toast.LENGTH_SHORT ).show();
                } else {
                    postnewmember( true );
                }
            }
        } );
        getroomlist();
        getstatus();
    }

   public void getroomlist() {
       final ValueEventListener postListener = new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Log.d("onDataChange", "Data is Updated");
               data.clear();
               for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                   String key = postSnapshot.getKey();
                   Roommatelistitem get = postSnapshot.getValue(Roommatelistitem.class);
                   Log.d("getFirebaseDatabase", "info: " + get.getName());
                   if(get.getRoomID().equals(myInfo.getRoomID())) {
                       String[] info = {get.getName(), get.getStatus()};
                       String result = info[0] + " : " + info[1];
                       data.add(result);
                       Log.d("getFirebaseDatabase", "key: " + key);
                       Log.d("getFirebaseDatabase", "info: " + info[0] + info[1]);
                   }
               }
               arrayAdapter.notifyDataSetChanged();
           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {
           }
       };
       //mPostReference.child("ROOM/room"+ myInfo.getRoomID()+"/member").addValueEventListener(postListener);
        mPostReference.child("MEMBER").addValueEventListener(postListener);
   }

    public String getmembercount() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    roommembercount get = postSnapshot.getValue( roommembercount.class );
                    mc = get.membersize;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("ROOM/room"+ myInfo.getRoomID()+"/member").addValueEventListener(postListener);
        return mc;
    }



    public void postnewmember(boolean add){
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;
        String membercount;
        membercount = getmembercount();
            if(add){
                Roommatelistitem post = new Roommatelistitem(ID);
                postValues = post.toMap();
                childUpdates.put("/ROOM/" + "room"+ myInfo.getRoomID() + "/member/" + "smp_"+ membercount +"memberid", ID);//smp_x수치 조정 피룡
            }
            mPostReference.child( "ROOM" ).child( "member" ).child( "membersize" ).setValue(  String.valueOf (membercount+1) );

            mPostReference.updateChildren(childUpdates);
            clearET();
    }

    public void getstatus() {
        stET.setText( myInfo.getStatus() );
    }

    public void postmystatusDatabase(boolean add) {
        if (add) {
        }
        mPostReference.child( "MEMBER" ).child( "member" + myInfo.getMemberid() ).child( "status" ).setValue( mystatus );
        clearET();
    }

    public void clearET() {
        stET.setText( "" );
        mystatus = "";
    }
}