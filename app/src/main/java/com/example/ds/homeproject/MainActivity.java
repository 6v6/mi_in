package com.example.ds.homeproject;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Window;


public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference myRef;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();

    private EditText edEmail;
    private EditText edPw;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        edEmail = (EditText) findViewById(R.id.id);
        edPw = (EditText) findViewById(R.id.pw);


        //로그인 됐는지, 로그아웃 됐는지 확인하는 리스너
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //로그인
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                } else {
                    //로그아웃 상태
                }
            }
        };

        //레퍼런스 정보 가져오기
        myRef = Database.getReference("id_list");
        myRef.child("last").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                password = dataSnapshot.child("pw").getValue(String.class);
                email = dataSnapshot.child("email").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    //로그인 됐는지, 로그아웃 됐는지 확인하는 리스너할때 필요한 것 onstart onstop
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }


    private void signinAccount(String email, String passwd) {
        mAuth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Sing in Account:" + task.isSuccessful());

                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "환영합니다.", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());


                                } else {
                                    Toast.makeText(MainActivity.this, "Log In Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

    }

    //로그인
    public void login(View view) {
        String id = edEmail.getText().toString().trim();
        String pw = edPw.getText().toString().trim();
        Log.d(TAG, "Email:" + id + " Password:" + pw);

        if (isValidEmail(id) && isValidPasswd(pw)) {
            signinAccount(id, pw);

        } else {

            Toast.makeText(MainActivity.this, "이메일과 비밀번호를 입력하세요!", Toast.LENGTH_LONG).show();
        }
    }


    //회원가입
    public void join(View view) {
        Intent intent = new Intent(getApplication(), JoinActivity.class);
        startActivity(intent);
    }

    //맞는지 확인
    private boolean isValidPasswd(String pw) {
        if(pw == null || TextUtils.isEmpty(pw)){

            return false;
        } else {
            if(pw.length() > 4&&pw.equals(password)) {
                Log.d(TAG, "EmailPassword:" + password);
                return true;
            }
            else
                return false;
        }
    }
    private boolean isValidEmail(String id){
        if(id == null || TextUtils.isEmpty(id)){

            return false;
        } else {
            if(id.equals(email)) {
                Log.d(TAG, "EMAILid:" + email);
                return true;
            }
            else
               return Patterns.EMAIL_ADDRESS.matcher(id).matches();
        }
    }



    //홈 화면
     private void home() {
         Intent intent = new Intent(getApplication(), HomeActivity.class);
         startActivity(intent);
     }


 }

