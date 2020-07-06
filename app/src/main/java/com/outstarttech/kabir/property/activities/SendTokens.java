package com.outstarttech.kabir.property.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
import com.inventioncore.kabir.sopfia.R;

import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.amirs.JSON;

public class SendTokens extends AppCompatActivity{


    private SessionManager sessionManager;

    private ProgressDialog pdLoading;
    private static RequestQueue queue;

    private EditText toAddress, tokenValue;
    private String uName, uEmail,uPassword, toWalletAddress, tokenValueSend;
    private Button sendTokens;
    Boolean toAddressVerified = false, tokenValueVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tokens);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        queue = Volley.newRequestQueue(SendTokens.this, new HurlStack());

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


        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF45BB99);



        sessionManager = new SessionManager(this);
        //Toast.makeText(getApplicationContext(), "GOT Wallet : " + sessionManager.getKeyLoginWalletBalance(), Toast.LENGTH_SHORT).show();
//        view = findViewById(R.id.include2);



//        lateInv = view.findViewById(R.id.lateInvValue);

//
//        View headerView = navigationView1.getHeaderView(0);
//        TextView text = (TextView) headerView.findViewById(R.id.navName);
//        text.setText(sessionManager.getLoginUsername());
//        TextView text2 = (TextView) headerView.findViewById(R.id.navEmail);
//        text2.setText(sessionManager.getKeyLoginEmail());
//        ImageView imageView = (ImageView) headerView.findViewById(R.id.navImage);

        toAddress = (EditText) findViewById(R.id.toAddress);
        tokenValue = (EditText) findViewById(R.id.tokenAmount);

        sendTokens = (Button) findViewById(R.id.sendTokenButton);
        sendTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                toWalletAddress = toAddress.getText().toString();
                tokenValueSend = tokenValue.getText().toString();


                if (toWalletAddress.length() == 42) {
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
                    toAddress.setError("Invalid Wallet Address");
                }
                if (!tokenValueVerified) {
                    tokenValue.setError("Amount Invalid");
                }
                if (toAddressVerified && tokenValueVerified) {
                    pdLoading.show();
                    int value = Integer.parseInt(tokenValueSend);
                    value = value * 100;
                    tokenValueSend = Integer.toString(value);
                    SendTokens(toWalletAddress, tokenValueSend, sessionManager.getKeyLoginWalletAddress(), sessionManager.getKeyLoginPrivateKey());

                }
            }
        });

    }



    public void SendTokens(final String toWallet, final String sendValue, final String fromWallet, final String prvKey)
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Transaction");
        progressDialog.show();

        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.SEND_TOKEN, new Response.Listener<String>() {
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
                        startActivity(new Intent(SendTokens.this, Main2Activity.class));
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
                Toast.makeText(getApplicationContext(), "Transaction Unsuccessful: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("from_address", sessionManager.getKeyLoginWalletAddress());
                params.put("from_private_key", sessionManager.getKeyLoginPrivateKey());
                params.put("to_address", toWallet);
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
        SessionManager sessionManager = new SessionManager(SendTokens.this);
        sessionManager.logoutUser();
    }
}