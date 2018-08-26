package com.example.ds.homeproject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class HomeActivity extends FragmentActivity  {

    private CalFragment calFragment;
    private HomeFragment homeFragment;
    private GalFragment galFragment;
    private ChatFragment chatFragment;
    private AlertDialog dialog;
    boolean isPageOpen=false;
    Animation translationLeftAnim;
    Animation translationRightAnim;

    private FirebaseAuth auth;
    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = Database.getReference("id_list");


    private ImageView imageView;
   private LinearLayout page;

    private TextView textEmail;
    private TextView textName;
    private TextView textfamCode;
    private TextView textRole;
    private BitmapDrawable mom,dad,girl,boy;

    String email;
    String name;
    String role;
    String familyCode;
    String user;

    TabListener tl = new TabListener();
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        calFragment = new CalFragment();
        homeFragment = new HomeFragment();
        galFragment = new GalFragment();
        chatFragment = new ChatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,homeFragment).commit();

        page=(LinearLayout) findViewById(R.id.page);

        imageView=(ImageView)findViewById(R.id.pro);
        translationLeftAnim= AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translationRightAnim= AnimationUtils.loadAnimation(this, R.anim.translate_right);
        SPAListener al=new SPAListener();
        translationLeftAnim.setAnimationListener(al);
        translationRightAnim.setAnimationListener(al);

        mom=(BitmapDrawable)getResources().getDrawable(R.drawable.mom);
        dad=(BitmapDrawable)getResources().getDrawable(R.drawable.dad);
        girl=(BitmapDrawable)getResources().getDrawable(R.drawable.girl);
        boy=(BitmapDrawable)getResources().getDrawable(R.drawable.boy);


        //bottom바 액션 설정
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tl);

        //로그인한 이메일 주소 가져오기
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser().getEmail();



        //레퍼런스 정보 가져오기
        String id = user.substring(0,user.indexOf("@"));
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            email=dataSnapshot.child("email").getValue(String.class);
            name=dataSnapshot.child("name").getValue(String.class);
            familyCode=dataSnapshot.child("code").getValue(String.class);
            role=dataSnapshot.child("role").getValue(String.class);

            textName=(TextView)findViewById(R.id.name);
            textEmail=(TextView)findViewById(R.id.email);
            textfamCode=(TextView)findViewById(R.id.famcode);
            textRole=(TextView)findViewById(R.id.role);


            textEmail.setText(email);
            textName.setText(name);
            textfamCode.setText(familyCode);
            textRole.setText(role);
            if(role.equals("엄마"))
                imageView.setImageDrawable(mom);
            else if(role.equals("아빠"))
                imageView.setImageDrawable(dad);
            else if(role.equals("딸"))
                imageView.setImageDrawable(girl);
            else if(role.equals("아들"))
                imageView.setImageDrawable(boy);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

}
    public void onLogOut(View view) {
        auth.signOut();
        finish();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    public void version(View view) {
        Toast.makeText(this,"mi_in_5346",Toast.LENGTH_LONG).show();
    }

  private class SPAListener implements Animation.AnimationListener{

      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
          if(isPageOpen){
              page.setVisibility(View.INVISIBLE);
              isPageOpen=false;
          }
          else {
              isPageOpen=true;

          }
      }
      @Override
      public void onAnimationRepeat(Animation animation) {
      }
  }

    private class TabListener implements OnTabSelectListener{
        @Override
        public void onTabSelected(@IdRes int tabId) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (tabId == R.id.gal) {
                transaction.replace(R.id.contentContainer, galFragment).commit();
                if(isPageOpen){
                    page.startAnimation(translationRightAnim);
                    page.setVisibility(View.INVISIBLE);
                }
            }
            if (tabId == R.id.cal) {
                transaction.replace(R.id.contentContainer, calFragment).commit();
                if(isPageOpen){
                    page.startAnimation(translationRightAnim);
                    page.setVisibility(View.INVISIBLE);
                }
            }
            if (tabId == R.id.menu) {
                if(isPageOpen){
                    page.startAnimation(translationRightAnim);
                    page.setVisibility(View.INVISIBLE);
                }
                else {
                    page.setVisibility(View.VISIBLE);
                    page.startAnimation(translationLeftAnim);
                }
            }
            if (tabId == R.id.chat) {
                transaction.replace(R.id.contentContainer, chatFragment).commit();
                if(isPageOpen){
                    page.startAnimation(translationRightAnim);
                    page.setVisibility(View.INVISIBLE);
                }
            }
            if (tabId == R.id.home) {
                transaction.replace(R.id.contentContainer, homeFragment).commit();
                if(isPageOpen){
                    page.startAnimation(translationRightAnim);
                    page.setVisibility(View.INVISIBLE);
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102 || requestCode == 101)
            homeFragment.onActivityResult(requestCode, resultCode, data);
        else
            galFragment.onActivityResult(requestCode, resultCode, data);
    }
}


