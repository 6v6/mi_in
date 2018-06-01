package com.example.ds.homeproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NoticeActivity extends AppCompatActivity {
    EditText notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        notice = (EditText)findViewById(R.id.notice);
    }

    public void setFinish(View view) {
        Intent intent = new Intent();
        intent.putExtra("notice",notice.getText().toString());
        setResult(100,intent);

        finish();

    }
}
