package com.example.ds.homeproject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    //사진
    private static final String TAG = "Test";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    ImageView image;
   private AlertDialog dialog;
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
        photo = (Button)rootView.findViewById(R.id.photoSel);
        adapter = new SingerAdapter();

        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.singer);
        adapter.addItem(new singerItem(bitmap));
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                singerItem item = (singerItem) adapter.getItem(position);
                //확대 보기
                resizeImage(item);

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = createDialog();
                dialog.show();
            }}

        );

        return rootView;
    }

    public void resizeImage(singerItem item){

        Intent intent = new Intent(getActivity(), details.class);
        intent.putExtra("image", item.getImage());
        startActivity(intent);
    }

    private AlertDialog createDialog() {
        final View innerView = getActivity().getLayoutInflater().inflate(R.layout.activity_gal_dialog, null);
        Button camera = (Button) innerView.findViewById(R.id.camera);
        Button gellary = (Button) innerView.findViewById(R.id.gallery);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
                setDismiss(dialog);
            }
        });
        gellary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAlbum();
                setDismiss(dialog);
            }
        });
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("이미지 가져오기");
        ab.setView(innerView);

        return ab.create();



    }

    // 다이얼로그 종료
    private void setDismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    // 카메라 호출 하기
    private void takePicture() {
      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_FROM_CAMERA);*/


    }

    //앨범 호출 하기
    private void takeAlbum() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_ALBUM);
      /*  Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);*/

    }

    //사진 선택 후 저장

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        image = (ImageView) rootView.findViewById(R.id.img);
        GridView gridView2;
        gridView2 = (GridView) rootView.findViewById(R.id.gridView);



        try {

            if (requestCode == PICK_FROM_ALBUM && resultCode == -1 && null != data) {

                //data에서 절대경로로 이미지를 가져옴

                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                //이미지가 크면 불러 오지 못하므로 사이즈를 줄여 준다.
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



                //데베 저장소
                storage=FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference("gs://miin-2a596.appspot.com");

                Uri file=Uri.fromFile(new File(getPath(data.getData())));
                Log.d(TAG, "ALBUM:" + data.getData().getPath());
                Log.d(TAG, "ALBUM:" + data.getData());
                StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);


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



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //getPath
    public String getPath(Uri uri){
        String [] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(getContext(),uri,proj,null,null,null);

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


