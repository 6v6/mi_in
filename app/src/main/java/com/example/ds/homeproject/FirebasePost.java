package com.example.ds.homeproject;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class FirebasePost {
    public String id;
    public String name;
    public String pw;
    public String email;
    public String code;
    public String role;

    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public FirebasePost(String name, String id, String pw, String email, String code, String role) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.code = code;
        this.email = email;
        this.role = role;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("pw", pw);
        result.put("email", email);
        result.put("code", code);
        result.put("role", role);
        return result;
    }
}