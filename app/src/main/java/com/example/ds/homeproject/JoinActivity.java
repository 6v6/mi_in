package com.example.ds.homeproject;



import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

import java.util.HashMap;
import java.util.Map;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class JoinActivity extends AppCompatActivity {

    public static final String TAG ="JoinActivity";
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mPostReference = Database.getReference();
    private FirebaseAuth mAuth;

    String[] items = { "엄마", "아빠", "아들", "딸",};
    Button finish;

    EditText name, id, edPw, edEmail,familyCode;
    String role;
    boolean tableCreated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setTitle("회원가입");

        tableCreated = false;
        name = (EditText)(findViewById(R.id.name));
        id = (EditText)(findViewById(R.id.id));
        edPw = (EditText)(findViewById(R.id.pw));
        edEmail = (EditText)(findViewById(R.id.email));
        familyCode = (EditText)(findViewById(R.id.familyCode));

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,items);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                role = items[0];
            }});

        finish = (Button)(findViewById(R.id.joinBtn));
        finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String createID = edEmail.getText().toString().trim();
                String createPW = edPw.getText().toString().trim();
                createAccount(createID,createPW);
                postFirebaseDatabase(true);
                finish();
            }
         }
        );
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            FirebasePost post = new FirebasePost(name.getText().toString(),id.getText().toString(), edPw.getText().toString(),
                    edEmail.getText().toString(),familyCode.getText().toString(),role);
            postValues = post.toMap();
        }
        childUpdates.put("/id_list/" + id.getText().toString(), postValues);
        mPostReference.updateChildren(childUpdates);
    }

    private void createAccount(String email, String passwd) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "Create Account:" + task.isSuccessful());
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "생성 성공", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Current User:" + mAuth.getCurrentUser().getEmail());
                                } else {
                                    Toast.makeText(getApplicationContext(), "생성 실패", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }
}
