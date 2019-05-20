package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class KnockMainActivity extends AppCompatActivity {
    ListView lvMainKnockList, lvMyKnockList;
    FloatingActionButton btnMakeKnock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knock_main);

        //find View by ID
        lvMainKnockList = findViewById(R.id.listView_MainKnockList);
        lvMyKnockList = findViewById(R.id.listView_MyKnockList);
        btnMakeKnock = findViewById(R.id.button_MakeKnock);

        //if we click the list my knock list, go to MyKnockActivity
        lvMyKnockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyKnock = new Intent(KnockMainActivity.this, MyKnockActivity.class);
                startActivity(intentMyKnock);
            }
        });

        //if we click button, go to SendKnockActivity
        btnMakeKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSendKnock = new Intent(KnockMainActivity.this, SendKnockActivity.class);
                startActivity(intentSendKnock);
            }
        });


    }
}
