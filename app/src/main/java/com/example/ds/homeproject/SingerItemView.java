package com.example.ds.homeproject;

/**
 * Created by DS on 2018-05-25.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SingerItemView extends LinearLayout {
    TextView textView;
    ImageView imageView;


    public SingerItemView(Context context) {
        super(context);
        init(context);
    }

    public SingerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_singer_item, this, true);
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.textView);

    }


    public void setTitle(String title) {
        textView.setText(title);
    }


    public void setImage(Bitmap bitmap) { imageView.setImageBitmap(bitmap);}
}
