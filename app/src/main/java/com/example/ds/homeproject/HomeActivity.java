package com.example.ds.homeproject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;

public class HomeActivity extends FragmentActivity {

    private CalFragment calFragment;
    private HomeFragment homeFragment;
    private GalFragment galFragment;
    private ChatFragment chatFragment;

    boolean isPageOpen=false;
    Animation translationLeftAnim;
    Animation translationRightAnim;
    private AlertDialog dialog;
    ImageView imageView;//프로필
    LinearLayout page;
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

        page=(LinearLayout)findViewById(R.id.page);
        imageView=(ImageView)findViewById(R.id.pro);
        translationLeftAnim= AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translationRightAnim= AnimationUtils.loadAnimation(this, R.anim.translate_right);
        SPAListener al=new SPAListener();
        translationLeftAnim.setAnimationListener(al);
        translationRightAnim.setAnimationListener(al);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(tl);
    }

    public void onButtonImageClick(View view) {
        dialog = createDialog();
        dialog.show();
    }

    private AlertDialog createDialog() {
        final View innerView = getLayoutInflater().inflate(R.layout.menu_dialog, null);

        ImageView img=(ImageView)innerView.findViewById(R.id.mom);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.mom);
                imageView.setImageDrawable(bitmap);
                setDismiss(dialog);
            }
        });

        ImageView img2=(ImageView)innerView.findViewById(R.id.dad);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmap2=(BitmapDrawable)getResources().getDrawable(R.drawable.dad);
                imageView.setImageDrawable(bitmap2);
                setDismiss(dialog);
            }
        });
        ImageView img3=(ImageView)innerView.findViewById(R.id.girl);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmap3=(BitmapDrawable)getResources().getDrawable(R.drawable.girl);
                imageView.setImageDrawable(bitmap3);
                setDismiss(dialog);
            }
        });
        ImageView img4=(ImageView)innerView.findViewById(R.id.boy);
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmap4=(BitmapDrawable)getResources().getDrawable(R.drawable.boy);
                imageView.setImageDrawable(bitmap4);
                setDismiss(dialog);
            }
        });

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("프로필 설정");

        ab.setView(innerView);

        return ab.create();
    }

    private void setDismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
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
        if(requestCode==102 || resultCode == 100)
            homeFragment.onActivityResult(requestCode, resultCode, data);
        else
            galFragment.onActivityResult(requestCode, resultCode, data);
    }
}


