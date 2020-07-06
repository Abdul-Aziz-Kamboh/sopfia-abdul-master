package com.outstarttech.kabir.property.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.inventioncore.kabir.sopfia.R;

public class CreditsDetail extends AppCompatActivity {

    Button earnCredits, sendCredits;
    TextView appCredit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appCredit = (TextView) findViewById(R.id.appCreditUpdated);

        String appCreditUpdated = getIntent().getStringExtra("APP_CREDIT");

        appCredit.setText(appCreditUpdated);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Credits Details");
        }


        sendCredits = (Button) findViewById(R.id.sendCreditsButton);
        earnCredits = (Button) findViewById(R.id.earnCreditsButton);

        sendCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CreditsDetail.this, SendCredits.class));
//                Toast.makeText(CreditsDetail.this, "Send S0PFiA Credits", Toast.LENGTH_SHORT).show();
            }
        });

        earnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditsDetail.this, EarnPoints.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
