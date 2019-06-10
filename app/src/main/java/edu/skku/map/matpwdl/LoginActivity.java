package edu.skku.map.matpwdl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    private DatabaseReference kPostReference, kPostReference2;  //TODO MYINFO 초기화
    EditText IDeditText, PWeditText;
    Button button;
    String id, pw, shakey, data, memberID, login, count;
    SharedPreferences loginPref;
    boolean loginSuccess = false;
    Intent intent;
    int i;
    MyInformation myInfo;
    private DatabaseReference mDatabase, postRef;// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        kPostReference = FirebaseDatabase.getInstance().getReference();
        intent = new Intent(LoginActivity.this, HomeActivity.class);
        IDeditText = (EditText) findViewById(R.id.editid);
        PWeditText = (EditText) findViewById(R.id.editpw);
        button = (Button) findViewById(R.id.button2);
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database
        /*loginPref = this.getPreferences( Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPref.edit();
        String defaultValue = loginPref.getString("login", null);
        if (defaultValue != null) { //자동로그인
            startActivity(intent);
            finish();
        } */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSuccess = false;
                id = IDeditText.getText().toString().trim();
                pw = PWeditText.getText().toString().trim();
                if ((id.length() * pw.length()) == 0) {
                    Toast.makeText(LoginActivity.this, "Type all info...", Toast.LENGTH_SHORT);
                } else {
                    getFirebaseDatabase();
                    /*shakey = id + " " + pw;
                                        Log.d("shakey", shakey);
                                        // Save LOGIN information in shared preferences
                                        editor.putString("login", shakey);
                                        editor.commit(); */
                    if(loginSuccess) {

                        //intent.putExtra(myInfo);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT);
                    }
                }
            }
        });

    }

    private void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    myInfo = postSnapshot.getValue(MyInformation.class);
                    Log.d("LoginTest", myInfo.getID() + " " + myInfo.getPW());
                    if(myInfo.getID().equals(id) && myInfo.getPW().equals(pw)) loginSuccess = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        kPostReference.child("MEMBER").addValueEventListener(postListener);
    }

}