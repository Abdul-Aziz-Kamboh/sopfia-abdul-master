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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.ActionBar;

import android.text.InputType;
import android.view.MenuItem;
import android.view.WindowManager;

import com.inventioncore.kabir.sopfia.R;
import com.duy.calculator.activities.base.BaseEvaluatorActivity;
import com.duy.calculator.evaluator.LaTexFactory;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.utils.DLog;
import com.gx.common.collect.Lists;

import org.matheclipse.core.interfaces.IExpr;

import java.util.ArrayList;

/**
 * Created by Duy on 06-Jan-17.
 */

public class PiActivity extends BaseEvaluatorActivity {
    private static final String STARTED = PiActivity.class.getName() + "started";
    private boolean isDataNull = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Pi Number");
        }
        setTitle(getString(R.string.pi_number));

        mBtnEvaluate.setText(R.string.eval);
        mHint1.setHint(getString(R.string.precision_));
        mInputFormula.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_SIGNED);

        getIntentData();

        boolean isStarted = mPreferences.getBoolean(STARTED, false);
        if ((!isStarted || DLog.UI_TESTING_MODE) && isDataNull) {
            mInputFormula.setText("1000");
        }

    }

    @Override
    public void clickHelp() {

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
                isDataNull = false;
                clickEvaluate();
            }
        }
    }

    @Override
    protected String getExpression() {
        return "N(Pi," + mInputFormula.getCleanText() + ")";
    }

    @Override
    public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @WorkerThread
            @Override
            public ArrayList<String> execute(String input) {
                IExpr iExpr = MathEvaluator.getInstance().evalStr(input);
                String result = LaTexFactory.toLaTeX(iExpr);
                return Lists.newArrayList(result);
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
