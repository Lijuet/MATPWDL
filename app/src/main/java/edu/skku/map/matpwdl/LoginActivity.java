package edu.skku.map.matpwdl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //todo 메세지에서 login 화면으로 올때 기존 메세지 화면 종료시키기 및 그냥 켰을 때 오류 안나는지?
    }
}
