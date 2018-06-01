package com.example.ds.homeproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    Button notice;
    ImageButton imgBtn;
    ImageView imgV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home,container,false);
        notice = (Button)rootView.findViewById(R.id.notice);
        imgBtn = (ImageButton)rootView.findViewById(R.id.imageButton);
        imgV = (ImageView)rootView.findViewById(R.id.imageView);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoticeActivity.class);
                getActivity().startActivityForResult(intent,101);
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), 102);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
            notice.setText(data.getExtras().getString("notice"));
        else if(requestCode==102) {
            try {
                //이미지를 하나 골랐을때 ***resultok 대신에 -1
                if (resultCode == -1 && null != data) {
                    //data에서 절대경로로 이미지를 가져옴
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    //이미지가 크면 불러 오지 못하므로 사이즈를 줄여 준다.
                    int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                    Bitmap selected = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
                    imgV.setImageBitmap(selected);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
