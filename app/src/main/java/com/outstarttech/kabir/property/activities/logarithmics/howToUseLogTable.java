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

public class howToUseLogTable extends AppCompatActivity {
    private float mScaleFactor = 1.0f;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use_log_table);
        tv=(TextView) findViewById(R.id.textView);
        tv.setText("The procedure is given below to find the log value of a number using the log table. \n" +
                "First, you have to know how to use the log table. The log table is given for the reference to find the values.\n" +
                "\n" +
                "1:Choose the correct table. To find loga(n), you'll need a loga table. Most log tables are for base-10 logarithms, called \"common logs.\"[2]\n" +
                "Example: log10(31.62) requires a base-10 table.\n" +
                "\n" +
                "2:Find the correct cell. Look for the cell value at the following intersections, ignoring all decimal places:[3]\n" +
                "Row labeled with first two digits of n\n" +
                "Column header with third digit of n\n" +
                "Example: log10(31.62) → row 31, column 6 → cell value 0.4997.\n" +
                "\n" +
                "3:Use smaller chart for precise numbers. Some tables have a smaller set of columns on the right side of the chart. Use these to adjust answer if n has four or more significant digits:\n" +
                "Stay in same row\n" +
                "Find small column header with fourth digit of n\n" +
                "Add this to previous value\n" +
                "Example: log10(31.62) → row 31, small column 2 → cell value 2 → 4997 + 2 = 4999.\n" +
                "\n" +
                "4:Prefix a decimal point. The log table only tells you the portion of your answer after the decimal point. This is called the \"mantissa.\"[4]\n" +
                "Example: Solution so far is ?.4999\n" +
                "\n" +
                "5:Find the integer portion. Also called the \"characteristic\". By trial and error, find integer value of p such that {\\displaystyle a^{p}<n}a^{p}<n and {\\displaystyle a^{p+1}>n}a^{{p+1}}>n.\n" +
                "Example: {\\displaystyle 10^{1}=10<31.62}10^{1}=10<31.62 and {\\displaystyle 10^{2}=100>31.62}10^{2}=100>31.62. The \"characteristic\" is 1. The final answer is 1.4999\n" +
                "Note how easy this is for base-10 logs. Just count the digits left of the decimal and subtract one.\n" +
                "\n" +
                "6:Find the characteristic part. Since the number lies between 10 and 100, (101 and 102),\n" +
                " the characteristic part should be 1.\n" +
                "\n" +
                "7:Finally combine both the characteristic part and the mantissa part.");
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
