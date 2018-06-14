package com.example.ds.homeproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    ListView listView;
    Button write;
    LetterAdapter adapter;
    ViewGroup rootView;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference letterReference =  Database.getReference("letter_list");
    private DatabaseReference mPostR =  Database.getReference("id_list");
    private FirebaseAuth auth;
    String id, user, name,key, letter;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_chat,container,false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new LetterAdapter();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser().getEmail();

        id = user.substring(0, user.indexOf("@"));
        mPostR.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot datas : dataSnapshot.getChildren()) {} 반복문
                name = dataSnapshot.child("name").getValue(String.class);
                letterChaged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });


        listView.setAdapter(adapter);

        //편지쓰기
        write = (Button)rootView.findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),writeLetter.class);
                startActivity(intent);
            }}
        );

        return rootView;
    }

    public void letterChaged(){
        letterReference.child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                      key = uniqueKeySnapshot.getKey();
                     letter = uniqueKeySnapshot.getValue(String.class);
                     adapter.addItem(new letterItem(key, letter));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    class LetterAdapter extends BaseAdapter {

        ArrayList<letterItem> items = new ArrayList<letterItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(letterItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            letterItemView view = new letterItemView(getActivity());
            letterItem item = items.get(position);
            view.setTv(item.getTv());
            view.setLetter(item.getLetter());
            return view;
        }
    }
}
