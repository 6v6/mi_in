package com.example.ds.homeproject;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.models.Post;
import com.google.firebase.quickstart.database.models.User;*/
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class JoinActivity extends AppCompatActivity {

    /*private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = Database.getReference();*/

    String[] items = { "엄마", "아빠", "아들", "딸",};
    Button finish;

    EditText nm, id, pw, email,familyCode;
    String role;
    boolean tableCreated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setTitle("회원가입");

        tableCreated = false;
        nm = (EditText)(findViewById(R.id.name));
        id = (EditText)(findViewById(R.id.id));
        pw = (EditText)(findViewById(R.id.pw));
        email = (EditText)(findViewById(R.id.email));
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

                finish();
            }
         }
        );
    }





}
