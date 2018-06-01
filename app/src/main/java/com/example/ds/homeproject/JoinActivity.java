package com.example.ds.homeproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class JoinActivity extends AppCompatActivity {
    String[] items = { "엄마", "아빠", "아들", "딸",};
    Button finish;
    SQLiteDatabase db;
    EditText nm, id, pw, email,familyCode;
    String role;
    boolean databaseCreated,tableCreated;
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
                createDatabase("home");
                finish();
            }
         }
        );
    }

    private void createDatabase(String name){
        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE,
                    null);
            databaseCreated = true;
            createTable("info");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }


    private void createTable(String name){
        db.execSQL("create table if not exists "+name+"("
                +"name text,"
                +"_id text PRIMARY KEY autoincrement,"
               +"pw text,"+"email text,"+"family text,"+"role text);");
        tableCreated = true;
        db.execSQL("insert into info(name,id,pw,email,family,role values("
                +nm.getText().toString()+","+id.getText().toString()+","+pw.getText().toString()+","
                +email.getText().toString()+","+familyCode.getText().toString()+role+");");

    }


}
