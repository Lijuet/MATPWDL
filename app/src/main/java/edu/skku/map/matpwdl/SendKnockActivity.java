package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendKnockActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    EditText etMyKnockReciever, etMyKnockContent;
    Button btnSendKnock;

    String contents, reciever;
    String knock_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_knock);

        //find id
        etMyKnockReciever = findViewById(R.id.editText_MyKnockReciever);
        etMyKnockContent = findViewById(R.id.editText_MyKnockContent);
        btnSendKnock = findViewById(R.id.button_MyKnockSend);

        //set firebase
        mPostReference = FirebaseDatabase.getInstance().getReference();

        //
        Intent intent = getIntent();
        knock_id = intent.getStringExtra("rule_id");

        //when click button, new knock sent
        btnSendKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get contents
                contents = etMyKnockContent.getText().toString();
                reciever = etMyKnockReciever.getText().toString();

                //post firebase
                if (contents.length() == 0 && reciever.length() == 0) {//todo 받는 사람 팝업 메뉴로 선택하기
                    Toast.makeText(SendKnockActivity.this, "Data is missing", Toast.LENGTH_SHORT).show();

                } else {
                    postFirebaseDatabase(true);
                }
            }
        });
    }


    public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        //내용 채워넣기
        //1.내용, 보내는 사람, 받는 사람, 날짜, id 으로 knock 만들기
        //todo 2. 시간 제한
        if (add) {
            //보내는 시간 얻음.
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
            String time = sdf.format(date);

            Knock post = new Knock(contents,  "1234"/* todo 내정보 class에서 가져오기*/, reciever, time, knock_id);
            postValues = post.toMap();
        }

        //만약 시간이 15분이내 보낸 흔적이 3회이하이고
        if(/*todo */true) {
            childUpdates.put("/ROOM/" + "room1" + "/knock/knock" + knock_id, postValues);
            mPostReference.updateChildren(childUpdates);
            clearET();
            Toast.makeText(getApplicationContext(), "Knock를 보냈습니다.", Toast.LENGTH_SHORT).show();
        }//그렇지 않으면 거절 메세지 띄우기
        else{
            //
        }
    }

    public void clearET() {
        etMyKnockReciever.setText("");
        etMyKnockContent.setText("");
    }
}
