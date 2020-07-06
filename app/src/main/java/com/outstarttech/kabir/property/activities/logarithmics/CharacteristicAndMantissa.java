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

public class CharacteristicAndMantissa extends AppCompatActivity {
    private float mScaleFactor = 1.0f;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_and_mantissa);

        tv=(TextView) findViewById(R.id.textView);
        tv.setText("An important property of base-10 logarithms, which makes them so useful in calculations, is that the logarithm of numbers greater than 1 that differ by a factor of a power of 10 all have the same fractional part. \n" +
                "The fractional part is known as the mantissa.\n" +
                "Thus log tables need only show the fractional part. \n" +
                "Tables of common logarithms typically listed the mantissa, to four or five decimal places or more, of each number in a range, e.g., 1000 to 9999. Such a range would cover all possible values of the mantissa.\n" +
                "For example:\n" +
                "log10(n) − i\n" +
                "The integer part, called the characteristic, can be computed by simply counting how many places the decimal point must be moved so that it is just to the right of the first significant digit. \n" +
                "For example:\n" +
                "i = floor(log10(n))");

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
