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

public class EthmnyDetail extends AppCompatActivity {

    Button sendEthmny, earnEthmny;
    TextView tokenBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethmny_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tokenBalance = (TextView) findViewById(R.id.tokenUpdated);

        String tokenCreditUpdated = getIntent().getStringExtra("TOKEN_CREDIT");

        tokenBalance.setText(tokenCreditUpdated);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("ETHMNY Details");
        }

        sendEthmny = (Button) findViewById(R.id.sendEthmnyButton);
        earnEthmny = (Button) findViewById(R.id.earnEthmnyButton);

        sendEthmny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EthmnyDetail.this, SendTokens.class));
            }
        });

        earnEthmny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EthmnyDetail.this, "Earn ETHMNY Tokens", Toast.LENGTH_SHORT).show();
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
