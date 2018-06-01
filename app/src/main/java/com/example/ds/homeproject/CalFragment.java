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

public class CalFragment extends Fragment {
    int numButton=0;
    CalendarView cal;
    EditText edit;
    Button save;
    TextView textView2;
    String date ;
    LinearLayout dynamicArea;

    private final int DYNAMIC_VIEW_ID =0x8000;

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
        textView2 =(TextView)rootView.findViewById(R.id.result);
        dynamicArea =(LinearLayout)rootView.findViewById(R.id.dynamicArea);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =edit.getText().toString();
                numButton++;
                TextView textview3= new TextView(getActivity());
                textview3.setId(DYNAMIC_VIEW_ID+numButton);
                textview3.setText(date+" "+text+"예약됨");
                dynamicArea.addView(textview3,new LinearLayout.LayoutParams(WindowManager.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                // textView2.setText(date+" "+text +"예약됨");
            }
        });
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date =year+"년"+(month+1)+"월"+dayOfMonth+"일"; // date에 선택된 날짜가 저장됨
            }
        });
        return rootView;
    }





}
