package com.outstarttech.kabir.property.activities.logarithmics;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.inventioncore.kabir.sopfia.R;

public class logCalculator extends AppCompatActivity {
    Button cl,log,anti;
    EditText nu,ba;
    TextView re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_calculator);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            bar.setTitle("Logarithm Calculator");
        }
        cl=(Button) findViewById(R.id.clear);
        log=(Button) findViewById(R.id.log);
        anti=(Button) findViewById(R.id.antilog);
        nu=(EditText) findViewById(R.id.num);
        ba=(EditText) findViewById(R.id.base);
        re=(TextView) findViewById(R.id.res);


        String number=nu.getText().toString();
        String base=nu.getText().toString();

    }

    public void clear(View view) {
        nu.setText("");
        ba.setText("");
        re.setText("");
    }

    public void log(View view) {
        re.setVisibility(View.VISIBLE);

        String number=nu.getText().toString();
        String base=nu.getText().toString();
        double b=Double.parseDouble(base);
        double n=Double.parseDouble(number);
        double result=Math.log10(n)/Math.log10(b);
        String finalresult=new Double(result).toString();
        re.setText(finalresult);
        Toast.makeText(logCalculator.this, "LOG: " + result, Toast.LENGTH_SHORT).show();
    }


    public void antilog(View view) {
        re.setVisibility(View.VISIBLE);
        String number=nu.getText().toString();
        String base=nu.getText().toString();
        double b=Double.parseDouble(base);
        double n=Double.parseDouble(number);
        double as= Math.pow(b,n);

      String finalres=new Double(as).toString();
          re.setText(finalres);
    }

}
