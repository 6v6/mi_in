package com.example.ds.homeproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalFragment extends Fragment {
    CalendarView cal;
    EditText edit;
    Button save;
    TextView textView;
    String date ;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference mPostReference =  Database.getReference("schedule_list");
    String schedule;
    Iterable it;
    String textSchedule;


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
        SimpleDateFormat today = new SimpleDateFormat("yyyy_MM_d");
        date = today.format(new Date());
        scheduleChange();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule =edit.getText().toString();
                postFirebaseDatabase(true);
                scheduleChange();
                edit.setText("");
               //textView.setText(textSchedule);
            }
        });
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date =year+"_"+(month+1)+"_"+dayOfMonth; // date에 선택된 날짜가 저장됨
                scheduleChange();
               // textView.setText(textSchedule);
            }
        });
        return rootView;
    }
    public void postFirebaseDatabase(boolean add){
        String key = mPostReference.child(date).push().getKey();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(  date + "/" + key, schedule);
        mPostReference.updateChildren(childUpdates);
    }

    public void scheduleChange(){
        mPostReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textSchedule="";
                for(DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()){
                    textSchedule+=uniqueKeySnapshot.getValue()+"\n";
                }
                textView.setText(textSchedule);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


}
