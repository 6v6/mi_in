package com.example.ds.homeproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CalFragment extends Fragment {
    int numButton=0;
    CalendarView cal;
    EditText edit;
    Button save;
    TextView textView;
    String date ;
    LinearLayout dynamicArea;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mPostReference =  Database.getReference("schedule_list");
    private final int DYNAMIC_VIEW_ID =0x8000;
    String schedule;
    int count=0;
    Iterable it;
    String textSchedule;

    public CalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_cal,container,false);
        // Inflate the layout for this fragment
        cal =(CalendarView)rootView.findViewById(R.id.calendarView);
        edit =(EditText)rootView.findViewById(R.id.edit);
        save =(Button)rootView.findViewById(R.id.save);
        textView =(TextView)rootView.findViewById(R.id.result);
        //date 오늘 날짜로 초기화
      //  date = year

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule =edit.getText().toString();
                postFirebaseDatabase(true);
                numButton++;
                TextView textview3= new TextView(getActivity());
            }
        });
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date =year+"_"+(month+1)+"_"+dayOfMonth; // date에 선택된 날짜가 저장됨
                mPostReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        textSchedule="";
                        for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                                //loop 2 to go through all the child nodes of books node
                                textSchedule+=uniqueKeySnapshot.getValue()+"\n";
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textView.setText(textSchedule);
            }
        });
        return rootView;
    }
    public void postFirebaseDatabase(boolean add){
        if(mPostReference.child(date)==null) {
            Map<String, Object> childUpdates = new HashMap<>();
            Map<String, Object> postValues = null;
            if (add) {
                FirebasePost post = new FirebasePost(date, schedule);
                postValues = post.toMap();
            }
            childUpdates.put(date, postValues);
            mPostReference.updateChildren(childUpdates);
        }
        else{
            mPostReference.child(date).child("schedule"+count).setValue(schedule);
            count++;
        }
    }



}
