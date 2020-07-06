package com.outstarttech.kabir.property.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.inventioncore.kabir.sopfia.R;

public class SplashScreen extends AppCompatActivity {

//    private final int SPLASH_DISPLAY_LENGTH = 3500;

    private Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        proceed = (Button) findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, LogInActivity.class));
            }
        });



//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(SplashScreen.this, LogInActivity.class);
//                SplashScreen.this.startActivity(mainIntent);
//                SplashScreen.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);



    }
}
