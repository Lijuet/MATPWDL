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

<<<<<<< HEAD
public class LoginActivity extends AppCompatActivity{
    //private DatabaseReference kPostReference, kPostReference2;  TODO MYINFO 초기화
=======
public class LoginActivity extends AppCompatActivity {
    private DatabaseReference kPostReference, kPostReference2;  //TODO MYINFO 초기화
>>>>>>> 238e29d362a7481c3e30afacecd17530fda18673
    EditText IDeditText, PWeditText;
    Button button;
    String id, pw, shakey, data, memberID, login, count;
    SharedPreferences loginPref;
    Intent intent;
    int i;
    MyInformation myInfo;
    private DatabaseReference mDatabase, postRef;// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
// Initialize Firebase Auth

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        intent = new Intent( LoginActivity.this, HomeActivity.class );
        IDeditText = (EditText) findViewById( R.id.editid );
        PWeditText = (EditText) findViewById( R.id.editpw );
        button = (Button) findViewById( R.id.button2 );
        mDatabase = FirebaseDatabase.getInstance().getReference(); //database
        /*loginPref = this.getPreferences( Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = loginPref.edit();
        String defaultValue = loginPref.getString("login", null);
        if (defaultValue != null) { //자동로그인
            startActivity(intent);
            finish();
        } */
        button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                id = IDeditText.getText().toString();
                pw = PWeditText.getText().toString();
=======
                id = IDeditText.getText().toString().trim();
                pw = PWeditText.getText().toString().trim();
                login = id +pw;
>>>>>>> 238e29d362a7481c3e30afacecd17530fda18673
                if ((id.length() * pw.length()) == 0) {
                    Toast.makeText( LoginActivity.this, "Type all info...", Toast.LENGTH_SHORT );
                } else {
<<<<<<< HEAD
                    for (i =1;i<3;i++){
                        count = String.valueOf( i );
                        postRef = FirebaseDatabase.getInstance().getReference().child( "MEMBER" ).child("member"+count);
                        postRef.orderByChild( "ID" ).equalTo( id ).addListenerForSingleValueEvent( new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot2) {
                                if (dataSnapshot2.exists()) {
                                    postRef.orderByChild( "PW" ).equalTo( pw ).addListenerForSingleValueEvent( new ValueEventListener(){
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot3) {
                                            if (dataSnapshot3.exists()) {
                                                    /*shakey = id + " " + pw;
                                                    Log.d("shakey", shakey);
                                                    // Save LOGIN information in shared preferences
                                                    editor.putString("login", shakey);
                                                    editor.commit(); */
                                                startActivity( intent );
                                                finish();
                                                //로그인 성공//
/*<<<<<<< HEAD
======= */
                                                    /* TODO MYINFO 초기화
                                                    myInfo = dataSnapshot.getValue(MyInformation.class);
                                                    getFirebaseDatabase();
                                                    */
                                                /* >>>>>>> ff4194d15ff9d6d521bbe2d1cc6752601d90e727 */
                                            } else {
                                                Toast.makeText( LoginActivity.this, "failed pw", Toast.LENGTH_SHORT );
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    } );
                                } else {
                                    Toast.makeText( LoginActivity.this, "failed id", Toast.LENGTH_SHORT );
=======
                    postRef.orderByChild("IDPW").equalTo(login).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        myInfo = dataSnapshot.getValue(MyInformation.class);
                                        Log.d("LoginTest",myInfo.getRoomID()+myInfo.getName());
                                        //getFirebaseDatabase();
                                        /*shakey = id + " " + pw;
                                        Log.d("shakey", shakey);
                                        // Save LOGIN information in shared preferences
                                        editor.putString("login", shakey);
                                        editor.commit(); */
                                        //intent.putExtra(myInfo);
                                        startActivity(intent);
                                        finish();
                                        //로그인 성공//
/*<<<<<<< HEAD======= */
/* >>>>>>> ff4194d15ff9d6d521bbe2d1cc6752601d90e727 */
                                    } else {
                                        Toast.makeText(LoginActivity.this, "failed id", Toast.LENGTH_SHORT);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
>>>>>>> 238e29d362a7481c3e30afacecd17530fda18673
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        } );
                    }



                }

            }

        } );
    }
<<<<<<< HEAD
}

/* TODO MYINFO 초기화
=======

>>>>>>> 238e29d362a7481c3e30afacecd17530fda18673
    private void getFirebaseDatabase() {
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String membersize = (String) dataSnapshot.child("membersize").getValue();

                    for(int i = 1; i < Integer.valueOf(membersize) + 1; i++){
                        memberID = (String) dataSnapshot.child("smp_"+ String.valueOf(i) + "member_id").getValue();
                        postRef.orderByChild("memberid").equalTo(memberID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    myInfo.addRoommatessID(Integer.valueOf(memberID), (String)postSnapshot.child("name").getValue());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        kPostReference.child("ROOM").child("room"+myInfo.getRoomID()).child("room_info").addValueEventListener(postListener);
    }
<<<<<<< HEAD
    */
=======

}
>>>>>>> 238e29d362a7481c3e30afacecd17530fda18673
