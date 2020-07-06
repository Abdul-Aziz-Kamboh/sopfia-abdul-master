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

public class CommonLogrithm extends AppCompatActivity {
    private float mScaleFactor = 1.0f;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_logrithm);

        tv=(TextView) findViewById(R.id.textView);
        tv.setText("The common logarithm is the logarithm to base 10.\n" +
                "The notation logx is used by physicists, engineers, and calculator keypads to denote the common logarithm. However, mathematicians generally use the same symbol to mean the natural logarithm ln, lnx.\n" +
                "Worse still, in Russian literature the notation lgx is used to denote a base-10 logarithm, which conflicts with the use of the symbol lg to indicate the logarithm to base 2.\n" +
                "To avoid all ambiguity, it is best to explicitly specify log_(10)x when the logarithm to base 10 is intended. In this work, logx=log_(10)x, lnx=log_ex is used for the natural logarithm, and lgx=log_2x is used for the logarithm to the base 2.\n" +
                "\n" +
                "The common logarithm is implemented in the Wolfram Language as Log[10, x] and Log10[x].\n" +
                "\n" +
                "Hardy and Wright (1979, p. 8) assert that the common logarithm has \"no mathematical interest.\"\n" +
                "\n" +
                "Common and natural logarithms can be expressed in terms of each other as\n" +
                "\n" +
                " lnx=(log_(10)x)/(log_(10)e) \n" +
                "\n" +
                " log_(10)x=(lnx)/(ln10). ");

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
