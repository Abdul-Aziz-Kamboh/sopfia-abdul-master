package com.outstarttech.kabir.property.activities.calculator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.inventioncore.kabir.sopfia.R;
import com.outstarttech.kabir.property.activities.calculator.DBHelper;

public class StandardCalculator extends AppCompatActivity {


    private TextView e1,e2;
    private int count=0;
    private String expression="";
    private String text="";
    private Double result=0.0;
    private DBHelper dbHelper;
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,dot,clear,backSpace,plus,minus,divide,multiply,equal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_calculator);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Standard Calculator");
        }

        e1=(TextView) findViewById(R.id.result);
        e2=(TextView) findViewById(R.id.formula);

        dbHelper=new DBHelper(this);

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        dot = (Button) findViewById(R.id.btn_decimal);
        clear = (Button) findViewById(R.id.btn_clear);
//        backSpace = (Button) findViewById(R.id.backSpace);
        plus = (Button) findViewById(R.id.btn_plus);
        minus = (Button) findViewById(R.id.btn_minus);
        divide = (Button) findViewById(R.id.btn_divide);
//        openBraket = (Button) findViewById(R.id.openBracket);
//        closeBraket= (Button) findViewById(R.id.closeBracket);
        multiply = (Button) findViewById(R.id.btn_multiply);
        equal = (Button) findViewById(R.id.btn_equals);
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"0");
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"1");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"2");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"3");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"4");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"5");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"6");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"7");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"8");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+"9");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText(e2.getText()+".");
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e2.setText("");
                e1.setText("");
            }
        });
//        backSpace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text=e2.getText().toString();
//                if(text.length()>0)
//                {
//                    if(text.endsWith("."))
//                    {
//                        count=0;
//                    }
//                    String newText=text.substring(0,text.length()-1);
//                    //to delete the data contained in the brackets at once
//                    if(text.endsWith(")"))
//                    {
//                        char []a=text.toCharArray();
//                        int pos=a.length-2;
//                        int counter=1;
//                        //to find the opening bracket position
//                        for(int i=a.length-2;i>=0;i--)
//                        {
//                            if(a[i]==')')
//                            {
//                                counter++;
//                            }
//                            else if(a[i]=='(')
//                            {
//                                counter--;
//                            }
//                            //if decimal is deleted b/w brackets then count should be zero
//                            else if(a[i]=='.')
//                            {
//                                count=0;
//                            }
//                            //if opening bracket pair for the last bracket is found
//                            if(counter==0)
//                            {
//                                pos=i;
//                                break;
//                            }
//                        }
//                        newText=text.substring(0,pos);
//                    }
//                    //if e2 edit text contains only - sign or sqrt at last then clear the edit text e2
//                    if(newText.equals("-")||newText.endsWith("sqrt"))
//                    {
//                        newText="";
//                    }
//                    //if pow sign is left at the last
//                    else if(newText.endsWith("^"))
//                        newText=newText.substring(0,newText.length()-1);
//
//                    e2.setText(newText);
//                }
//            }
//        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.length() == 0){
                    e2.setText(e2.getText()+"+");
                }
                else{
                    e2.setText(e1.getText()+"+");
                    e1.setText("");
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.length() == 0){
                    e2.setText(e2.getText()+"-");
                }
                else{
                    e2.setText(e1.getText()+"-");
                    e1.setText("");
                }
            }
        });
        divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.length() == 0){
                    e2.setText(e2.getText()+"/");
                }
                else{
                    e2.setText(e1.getText()+"/");
                    e1.setText("");
                }
            }
        });
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.length() == 0){
                    e2.setText(e2.getText()+"*");
                }
                else{
                    e2.setText(e1.getText()+"*");
                    e1.setText("");
                }
            }
        });
//        openBraket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(e1.length() == 0){
//                    e2.setText(e2.getText()+"(");
//                }
//                else{
//                    e2.setText(e1.getText()+"(");
//                    e1.setText("");
//                }
//            }
//        });
//        closeBraket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(e1.length() == 0){
//                    e2.setText(e2.getText()+")");
//                }
//                else{
//                    e2.setText(e1.getText()+")");
//                    e1.setText("");
//                }
//            }
//        });
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e2.length()!=0)
                {
                    text=e2.getText().toString();
                    expression=e1.getText().toString()+text;
                }
                e1.setText("");
                if(expression.length()==0)
                    expression="0.0";
                DoubleEvaluator evaluator = new DoubleEvaluator();
                try
                {
                    //evaluate the expression
                    result=new ExtendedDoubleEvaluator().evaluate(expression);
                    //insert expression and result in sqlite database if expression is valid and not 0.0
                    if(!expression.equals("0.0"))
                        dbHelper.insert("STANDARD",expression+" = "+result);
                    e1.setText(result+"");
                }
                catch (Exception e)
                {
                    e2.setText("Invalid Expression");
                    e1.setText("");
                    expression="";
                    e.printStackTrace();
                }
            }
        });
    }

    private void operationClicked(String op)
    {
        if(e2.length()!=0)
        {
            String text=e2.getText().toString();
            e1.setText(e1.getText() + text+op);
            e2.setText("");
            count=0;
        }
        else
        {
            String text=e1.getText().toString();
            if(text.length()>0)
            {
                String newText=text.substring(0,text.length()-1)+op;
                e1.setText(newText);
            }
        }
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
