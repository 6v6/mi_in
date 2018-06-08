package com.example.ds.homeproject;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class GalFragment extends Fragment {

    GridView gridView;
    Button photo;
    SingerAdapter adapter;
    Bitmap bitmap;


    private static final String TAG = "Test";
    private static final int PICK_FROM_ALBUM = 1;

    private FirebaseStorage storage;

    ImageView image;
    ViewGroup rootView;

    public GalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //권한
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        rootView = (ViewGroup)inflater.inflate(R.layout.fragment_gal,container,false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        adapter = new SingerAdapter();

        // adapter.addItem(new singerItem(bitmap));
        adapter.addItem(new singerItem(bitmap));
        gridView.setAdapter(adapter);

        photo = (Button)rootView.findViewById(R.id.photoSel);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAlbum();
            }}

        );

        return rootView;
    }

    //확대보기
    public void resizeImage(singerItem item){

        Intent intent = new Intent(getActivity(), details.class);
        intent.putExtra("image", item.getImage());
        startActivity(intent);
    }


    //앨범 호출 하기
    private void takeAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    //사진 선택 후 저장
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        image = (ImageView) rootView.findViewById(R.id.img);

        GridView gridView2;
        gridView2 = (GridView) rootView.findViewById(R.id.gridView);

        Uri UriFStorage = Uri.fromFile(new File(getPath(data.getData())));

        try {
            if (requestCode == PICK_FROM_ALBUM && resultCode == -1 && null != data) {


                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                //앨범
                Log.d(TAG, "ALBUM:" +"들어옴"+"\n");
                Log.d(TAG, "ALBUM 경로Path : " + uri.getPath()+"\n");
                Log.d(TAG, "ALBUM 경로Data : " + uri);

                //저장소
                Log.d(TAG, "저장소 경로 Path : " +UriFStorage+"\n");
                Log.d(TAG, "저장소 경로 getLastPathSegment : " + UriFStorage.getLastPathSegment()+"\n");

                //사이즈 줄이기
                int nh = (int) (bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);

                image.setImageBitmap(scaled);
                adapter.addItem(new singerItem(scaled));
                adapter.notifyDataSetChanged();


                gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        singerItem item = (singerItem) adapter.getItem(position);
                        //확대 보기
                        // resizeImage(item);
                    }
                });

            }

            //데베 저장소

            storage=FirebaseStorage.getInstance();
            //나의 데이터베이스 저장소 주소
            StorageReference storageRef = storage.getReferenceFromUrl("gs://miin-2a596.appspot.com");
            StorageReference riversRef = storageRef.child("images/"+UriFStorage.getLastPathSegment());
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

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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


    class SingerAdapter extends BaseAdapter {

        ArrayList<singerItem> items = new ArrayList<singerItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(singerItem item) {
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
            SingerItemView view = new SingerItemView(getActivity());
            singerItem item = items.get(position);
            view.setImage(item.getImage());

       /*     int numColumns = gridView.getNumColumns();
            int rowIndex = position / numColumns;
            int columnIndex = position % numColumns;*/

            return view;
        }
    }

}

