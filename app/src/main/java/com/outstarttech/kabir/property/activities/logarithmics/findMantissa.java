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

public class findMantissa extends AppCompatActivity {
    private float mScaleFactor = 1.0f;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mantissa);
        tv=(TextView) findViewById(R.id.textView);
        tv.setText("In the Log Table, look for the row 56.\n" +
                "Then move to the right in the same row until you reach to column 7\n" +
                "( first digit after decimal point).\n" +
                " It should have the value 7536 which should be written as 0.7536 \n" +
                "(as it is the decimal part). \n" +
                "Now, in the same row, move to mean difference table and note down the value under the column 8 \n" +
                "( 56.7834) which should be 6.\n" +
                " Now add the mean difference to the value we obtained in column 7 \n" +
                "( i.e. 0.7536 + 0.0006 = 0.7542).\n" +
                "\n" +
                "So we obtained the mantissa part i.e. 0.7542.\n" +
                "The decimal part of the logarithm number is said to be the mantissa part and it should always be a positive value.\n If the mantissa part is in a negative value,\n then convert into the positive value.\n\n" +
                "Now, consider a number (say 6.72) between 1 and 10. Clearly,\n" +
                "\n" +
                "1 < 6.72 < 10\n" +
                "\n" +
                "Therefore, log 1 < log 6.72 < log 10\n" +
                "\n" +
                "or, 0 < log 6.72 < 1 [ Since log 1 = 0 and log 10 = 1");

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
