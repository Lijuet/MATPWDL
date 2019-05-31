package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendKnockActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    EditText etMyKnockContent;
    MultiAutoCompleteTextView atMyKnockReciever;
    Button btnSendKnock;
    MyInformation myInfo;

    private String contents;
    private String[] recievers;
    private String knock_id;
    private ArrayList<String> members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_knock);

        //find id
        atMyKnockReciever = findViewById(R.id.autoText_MyKnockReciever);
        etMyKnockContent = findViewById(R.id.editText_MyKnockContent);
        btnSendKnock = findViewById(R.id.button_MyKnockSend);

        //set firebase
        mPostReference = FirebaseDatabase.getInstance().getReference();

        //todo myInfo 초기화
        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");
        knock_id = intent.getStringExtra("rule_id");

        //Auto complete Text View
        members = new ArrayList<String>();
        members = myInfo.getMembersID();
        atMyKnockReciever.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line ,members));
        atMyKnockReciever.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        //when click button, new knock sent
        btnSendKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get contents
                contents = etMyKnockContent.getText().toString();
                recievers = atMyKnockReciever.getText().toString().split(",");

                //post firebase
                if (contents.length() == 0 || recievers.length == 0) {//todo 받는 사람 팝업 메뉴로 선택하기
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

            //Todo 내가 포함되어있으면 안보내기
            for(String receiever :recievers) {
                if(!receiever.equals(" ")) {
                    Knock post = new Knock(contents, myInfo.getMyID(), receiever.trim(), time, knock_id);
                    postValues = post.toMap();
                    childUpdates.put("/ROOM/" + "room" + myInfo.getRoomID() + "/knock/knock" + knock_id, postValues);
                    knock_id = String.valueOf(Integer.valueOf(knock_id) + 1);
                }
            }
            knock_id = String.valueOf(Integer.valueOf(knock_id) - 1);
        }

        //todo 만약 시간이 15분이내 보낸 흔적이 3회이하이고
        mPostReference.updateChildren(childUpdates);
        clearET();
        Toast.makeText(getApplicationContext(), "Knock를 보냈습니다.", Toast.LENGTH_SHORT).show();
    }

    public void clearET() {
        atMyKnockReciever.setText("");
        etMyKnockContent.setText("");
    }
}
