package com.zundi.perawatindonesia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zundi.inc.R;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

      Thread timer = new Thread(){
             public void run(){
                 try{
                     sleep(4100);
                 }
                 catch(InterruptedException e){
                     e.printStackTrace();
                 } finally {
                     Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                     startActivity(intent);
                 }
             }

        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
