package com.outstarttech.kabir.property.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendEth extends AppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 40000;
    private final int MOVE_DOT = 2500;
    private final int MOVE_DOT2 = 8000;
    private final int MOVE_DOT3 = 14500;
    private final int MOVE_DOT4 = 24000;
    private final int MOVE_DOT5 = 2500;
    private final int MOVE_DOT6 = 3000;
    private final int MOVE_DOT7 = 3500;


    private NavigationView navigationView1;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public Toolbar mToolbar;
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
        setContentView(R.layout.activity_send_eth);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Earn Credits");
        }

        queue = Volley.newRequestQueue(SendEth.this, new HurlStack());


        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);



        sessionManager = new SessionManager(this);
        //Toast.makeText(getApplicationContext(), "GOT Wallet : " + sessionManager.getKeyLoginWalletBalance(), Toast.LENGTH_SHORT).show();


        toAddress = (EditText) findViewById(R.id.toAddress);
        tokenValue = (EditText) findViewById(R.id.tokenAmount);

        sendTokens = (Button) findViewById(R.id.sendEthButton);
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
                    sendEthereum(toWalletAddress, tokenValueSend, sessionManager.getKeyLoginWalletAddress(), sessionManager.getKeyLoginPrivateKey());

                }
            }
        });

    }



    public void sendEthereum(final String toWallet, final String sendValue, final String fromWallet, final String prvKey)
    {
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Transaction");
        progressDialog.show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressDialog.setMessage("Compiling the data...");
            }
        }, MOVE_DOT);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressDialog.setMessage("Transaction is being Encrypted...");
            }
        }, MOVE_DOT2);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressDialog.setMessage("Processing Data...");
            }
        }, MOVE_DOT3);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                progressDialog.setMessage("Getting Ethereum servers status...");
            }
        }, MOVE_DOT4);

        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.SEND_ETH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                try {
                    if (pdLoading != null && pdLoading.isShowing()) {
                        pdLoading.dismiss();
                    }
                    JSONObject jObj = new JSONObject(response);
//                    int responseCode = jObj.getInt("responseCode");
//                    Toast.makeText(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
//

                    finish();
                    startActivity(new Intent(SendEth.this, Main2Activity.class));
//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
//                    if (responseCode == 200) {
//
////
//
//                        Toast.makeText(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();
//
//                        finish();
//                        startActivity(new Intent(SendEth.this, Main2Activity.class));
//                    }
//                    else{
//
//                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if(error.networkResponse.statusCode == 503){
                    Toast.makeText(getApplicationContext(), "[Transaction Pending] Ethereum Servers Busy, Your balance will be updated soon", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(SendEth.this, Main2Activity.class));
                }
                else if(error.networkResponse.statusCode == 400){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Transaction Unsuccessful: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("currency", "eth");
                params.put("from", fromWallet);
                params.put("privKey", prvKey);
                params.put("to", toWallet);
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
        SessionManager sessionManager = new SessionManager(SendEth.this);
        sessionManager.logoutUser();
    }


}