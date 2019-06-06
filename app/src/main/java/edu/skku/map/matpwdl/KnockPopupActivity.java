package edu.skku.map.matpwdl;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KnockPopupActivity extends AppCompatActivity {
    public static Activity _KnockPopupActivity;
    TextView tvKnockPopupContnet;
    Button btnCheckPopup, btnCheckGoApp;
    Knock newKnock;
    MyInformation myInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _KnockPopupActivity = KnockPopupActivity.this;
        setContentView(R.layout.activity_knock_popup);

        tvKnockPopupContnet = findViewById(R.id.textView_KnockPopupContnet);
        btnCheckPopup = findViewById(R.id.button_popupCheck);
        btnCheckGoApp = findViewById(R.id.button_popupGoApp);

        Intent intent = getIntent();
        Boolean SCREENON = intent.getBooleanExtra("SCREENON", true);
        newKnock = (Knock) intent.getSerializableExtra("NEWKNOCK");
        myInfo = (MyInformation) intent.getSerializableExtra("myinfo");

        if(!SCREENON) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

        tvKnockPopupContnet.setText(newKnock.getContent());

        btnCheckPopup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                newKnock.setRead(1);
                postFirebaseDatabase(true);
                finish();
            }
        });

        btnCheckGoApp.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                newKnock.setRead(1);
                postFirebaseDatabase(true);
                Intent intentGoLogoin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentGoLogoin);
                finish();//todo login activity 가서 finish하기
            }
        });
    }


    public void postFirebaseDatabase(boolean add) {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        if(add){
            try {
                Log.d("readKnock","before");
                Knock post = newKnock;
                postValues = post.toMap();
                Log.d("readKnock",String.valueOf(post.getRead()));

                childUpdates.put("/ROOM/" + "room" + myInfo.getRoomID() + "/knock/knock" + newKnock.getKnockID(), postValues);
                mPostReference.updateChildren(childUpdates);
                Log.d("readKnock","finish");
            }catch (Exception e){
                Log.d("readKnock",e.getMessage());
                Toast.makeText(getApplicationContext(), "Can not update newKnock",Toast.LENGTH_SHORT).show();
            }
        }



    }
}

