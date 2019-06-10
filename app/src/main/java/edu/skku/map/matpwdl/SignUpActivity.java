package edu.skku.map.matpwdl;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mPostReference;
    EditText editText_inputId;
    EditText editText_inputPw;
    EditText editText_chkPw;
    EditText editText_inputName;
    Button button_submit;
    String biggest_member_id = "0";
    ArrayList<Roommatelistitem> members = new ArrayList<>();
    boolean isSameID = false;
    boolean checking = false;
    boolean brought = false;
    //boolean modifing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mPostReference = FirebaseDatabase.getInstance().getReference();

        editText_inputId = findViewById(R.id.editText_inputId);
        editText_inputPw = findViewById(R.id.editText_inputPw);
        editText_chkPw = findViewById(R.id.editText_chkPw);
        editText_inputName = findViewById(R.id.editText_inputName);
        button_submit = findViewById(R.id.button_submitSignUp);

        getFirebaseDatabase();

        button_submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                while(!brought);
                String id = editText_inputId.getText().toString();
                String pw = editText_inputPw.getText().toString();
                String chkPw = editText_chkPw.getText().toString();
                String name = editText_inputName.getText().toString();
                if((id.length()*pw.length()*chkPw.length()*name.length())!=0){
                    if(pw.equals(chkPw)){
                        checking = true;
                        isSameID = false;
                        for(int i=0; i<members.size(); i++){
                            if(members.get(i).getID().equals(id)){
                                isSameID = true;
                                break;
                            }
                        }
                        checking = false;
                        if(isSameID){
                            Toast toast = Toast.makeText(getApplicationContext(),"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            String memberid = String.valueOf(Integer.parseInt(biggest_member_id)+1);
                            postFirebaseDatabase(true,id,pw,memberid,name,"0","");
                            Toast toast = Toast.makeText(getApplicationContext(),"가입이 완료되었습니다",Toast.LENGTH_SHORT);
                            toast.show();
                            finish();
                        }
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),"비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

    }

    public void postFirebaseDatabase(boolean add, String ID, String PW, String memberid, String name, String room_id, String status){
        Map<String,Object> childUpdates = new HashMap<>();
        Map<String,Object> postValues = null;
        if(add){
            Roommatelistitem post = new Roommatelistitem(ID,PW,memberid,name,room_id,status);
            postValues = post.toMap();
        }
        childUpdates.put("MEMBER/member"+memberid, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){
        final ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("onDataChange","Data is Updated");
                while(checking);
                members.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    Roommatelistitem get = postSnapshot.getValue(Roommatelistitem.class);
                    members.add(get);
                    if(Integer.parseInt(get.getMemberid())>Integer.parseInt(biggest_member_id)){
                        biggest_member_id = get.getMemberid();
                    }
                    Log.d("getFirebaseDatabase","key: "+ key);
                    //Log.d("getFirebaseDatabase",get.title);
                }
                brought = true;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){

            }
        };

        mPostReference.child("MEMBER").addValueEventListener(postListener);
    }
}
