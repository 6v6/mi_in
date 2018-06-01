package com.example.ds.homeproject;
import android.graphics.Bitmap;

public class singerItem {
    private Bitmap image;
    private String title;

    public singerItem(String title, Bitmap image) {
        super();
        this.title = title;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
