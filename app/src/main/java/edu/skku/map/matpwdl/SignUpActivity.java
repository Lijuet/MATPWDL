package edu.skku.map.matpwdl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText editText_inputId;
    EditText editText_inputPw;
    EditText editText_chkPw;
    EditText editText_inputName;
    Button button_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editText_inputId = findViewById(R.id.editText_inputId);
        editText_inputPw = findViewById(R.id.editText_inputPw);
        editText_chkPw = findViewById(R.id.editText_chkPw);
        editText_inputName = findViewById(R.id.editText_inputName);
        button_submit = findViewById(R.id.button_submitSignUp);

        button_submit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

    }
}
