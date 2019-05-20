package edu.skku.map.matpwdl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class SendKnockActivity extends AppCompatActivity {
    EditText etMyKnockReciever, etMyKnockContent;
    RadioButton btnOpt1_Im, btnOpt2_3m, btnOpt3_5m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_knock);

        etMyKnockReciever = findViewById(R.id.editText_MyKnockReciever);
        etMyKnockContent = findViewById(R.id.editText_MyKnockContent);
        btnOpt1_Im = findViewById(R.id.btnOpt1_im);
        btnOpt2_3m = findViewById(R.id.btnOpt2_3m);
        btnOpt3_5m = findViewById(R.id.btnOpt3_5m);

        btnOpt1_Im.setOnClickListener(optionOnClickLister);
        btnOpt2_3m.setOnClickListener(optionOnClickLister);
        btnOpt3_5m.setOnClickListener(optionOnClickLister);

    }

    RadioButton.OnClickListener optionOnClickLister = new RadioButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            //Depending on radion button option check, determine when a knock will send
        }
    };
}
