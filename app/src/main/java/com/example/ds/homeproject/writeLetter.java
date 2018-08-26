package com.example.ds.homeproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class writeLetter extends AppCompatActivity {
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mPostReference =  Database.getReference("letter_list");
    private DatabaseReference mPostR =  Database.getReference("id_list");
    private FirebaseAuth auth;
    EditText letter, name;
    String rName, sName,user;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_letter);
        setTitle("편지쓰기");
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        letter = (EditText) findViewById(R.id.letter);
        name = (EditText) findViewById(R.id.name);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser().getEmail();


        //레퍼런스 정보 가져오기
        id = user.substring(0, user.indexOf("@"));
        mPostR.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sName = dataSnapshot.child("name").getValue(String.class);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
    public void send(View view) {
        receiveLetter(letter.getText().toString());
        sendLetter(letter.getText().toString());
        finish();
    }
    private void receiveLetter(String letter) {
        rName=name.getText().toString();
        String key = mPostReference.child(rName).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+rName + "/receive" + key, letter);
        mPostReference.updateChildren(childUpdates);
    }
    private void sendLetter(String letter) {
        String key = mPostReference.child(sName).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+sName + "/send" + key, letter);
        mPostReference.updateChildren(childUpdates);
    }

}
