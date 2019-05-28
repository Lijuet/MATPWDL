package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddEditRuleActivity extends AppCompatActivity {

    private DatabaseReference rPostReference;
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
        rPostReference = FirebaseDatabase.getInstance().getReference();

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

        //글을 수정하는 경우 수정할 글의 정보를 받아온다.
        Intent intent = getIntent();
        final String rule_id = intent.getStringExtra("rule_id");
        String day = intent.getStringExtra("day");
        String time = intent.getStringExtra("time");
        editText_ruleTitle.setText(intent.getStringExtra("title"));
        editText_editRule.setText(intent.getStringExtra("content"));
        if((intent.getIntExtra("repeat",0)) == 1){
            checkBox_repeat.setChecked(true);
        }
        if(day.contains("월")) checkBox_mon.setChecked(true);
        if(day.contains("화")) checkBox_tue.setChecked(true);
        if(day.contains("수")) checkBox_wed.setChecked(true);
        if(day.contains("목")) checkBox_thu.setChecked(true);
        if(day.contains("금")) checkBox_fri.setChecked(true);
        if(day.contains("토")) checkBox_sat.setChecked(true);
        if(day.contains("일")) checkBox_sun.setChecked(true);
        int time_index = time.indexOf(":");
        String hour = "", minute = "";
        for(int i=0; i<time_index; i++){
            hour+=time.charAt(i);
        }
        for(int i=time_index+1; i<time.length(); i++){
            minute+=time.charAt(i);
        }
        editText_hour.setText(hour);
        editText_minute.setText(minute);

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
                String title = editText_ruleTitle.getText().toString();
                String content = editText_editRule.getText().toString();
                String time = editText_hour.getText().toString() + ":" + editText_minute.getText().toString();
                String day = "";
                int repeat = 0;
                if(checkBox_repeat.isChecked()) repeat = 1;
                if(checkBox_mon.isChecked()) day+="월 ";
                if(checkBox_tue.isChecked()) day+="화 ";
                if(checkBox_wed.isChecked()) day+="수 ";
                if(checkBox_thu.isChecked()) day+="목 ";
                if(checkBox_fri.isChecked()) day+="금" ;
                if(checkBox_sat.isChecked()) day+="토 ";
                if(checkBox_sun.isChecked()) day+="일 ";

                String member="";//임시
                //데이터베이스에 업로드
                postFirebaseDatabase(true,title,content,day,member,repeat,rule_id,time);
                //종료
                finish();
            }
        });

    }

    public void postFirebaseDatabase(boolean add, String title, String content, String day, String member, int repeat, String rule_id, String time){
        Map<String,Object> childUpdates = new HashMap<>();
        Map<String,Object> postValues = null;
        if(add){
            ListViewRuleItem post = new ListViewRuleItem(content,day,member,repeat,rule_id,time,title);
            postValues = post.toMap();
        }
        childUpdates.put("ROOM/room1/rule/rule"+rule_id , postValues);
        rPostReference.updateChildren(childUpdates);
    }
}
