package edu.skku.map.matpwdl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText IDeditText, PWeditText;
    Button button;
    String id, pw, shakey;
    SharedPreferences loginPref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, HomeActivity.class);
        IDeditText = (EditText) findViewById(R.id.editid);
        PWeditText = (EditText) findViewById(R.id.editpw);
        button = (Button) findViewById(R.id.button);
        loginPref = this.getPreferences( Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPref.edit();
        String defaultValue = loginPref.getString("login", null);
        if (defaultValue != null) {
            startActivity(intent);
            finish();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = IDeditText.getText().toString();
                pw = PWeditText.getText().toString();
                if ((id.length() * pw.length()) == 0) {
                    Toast.makeText(LoginActivity.this, "Type all info...", Toast.LENGTH_SHORT);
                } else {
                    shakey = id + " " + pw;
                    Log.d("shakey", shakey);
                    // Save LOGIN information in shared preferences
                    editor.putString("login", shakey);
                    editor.commit();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}