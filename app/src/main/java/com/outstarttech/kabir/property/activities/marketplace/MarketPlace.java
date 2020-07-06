package com.outstarttech.kabir.property.activities.marketplace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inventioncore.kabir.sopfia.R;
import com.outstarttech.kabir.property.activities.Calculations;
import com.outstarttech.kabir.property.activities.Main2Activity;

public class MarketPlace extends AppCompatActivity {


    LinearLayout gallery, backgrounds, arts, specialoffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Marketplace");
        }


        gallery = (LinearLayout) findViewById(R.id.cardGallery);
        backgrounds = (LinearLayout) findViewById(R.id.cardBackground);
        arts = (LinearLayout) findViewById(R.id.cardArts);
        specialoffers = (LinearLayout) findViewById(R.id.cardSpecialOffers);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketPlace.this, Gallery.class));
//                Toast.makeText(MarketPlace.this, "Gallery (Marketplace)", Toast.LENGTH_SHORT).show();
            }
        });


        backgrounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarketPlace.this, "Backgrounds (Marketplace)", Toast.LENGTH_SHORT).show();

            }
        });


        arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarketPlace.this, "S0PFiA Arts (Marketplace)", Toast.LENGTH_SHORT).show();

            }
        });


        specialoffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MarketPlace.this, "Special Offers (Marketplace)", Toast.LENGTH_SHORT).show();

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
