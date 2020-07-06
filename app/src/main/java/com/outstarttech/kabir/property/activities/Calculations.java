package com.outstarttech.kabir.property.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.duy.ScientificCal;
import com.duy.calculator.symja.activities.DerivativeActivity;
import com.duy.calculator.symja.activities.ExpandAllExpressionActivity;
import com.duy.calculator.symja.activities.FactorExpressionActivity;
import com.duy.calculator.symja.activities.FactorPrimeActivity;
import com.duy.calculator.symja.activities.IntegrateActivity;
import com.duy.calculator.symja.activities.LimitActivity;
import com.duy.calculator.symja.activities.ModuleActivity;
import com.duy.calculator.symja.activities.NumberActivity;
import com.duy.calculator.symja.activities.PermutationActivity;
import com.duy.calculator.symja.activities.PiActivity;
import com.duy.calculator.symja.activities.PrimitiveActivity;
import com.duy.calculator.symja.activities.SimplifyExpressionActivity;
import com.duy.calculator.symja.activities.SolveEquationActivity;
import com.duy.calculator.symja.activities.TrigActivity;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.calculator.LogicCalculatorActivity;
import com.duy.ncalc.geom2d.GeometryDescartesActivity;
import com.duy.ncalc.graph.GraphActivity;
import com.duy.ncalc.matrix.MatrixCalculatorActivity;
import com.duy.ncalc.systemequations.SystemEquationActivity;
import com.duy.ncalc.unitconverter.UnitCategoryActivity;
import com.inventioncore.kabir.sopfia.R;
import com.outstarttech.kabir.property.activities.calculator.ScientificCalculator;
import com.outstarttech.kabir.property.activities.calculator.StandardCalculator;
import com.outstarttech.kabir.property.activities.logarithmics.Logarithmics;

import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;

public class Calculations extends AppCompatActivity {
    protected final Handler handler = new Handler();
    LinearLayout simplecal, algebracal, logarithmics, trigno, calculus, scientificcal,solveEqn,graph,mtrx,untConvrt
            ,derivtv,intgrt,permitive,limit,permutation,combination,prime,factorExpression,newton,gemerty2d
            ,catalan,primeNumber,divisors,fabonacci,piNumber,trigReduce,trigExpo,sysEqn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculations);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Calculations");
        }

        simplecal = (LinearLayout) findViewById(R.id.cardCalculator);
    scientificcal = (LinearLayout) findViewById(R.id.cardScientific);
        calculus = (LinearLayout) findViewById(R.id.cardCalculus);
        algebracal = (LinearLayout) findViewById(R.id.cardAlgebra);
        solveEqn = (LinearLayout) findViewById(R.id.solveEquation);
        permitive = (LinearLayout) findViewById(R.id.permitive);
        mtrx = (LinearLayout) findViewById(R.id.metrix);
        sysEqn = (LinearLayout) findViewById(R.id.systemEqn);
        trigExpo = (LinearLayout) findViewById(R.id.trigExpo);
        piNumber = (LinearLayout) findViewById(R.id.piNumber);
        fabonacci = (LinearLayout) findViewById(R.id.fabonacci);
        divisors = (LinearLayout) findViewById(R.id.divisors);
        primeNumber = (LinearLayout) findViewById(R.id.primeNumber);
        catalan = (LinearLayout) findViewById(R.id.catalan);
        gemerty2d = (LinearLayout) findViewById(R.id.gemtry2d);
        newton  = (LinearLayout) findViewById(R.id.newton);
        factorExpression = (LinearLayout) findViewById(R.id.factorExpression);
        prime = (LinearLayout) findViewById(R.id.prime);
        limit = (LinearLayout) findViewById(R.id.limit);
        trigReduce = (LinearLayout) findViewById(R.id.trigReduce);
        combination = (LinearLayout) findViewById(R.id.combination);
        permutation = (LinearLayout) findViewById(R.id.permutation);
        intgrt = (LinearLayout) findViewById(R.id.integrate);
        derivtv = (LinearLayout) findViewById(R.id.derivative);
        untConvrt = (LinearLayout) findViewById(R.id.unitConverter);
        logarithmics = (LinearLayout) findViewById(R.id.cardLogarithmics);
        trigno = (LinearLayout) findViewById(R.id.cardTrignomatry);
        graph = (LinearLayout) findViewById(R.id.cardGraph);


        piNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, PiActivity.class));
            }
        });

        sysEqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, SystemEquationActivity.class));
            }
        });

        fabonacci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.FIBONACCI);
                postStartActivity(intent);
            }
        });

        divisors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.DIVISORS);
                postStartActivity(intent);
            }
        });

        primeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.PRIME);
                postStartActivity(intent);
            }
        });

        catalan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent  intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.CATALAN);
                postStartActivity(intent);
            }
        });

        gemerty2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, GeometryDescartesActivity.class));
            }
        });

        newton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, ExpandAllExpressionActivity.class));
            }
        });

        factorExpression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, FactorExpressionActivity.class));
            }
        });

        prime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
               startActivity(intent);
            }
        });

        combination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(getApplicationContext(), PermutationActivity.class);

                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
                postStartActivity(intent);
            }
        });

        permutation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(getApplicationContext(), PermutationActivity.class);

                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
                postStartActivity(intent);
            }
        });

        limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, LimitActivity.class));
            }
        });

        permitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, PrimitiveActivity.class));
            }
        });

        intgrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, IntegrateActivity.class));
            }
        });

        derivtv.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                startActivity(new Intent(Calculations.this, DerivativeActivity.class));
            }
        });

        untConvrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, UnitCategoryActivity.class));
            }
        });
        mtrx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, MatrixCalculatorActivity.class));
            }
        });
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, GraphActivity.class));
            }
        });

        solveEqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, SolveEquationActivity.class));
            }
        });

        simplecal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, StandardCalculator.class));
            }
        });
        scientificcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Calculations.this, ScientificCal.class));
            }
        });

        calculus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this,SimplifyExpressionActivity.class));
            }
        });

        algebracal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, AlgebraCalculator.class));
            }
        });
        logarithmics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calculations.this, Logarithmics.class));
            }
        });
        trigno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent    intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPAND);
                postStartActivity(intent);
            }
        });
        trigReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent    intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, REDUCE);
                postStartActivity(intent);
            }
        });
        trigExpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent    intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPONENT);
                postStartActivity(intent);
            }
        });

    }
    private void postStartActivity(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
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
