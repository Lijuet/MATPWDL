package edu.skku.map.matpwdl;

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

public class RoommateListActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String ID, name = "";
    EditText stET;
    Button btn;
    String mystatus = "";
    MyInformation myinfo;
    ListView listView;
    ArrayList<String> data;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roommate_list);
        data = new ArrayList<String>();
        stET = (EditText)findViewById(R.id.idET);
        btn = (Button)findViewById(R.id.listbtn);
        listView = (ListView)findViewById(R.id.roomlist);
        mPostReference = FirebaseDatabase.getInstance().getReference();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mystatus = stET.getText().toString();
                if (mystatus.length() == 0){
                    Toast.makeText(RoommateListActivity.this, "Data is missing", Toast.LENGTH_SHORT).show();
                }else{

                }
            }
            });

    }


    public void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("onDataChange", "Data is Updated");
                data.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Roommatelistitem get = postSnapshot.getValue(Roommatelistitem.class);
                    String[] info = {get.id, get.roommatename, get.status};
                    String result = info[0]+ " "  + info[1] + " " + info[2] ;
                    data.add(result);
                    Log.d("getFirebaseDatabase", "key: " + key);
                    Log.d("getFirebaseDatabase", "info: " + info[0] + info[1] + info[2]);
                }
                arrayAdapter.clear();
                arrayAdapter.addAll(data);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mPostReference.child("MEMBER").addValueEventListener(postListener);
    }
    public void postFirebaseDatabase(boolean add){

        if(add){
        }

        mPostReference.child("MEMBER").child( "member"+myinfo.getMemberid() ).child("status").setValue( mystatus );
        clearET();
    }
    public void clearET () {
        stET.setText("");
        mystatus = "";
    }
}