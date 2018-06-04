package com.example.ds.homeproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Window;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private EditText id;
    private EditText pw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       mAuth = FirebaseAuth.getInstance();

        id=(EditText)findViewById(R.id.id);
        pw=(EditText)findViewById(R.id.pw);
    }

/*    private void checkUser(String id,String password){
        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           Toast.makeText(MainActivity.this,"성공",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"실패",Toast.LENGTH_LONG).show();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                        }
                    }
                });
    }*/

    public void getFirebaseDatabase() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    FirebasePost get = postSnapshot.getValue(FirebasePost.class);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase", "loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyId = FirebaseDatabase.getInstance().getReference().child("id_list").orderByChild("id");
        sortbyId.addListenerForSingleValueEvent(postListener);
        if(sortbyId.getRef().child(id.getText().toString())==null){
            Toast.makeText(this,"id를 확인해주세요",Toast.LENGTH_LONG).show();
        }
        else if((sortbyId.getRef().child(id.getText().toString())).equals(pw.getText().toString())){
            Intent intent = new Intent(getApplication(), HomeActivity.class);
            startActivity(intent);
        }

    }
    //로그인
    public void login(View view) {
        getFirebaseDatabase();
        /*Intent intent = new Intent(getApplication(), HomeActivity.class);
        startActivity(intent);*/
    }

    //회원가입
    public void join(View view) {
        Intent intent = new Intent(getApplication(), JoinActivity.class);
        startActivity(intent);
    }


}
