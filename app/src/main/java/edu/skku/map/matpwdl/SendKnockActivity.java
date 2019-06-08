package edu.skku.map.matpwdl;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView tvMyKnockReciever;
    Button btnSendKnock;
    MyInformation myInfo;

    String contents;
    ArrayList<String> recieversID;
    String knock_id;
    ArrayList<String> roommates;
    ArrayList<String> receivers;
    boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_knock);

        //find id
        tvMyKnockReciever = findViewById(R.id.autoText_MyKnockReciever);
        etMyKnockContent = findViewById(R.id.editText_MyKnockContent);
        btnSendKnock = findViewById(R.id.button_MyKnockSend);

        //set firebase
        mPostReference = FirebaseDatabase.getInstance().getReference();

        //todo myInfo 초기화
        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");
        knock_id = intent.getStringExtra("rule_id");
        roommates = new ArrayList<>();
        checkedItems = new boolean[roommates.size()];
        receivers = new ArrayList<>();
        recieversID = new ArrayList<>();
        tvMyKnockReciever.setText("");
        for(Integer i : myInfo.getRoommatessID().keySet()){
            roommates.add(String.valueOf(i) + ":" + myInfo.getRoommatessID().get(i));
        }
        tvMyKnockReciever.setText("");

        //when click Receiver TextView, dialog
        tvMyKnockReciever.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedItems = new boolean[roommates.size()];
                receivers = new ArrayList<>();
                recieversID = new ArrayList<>();
                tvMyKnockReciever.setText("");

                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(SendKnockActivity.this);

                builder.setTitle("보낼 사람을 선택하시오");
                builder.setMultiChoiceItems(roommates.toArray(new String[roommates.size()]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedItemId, boolean isSelected) {
                        if (isSelected) {
                            receivers.add(roommates.get(selectedItemId));
                        } else {
                            receivers.remove(roommates.get(selectedItemId));
                        }
                    }
                }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("Dialog",String.valueOf(receivers.size()));
                        for(String selectedPerson : receivers){
                            String[] tempInfo = selectedPerson.split(":");
                            tvMyKnockReciever.setText(tvMyKnockReciever.getText().toString() + tempInfo[1] +",");
                            recieversID.add(tempInfo[0].trim());
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });


/*

        for(Integer i : myInfo.getRoommatessID().keySet()){
            roommates.add(myInfo.getRoommatessID().get(i));
        }
*/
        //when click button, new knock sent
        btnSendKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get contents
                contents = etMyKnockContent.getText().toString();

                //post firebase
                if (contents.length() == 0 || recieversID.size() == 0) {//todo 받는 사람 팝업 메뉴로 선택하기
                    Toast.makeText(SendKnockActivity.this, "Data is missing", Toast.LENGTH_SHORT).show();
                    Log.d("toMeTest", "First");
                }
                else if(isValdiateReceivers(recieversID)) {
                    Log.d("toMeTest", "Second");
                    //Log.d("test", roommates.get(0));
                    postFirebaseDatabase(true);
                    finish();
                }
                else{

                    Log.d("toMeTest", "Third");

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
            for(String receiever : recieversID) {
                if(!receiever.equals(" ")) {
                    Knock post = new Knock(contents, myInfo.getMemberid(), receiever.trim(), time, knock_id);
                    postValues = post.toMap();
                    //TODO 동명이인 처리하는 액티비티뷰 띄우기
                    childUpdates.put("/ROOM/" + "room" + myInfo.getRoomID() + "/knock/knock" + knock_id, postValues);
                    knock_id = String.valueOf(Integer.valueOf(knock_id) + 1);
                }
            }
            knock_id = String.valueOf(Integer.valueOf(knock_id) - 1);
        }

        mPostReference.updateChildren(childUpdates);
        clearET();
        Toast.makeText(getApplicationContext(), "Knock를 보냈습니다.", Toast.LENGTH_SHORT).show();
    }

    public void clearET() {
        tvMyKnockReciever.setText("");
        etMyKnockContent.setText("");
    }

    private boolean isValdiateReceivers(ArrayList<String> receievers){
        for(int i = 0 ; i < receievers.size(); i++){
            Log.d("toMeTest", "testValidate");
            String receieverID = receievers.get(i);
            if(receieverID.equals(" ")) continue;
            if(receieverID.equals(myInfo.getMemberid())){
                Log.d("toMeTest", "beforeToast");
                Toast.makeText(getApplicationContext(), "나 자신에게는 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                Log.d("toMeTest", "Toast Successful");
                return false;
            }
        }
        return true;
    }
}
