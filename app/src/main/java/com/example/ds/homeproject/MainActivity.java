package com.example.ds.homeproject;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


    }

    //로그인
    public void login(View view) {
        Intent intent = new Intent(getApplication(), HomeActivity.class);
        startActivity(intent);
    }

    //회원가입
    public void join(View view) {
        Intent intent = new Intent(getApplication(), JoinActivity.class);
        startActivity(intent);
    }

}
