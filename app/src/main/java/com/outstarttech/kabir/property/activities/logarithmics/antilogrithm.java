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

public class antilogrithm extends AppCompatActivity {
    private float mScaleFactor = 1.0f;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antilogrithm);
        tv=(TextView) findViewById(R.id.anti);
        tv.setText("Antilog is generally written in front of a number and it means Look up in the table the value of the base when raised to the power represented by the number.\n" +
                "\n" +
                "The antilogarithm is the logarithmic inverse of the logarithm, meaning that the \n" +
                "antilogx ( logx(y) ) = y.\n" +
                "If x = logy, then y is the antilogarithm of x.\n" +
                "\n" +
                "The number for which a given logarithm stands.\n" +
                "for example:\n" +
                "if log x equals y, then x is the antilogarithm of y");
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
