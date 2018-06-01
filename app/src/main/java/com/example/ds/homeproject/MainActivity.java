package com.example.ds.homeproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private static String DATABASE_NAME = "home";
    private static String TABLE_NAME = "info";
    private static int DATABASE_VERSION = 1;
    //private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


    }

    //로그인
    public void login(View view) {
       /* boolean isOpen = openDatabase();
        if (isOpen) {
            executeRawQuery();
            //executeRawQueryParam();
        }*/
        Intent intent = new Intent(getApplication(), HomeActivity.class);
        startActivity(intent);
    }

    //회원가입
    public void join(View view) {
        Intent intent = new Intent(getApplication(), JoinActivity.class);
        startActivity(intent);
    }
/*
    private boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private void executeRawQuery() {

        Cursor c1 = db.rawQuery("select count(*) as Total from " + TABLE_NAME, null);
        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam() {
        String SQL = "select _id, pw"
                + " from " + TABLE_NAME;
        //***********?????
        String[] args= {"30"};

        Cursor c1 = db.rawQuery(SQL, args);
        int recordCount = c1.getCount();


        for (int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            String name = c1.getString(0);
            int age = c1.getInt(1);
            String phone = c1.getString(2);

        }

        c1.close();
    }*/


}
