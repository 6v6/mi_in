package com.example.ds.homeproject;

import android.Manifest;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;



public class HomeFragment extends Fragment {



    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private  FirebaseDatabase db;
    private DatabaseReference myImage = FirebaseDatabase.getInstance().getReference("img_list");

    String name;
    public static final String TAG ="HomeFragment";


    public HomeFragment() {
        // Required empty public constructor
    }
    Button notice;
    Button imgBtn;
    ImageView imgV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home,container,false);
        notice = (Button)rootView.findViewById(R.id.notice);
        imgBtn = (Button)rootView.findViewById(R.id.imageButton);
        imgV = (ImageView)rootView.findViewById(R.id.imageView);


        myImage.child("home").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //for (DataSnapshot datas : dataSnapshot.getChildren()) {} 반복문
                String getUri=dataSnapshot.getValue().toString();
                String finalUri=getUri.substring(10,getUri.length());
                Log.d(TAG, "homeUri:" + finalUri+"\n");

                Glide.with(getActivity())
                        .load(finalUri)
                        .into(imgV);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

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
                getActivity(). startActivityForResult(intent,102);
            }
        });

        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri UriFStorage = Uri.fromFile(new File(getPath(data.getData())));

        if(requestCode==101)
            notice.setText(data.getExtras().getString("notice"));

        else if(requestCode==102) {
            try {
                if (resultCode == -1) {

                    //이미지 가져오기
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    //사이즈 줄이기
                    int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                    Bitmap selected = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                    imgV.setImageBitmap(selected);
                }
                //데베 저장소
                storage=FirebaseStorage.getInstance();

                //나의 데이터베이스 저장소 주소
                StorageReference storageRef = storage.getReferenceFromUrl("gs://miin-2a596.appspot.com");
                StorageReference riversRef = storageRef.child("HomeImages/"+UriFStorage.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(UriFStorage);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }

                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrI=taskSnapshot.getDownloadUrl();
                        FirebasePost firebasePost=new FirebasePost();
                        firebasePost.imageUrl=downloadUrI.toString();
                        myImage.child("home").setValue(firebasePost);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //getPath
    public String getPath(Uri uri){
        String [] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground ();
        int index = cursor.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }
}