package edu.skku.map.matpwdl;

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

import java.util.HashMap;
import java.util.Map;

public class SendKnockActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    EditText etMyKnockReciever, etMyKnockContent;
    RadioButton btnOpt1_Im, btnOpt2_3m, btnOpt3_5m;
    Button btnSendKnock;

    String contents, reciever;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_knock);

        //find id
        etMyKnockReciever = findViewById(R.id.editText_MyKnockReciever);
        etMyKnockContent = findViewById(R.id.editText_MyKnockContent);
        btnSendKnock = findViewById(R.id.button_MyKnockSend);
        btnOpt1_Im = findViewById(R.id.btnOpt1_im);
        btnOpt2_3m = findViewById(R.id.btnOpt2_3m);
        btnOpt3_5m = findViewById(R.id.btnOpt3_5m);

        btnOpt1_Im.setOnClickListener(optionOnClickLister);
        btnOpt2_3m.setOnClickListener(optionOnClickLister);
        btnOpt3_5m.setOnClickListener(optionOnClickLister);

        //set firebase
        mPostReference = FirebaseDatabase.getInstance().getReference();


        //when click button, new knock sent
        btnSendKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get contents
                contents = etMyKnockContent.getText().toString();
                reciever = etMyKnockReciever.getText().toString();
                //make new knock

                //post firebase
                if (contents.length() != 0 && reciever.length() != 0) {
                    Toast.makeText(SendKnockActivity.this, "Data is missing", Toast.LENGTH_SHORT).show();
                } else {
                    postFirebaseDatabase(true);
                }
            }
        });
    }

    RadioButton.OnClickListener optionOnClickLister = new RadioButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            //Depending on radion button option check, determine when a knock will send
        }
    };

    public void postFirebaseDatabase(boolean add) {
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            Knock post = new Knock(contents,  "1234"/* todo 내정보 class에서 가져오기*/, reciever, "201905271557"/* todo 날짯 설정*/);
            postValues = post.toMap();
        }
        //childUpdates.put("/memo_list/" + content, postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }


    public void clearET() {
        etMyKnockReciever.setText("");
        etMyKnockContent.setText("");

    }
}
