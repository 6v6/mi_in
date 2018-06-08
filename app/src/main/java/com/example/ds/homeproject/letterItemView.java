package com.example.ds.homeproject;

/**
 * Created by DS on 2018-05-25.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class letterItemView extends LinearLayout {

    TextView tv;
    TextView letter;


    public letterItemView(Context context) {
        super(context);
        init(context);
    }

    public letterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.letter_item, this, true);
        tv = (TextView) findViewById(R.id.tv);
        letter = (TextView) findViewById(R.id.letter);


    }

    public void setTv(String s) { tv.setText(s);}
    public void setLetter(String s) { letter.setText(s);}
}
