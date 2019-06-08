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
    EditText IDeditText, PWeditText;
    Button button;
    String id, pw, shakey, data;

    SharedPreferences loginPref;
    Intent intent;
    private DatabaseReference mDatabase,postRef;// ...
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(LoginActivity.this, HomeActivity.class);
        IDeditText = (EditText) findViewById(R.id.editid);
        PWeditText = (EditText) findViewById(R.id.editpw);
        button = (Button) findViewById(R.id.button);
        postRef = FirebaseDatabase.getInstance().getReference().child("MEMBER");
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database
        loginPref = this.getPreferences( Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPref.edit();
        String defaultValue = loginPref.getString("login", null);
        if (defaultValue != null) { //자동로그인
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
                    postRef.orderByChild("ID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        postRef.orderByChild("PW").equalTo(pw).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    shakey = id + " " + pw;
                                                    Log.d("shakey", shakey);
                                                    // Save LOGIN information in shared preferences
                                                    editor.putString("login", shakey);
                                                    editor.commit();
                                                    startActivity(intent);
                                                    finish();
                                                    //로그인 성공//


                                                } else {
                                                    Toast.makeText(LoginActivity.this, "failed pw", Toast.LENGTH_SHORT);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        Toast.makeText(LoginActivity.this, "failed id", Toast.LENGTH_SHORT);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                });

            }
        }});
    }
}