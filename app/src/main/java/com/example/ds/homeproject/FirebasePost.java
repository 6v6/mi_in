package com.example.ds.homeproject;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {

    //계정
    public String name;
    public String pw;
    public String email;
    public String code;
    public String role;

    //캘린더
    public String date,schedule;

    //이미지
    public String imageUrl;
    public String userEmail;
    public String userName;


    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String name, String pw, String email, String code, String role) {
        this.name = name;
        this.pw = pw;
        this.code = code;
        this.email = email;
        this.role = role;
    }

    public FirebasePost(String date, String schedule){
        this.date = date;
        this.schedule = schedule;
    }

    public FirebasePost(String imageUrl){
        this.imageUrl = imageUrl;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        if(name!=null) {
            result.put("name", name);
            result.put("pw", pw);
            result.put("email", email);
            result.put("code", code);
            result.put("role", role);
        }
        else if(date!=null){
            result.put("date", date);
            result.put("schedule", schedule);
        }

        else if(imageUrl!=null){
            result.put("imageUrl", imageUrl);
        }
        return result;
    }

}