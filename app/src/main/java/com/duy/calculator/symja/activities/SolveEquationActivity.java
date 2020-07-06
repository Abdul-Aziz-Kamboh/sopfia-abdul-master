/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.symja.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.inventioncore.kabir.sopfia.R;
import com.duy.calculator.activities.base.BaseEvaluatorActivity;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.symja.models.SolveItem;
import com.duy.calculator.symja.tokenizer.ExpressionTokenizer;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.utils.DLog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;
import java.util.Objects;

import static com.inventioncore.kabir.sopfia.R.string.solve;



public class SolveEquationActivity extends BaseEvaluatorActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {
    private static final String STARTED = SolveEquationActivity.class.getName() + "started";

    protected SharedPreferences preferences;
    private boolean isDataNull = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.solve_equation);
        mHint1.setHint(getString(R.string.input_equation));
        mBtnEvaluate.setText(solve);
        getIntentData();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


       getSupportActionBar().setLogo(R.drawable.ic_action_navigation_arrow_back);
            getSupportActionBar().setTitle("Solve Equation");
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isStarted = preferences.getBoolean(STARTED, false);
        if ((!isStarted || DLog.UI_TESTING_MODE) && isDataNull) {
            mInputFormula.setText("2x^2 + 3x + 1");
        }
    }

    @Override
    public void clickHelp() {
        final SharedPreferences.Editor editor = preferences.edit();
        TapTarget target0 = TapTarget.forView(mInputFormula,
                getString(R.string.input_equation),
                getString(R.string.input_equation_here))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target = TapTarget.forView(mBtnEvaluate,
                getString(R.string.solve_equation),
                getString(R.string.push_solve_button))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTargetSequence sequence = new TapTargetSequence(SolveEquationActivity.this);
        sequence.targets(target0, target);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                clickEvaluate();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                clickEvaluate();
            }
        });
         sequence.start();
    }

    /**
     * get data from activity start it
     */
    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(BasicCalculatorActivity.DATA);
        if (bundle != null) {
            String data = bundle.getString(BasicCalculatorActivity.DATA);
            if (data != null) {
                mInputFormula.setText(data);
                data = new ExpressionTokenizer().getNormalExpression(data);
                isDataNull = false;
                if (!data.isEmpty()) {
                    clickEvaluate();
                } else {
                    //
                }
            }
        }
    }

    @Override
    protected String getExpression() {
        String expr = mInputFormula.getCleanText();
        SolveItem solveItem = new SolveItem(expr);
        return solveItem.getInput();
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(getApplicationContext());
                String fraction = MathEvaluator.getInstance().solveEquation(input,
                        config.setEvalMode(EvaluateConfig.FRACTION), SolveEquationActivity.this);

                String decimal = MathEvaluator.getInstance().solveEquation(input,
                        config.setEvalMode(EvaluateConfig.DECIMAL),  SolveEquationActivity.this);

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
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
