package com.outstarttech.kabir.property.activities.logarithmics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.inventioncore.kabir.sopfia.R;

public class Logarithms extends AppCompatActivity {

    //private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logarithms);
tv=(TextView) findViewById(R.id.textView);
tv.setText("Logarithms were invented independently by John Napier, a Scotsman, and by Joost Burgi, a Swiss.\nThe logarithms which they invented differed from each other and from the common and natural logarithms now in use. Napier's logarithms were published in 1614.\nBurgi's logarithms were published in 1620.\nIn mathematics, the logarithm is the inverse function to exponentiation.\n That means the logarithm of a given number x is the exponent to which another fixed number, the base b, must be raised, to produce that number x.\n" +
        " In the simplest case, the logarithm counts the number of occurrences of the same factor in repeated multiplication.\n" +
        " e.g., since 1000 = 10 × 10 × 10 = 103,\n the logarithm base 10 of 1000 is 3, or log10(1000) = 3.\n The logarithm of x to base b is denoted as logb (x), or without parentheses, logb x,\n Natural Log is :\n ln(x)=logex.\n k=ln(c) \n ek=c");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            Intent i=getIntent();
            String title=i.getStringExtra("title");
            bar.setTitle(title);
        }

        //  image = (ImageView) findViewById(R.id.image); // initiate a ImageView
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }





}
