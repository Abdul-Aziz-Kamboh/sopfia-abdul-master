package com.outstarttech.kabir.property.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;
import com.inventioncore.kabir.sopfia.R;

import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendCredits extends AppCompatActivity {

    private SessionManager sessionManager;

    private ProgressDialog pdLoading;
    private static RequestQueue queue;

    private EditText toAddress, creditValue;
    private String uName, uEmail,uPassword, toWalletAddress, tokenValueSend;
    private Button sendTokens;
    Boolean toAddressVerified = false, tokenValueVerified = false, sameAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_credits);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        queue = Volley.newRequestQueue(SendCredits.this, new HurlStack());

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Send Ethmny Tokens");
        }

        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);



        sessionManager = new SessionManager(this);
        //Toast.makeText(getApplicationContext(), "GOT Wallet : " + sessionManager.getKeyLoginWalletBalance(), Toast.LENGTH_SHORT).show();

        toAddress = (EditText) findViewById(R.id.toAddress);
        creditValue = (EditText) findViewById(R.id.creditAmount);

        sendTokens = (Button) findViewById(R.id.sendTokenButton);
        sendTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toWalletAddress = toAddress.getText().toString();
                tokenValueSend = creditValue.getText().toString();


                if (!toWalletAddress.isEmpty()) {
                    toAddressVerified = true;
                }
                if(tokenValueSend.equals("")){
                    tokenValueVerified = false;
                }
                else if (Double.parseDouble(tokenValueSend) == 0) {
                    tokenValueVerified = false;
                }
                else if(tokenValueSend.equals(""))
                {
                    tokenValueVerified = false;
                }
                else{
                    tokenValueVerified = true;
                }
                if (!toAddressVerified) {
                    toAddress.setError("Invalid Email Address");
                }
                if (!tokenValueVerified) {
                    creditValue.setError("Amount Invalid");
                }
                if(toWalletAddress.equals(sessionManager.getKeyLoginEmail())){
                    toAddress.setError("Same Email Detected");
                }
                else {
                    sameAddress = true;
                }
                if (toAddressVerified && tokenValueVerified && sameAddress) {
                    pdLoading.show();
                    SendCredits(toWalletAddress, tokenValueSend, sessionManager.getKeyLoginEmail());

                }
            }
        });

    }



    public void SendCredits(final String toEmail, final String sendValue, final String fromEmail)
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Transaction");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.SEND_CREDITS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    if (pdLoading != null && pdLoading.isShowing()) {
                        pdLoading.dismiss();
                    }
                    JSONObject jObj = new JSONObject(response);
                    int responseCode = jObj.getInt("responseCode");

//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
                    if (responseCode == 200) {

//

                        Toast.makeText(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
                        pdLoading.dismiss();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(SendCredits.this, Main2Activity.class));
                    }
                } catch (JSONException e) {

                    pdLoading.dismiss();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                pdLoading.dismiss();
                if(error.networkResponse.statusCode == 408){
                    Toast.makeText(getApplicationContext(), "Not Enough Credits", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Connection Problem, Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("transaction[user_email]", fromEmail);
                params.put("transaction[amount]", sendValue);
                params.put("transaction[rec_id]", toEmail);
                params.put("value", sendValue);
//                Log.d(TAG, "Login getParams: " + params);
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + sessionManager.getLoginToken());
                return params;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);

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


    @Override
    protected void onResume() {
        super.onResume();
//
//        mPieChart.clearChart();
//        mPieChart2.clearChart();
//        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
//        SessionManager sessionManager = new SessionManager(SendCredits.this);
//        sessionManager.logoutUser();
    }
}
