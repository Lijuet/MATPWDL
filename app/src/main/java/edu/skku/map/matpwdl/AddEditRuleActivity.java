package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddEditRuleActivity extends AppCompatActivity {

    Button button_addMember;
    Button button_save;
    CheckBox checkBox_repeat;
    CheckBox checkBox_mon;
    CheckBox checkBox_tue;
    CheckBox checkBox_wed;
    CheckBox checkBox_thu;
    CheckBox checkBox_fri;
    CheckBox checkBox_sat;
    CheckBox checkBox_sun;
    EditText editText_ruleTitle;
    EditText editText_editRule;
    EditText editText_hour;
    EditText editText_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_rule);

        //Intent intent = getIntent();
        //String rule_id =intent.getStringExtra("rule_id");

        button_addMember = findViewById(R.id.button_addmember);
        button_save = findViewById(R.id.button_save);
        checkBox_repeat = findViewById(R.id.checkBox_repeat);
        checkBox_mon = findViewById(R.id.checkBox_mon);
        checkBox_tue = findViewById(R.id.checkBox_tue);
        checkBox_wed = findViewById(R.id.checkBox_wed);
        checkBox_thu = findViewById(R.id.checkBox_thu);
        checkBox_fri = findViewById(R.id.checkBox_fri);
        checkBox_sat = findViewById(R.id.checkBox_sat);
        checkBox_sun = findViewById(R.id.checkBox_sun);
        editText_ruleTitle = findViewById(R.id.editText_ruleTitle);
        editText_editRule = findViewById(R.id.editText_editRule);
        editText_hour = findViewById(R.id.editText_hour);
        editText_minute = findViewById(R.id.editText_minute);

        button_addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //멤버 팝업창
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //작성한 정보 저장
                finish();
            }
        });

    }
}
