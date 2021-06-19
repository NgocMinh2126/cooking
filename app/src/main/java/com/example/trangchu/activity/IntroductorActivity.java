package com.example.trangchu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.trangchu.R;

public class IntroductorActivity extends AppCompatActivity {
    TextView tv_appname;
    ImageView img_logo;
    LottieAnimationView intro_ani;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        tv_appname=findViewById(R.id.appname_intro);
        img_logo=findViewById(R.id.logo_intro);
        intro_ani=findViewById(R.id.intro_hambuger);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        img_logo.animate().translationY(height).setDuration(1000).setStartDelay(4000);
        tv_appname.animate().translationY(height).setDuration(1000).setStartDelay(4000);
        intro_ani.animate().translationY(height).setDuration(1000).setStartDelay(4000);
        Thread bamgio=new Thread(){
            public void run()
            {
                try {
                    sleep(5000);
                } catch (Exception e) {

                }
                finally
                {
                    Intent activitymoi=new Intent(IntroductorActivity.this,MainActivity.class);
                    startActivity(activitymoi);
                }
            }
        };
        bamgio.start();
    }
}
