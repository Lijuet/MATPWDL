package edu.skku.map.matpwdl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private DatabaseReference kPostReference;
    EditText IDeditText, PWeditText;
    Button button;
    String id, pw, shakey;
    boolean loginSuccess = false;
    Intent intent;
    MyInformation myInfo;
    String defaultValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kPostReference = FirebaseDatabase.getInstance().getReference();
        intent = new Intent(LoginActivity.this, HomeActivity.class);
        IDeditText = (EditText) findViewById(R.id.editid);
        PWeditText = (EditText) findViewById(R.id.editpw);
        button = (Button) findViewById(R.id.button2);
        myInfo = new MyInformation();

        //자동 로그인 파트

        SharedPreferences sf = getSharedPreferences("loginFile",MODE_PRIVATE);
        defaultValue = sf.getString("firstLoginFlag", null);
        if (defaultValue != null) { //자동로그인
            id = sf.getString("ID","");
            pw = sf.getString("PW","");
            if(!id.equals("")&&!pw.equals("")) {
                getFirebaseDatabase();
                finish();
            }
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSuccess = false;
                id = IDeditText.getText().toString().trim();
                pw = PWeditText.getText().toString().trim();
                if ((id.length() * pw.length()) == 0) {
                    Toast.makeText(LoginActivity.this, "Type all info...", Toast.LENGTH_SHORT);
                } else {
                    getFirebaseDatabase();

                }
            }
        });

    }

    private void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyInformation temp = postSnapshot.getValue(MyInformation.class);
                    Log.d("LoginTest", temp.getID() + " " + temp.getPW() + " " + String.valueOf(loginSuccess));
                    if(temp.getID().equals(id) && temp.getPW().equals(pw)) {
                        loginSuccess = true;
                        myInfo = temp;
                    }
                }
                login();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        kPostReference.child("MEMBER").addValueEventListener(postListener);
    }

    void login(){
        if(loginSuccess) {
            if(defaultValue == null) {
                SharedPreferences loginPref = getSharedPreferences("loginFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = loginPref.edit();
                shakey = id + " " + pw;
                editor.putString("firstLoginFlag", shakey);
                editor.putString("ID", id);
                editor.putString("PW", pw);
                editor.commit();
            }
            getroomlist();
        }
        else{
            Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT);
        }
    }

    public void getroomlist() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<Integer, String> roommatessID = new HashMap<>();
                Log.d("onDataChange", "Data is Updated");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    Roommatelistitem get = postSnapshot.getValue(Roommatelistitem.class);
                    Log.d("getFirebaseDatabase", "info: " + get.getName());
                    if(get.getRoomID().equals(myInfo.getRoomID())) {
                        Log.d("LoginTest",get.getMemberid() + get.getName());
                        roommatessID.put(Integer.valueOf(get.getMemberid()), get.getName());
                    }
                }
                myInfo.setRoommatessID(roommatessID);

                intent.putExtra("myInfo", myInfo);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        kPostReference.child("MEMBER").addValueEventListener(postListener);
    }
}