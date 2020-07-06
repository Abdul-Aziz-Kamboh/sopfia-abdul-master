package com.outstarttech.kabir.property.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

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

public class BuyTokens extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private String uName, uEmail,uPassword, toWalletAddress, tokenValueSend, ethValueSend, ethValueTemp;
    private Button sendTokens;
    Boolean toAddressVerified = false, tokenValueVerified = false;
    private TextView ethValText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_tokens);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        queue = Volley.newRequestQueue(BuyTokens.this, new HurlStack());

//        navigationView1 = (NavigationView) findViewById(R.id.drawerMainBuyActivity);
//        navigationView1.setNavigationItemSelectedListener(this);
//
//        mToolbar = (Toolbar) findViewById(R.id.nav_action);
//        setSupportActionBar(mToolbar);

        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);




        sessionManager = new SessionManager(this);




        tokenValue = (EditText) findViewById(R.id.tokenAmount);
        ethValText = (TextView) findViewById(R.id.ethCost);
        sendTokens = (Button) findViewById(R.id.sendEthButton);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    ethValText.setText("0");
                }
                else{
                    Double tempval = Double.parseDouble(s.toString());
                    tempval = tempval/100000;
                    ethValText.setText(Double.toString(tempval));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        tokenValue.addTextChangedListener(inputTextWatcher);

        sendTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tokenValueSend = tokenValue.getText().toString();
                ethValueTemp = tokenValue.getText().toString();
                double ethVal = Double.parseDouble(ethValueTemp);
                ethVal = ethVal/100000;
                ethValueSend = Double.toString(ethVal);


                if(tokenValueSend.equals("")){
                    tokenValueVerified = false;
                }
                else if (Double.parseDouble(tokenValueSend) == 0) {
                    tokenValueVerified = false;
                }
                else if(Integer.parseInt(tokenValueSend) < 100 || Integer.parseInt(tokenValueSend) > 100000)
                {
                    tokenValueVerified = false;
                }
                else if(tokenValueSend.equals(""))
                {
                    tokenValueVerified = false;
                }
                else{
                    tokenValueVerified = true;
                }
                if (!tokenValueVerified) {
                    tokenValue.setError("Amount Should be 100 - 100000");
                }
                if (tokenValueVerified) {
                    pdLoading.show();
                    buyTokens(tokenValueSend, ethValueSend,  sessionManager.getKeyLoginWalletAddress(), sessionManager.getKeyLoginPrivateKey());
                }
            }
        });

    }

    private void buyTokens(String tokenValueSend, String ethValueSend, String keyLoginWalletAddress, String keyLoginPrivateKey) {
        sendEthereum(tokenValueSend, ethValueSend, keyLoginWalletAddress, keyLoginPrivateKey);
    }

    public void SendTokens(final String toWallet, final String sendValue, final String fromWallet, final String prvKey)
    {
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Signing Transaction");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

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

                        progressDialog.dismiss();
                        pdLoading.dismiss();
                        Toast.makeText(getApplicationContext(), "Transaction Successful", Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(new Intent(BuyTokens.this, Main2Activity.class));
                    }
                } catch (JSONException e) {

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
                params.put("from_address", fromWallet);
                params.put("from_private_key", prvKey);
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


    public void sendEthereum(final String tokenValueSend, final String sendValue, final String fromWallet, final String prvKey)
    {
//        Toast.makeText(getApplicationContext(), "Send Eth Value: " + sendValue, Toast.LENGTH_SHORT).show();
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
                    int value = Integer.parseInt(tokenValueSend);
                    value = value * 100;
                    String tokenUpdated = Integer.toString(value);
                    SendTokens(sessionManager.getKeyLoginWalletAddress(), tokenUpdated, "0x9B2f0622cd0E0c7F7F683b9a730DF63dbb9a817C", "104CFB6DE7C1947AE8D3BFA1889A76504A8918330E042E331B531FD6ED012D16");
//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
//                    if (responseCode == 200) {

//                    }
//                    else{
//
//                    }
                } catch (JSONException e) {
                    pdLoading.dismiss();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                pdLoading.dismiss();
                if(error.networkResponse.statusCode == 503){

                    Toast.makeText(getApplicationContext(), "[Transaction Pending] Ethereum Servers Busy, Your balance will be updated soon", Toast.LENGTH_LONG).show();
                    Integer value = Integer.parseInt(tokenValueSend);
                    value = value * 100;
                    String tokenUpdated = Integer.toString(value);
                    SendTokens(sessionManager.getKeyLoginWalletAddress(), tokenUpdated, "0x9B2f0622cd0E0c7F7F683b9a730DF63dbb9a817C", "104CFB6DE7C1947AE8D3BFA1889A76504A8918330E042E331B531FD6ED012D16");
                }
                else if(error.networkResponse.statusCode == 400){
                    progressDialog.dismiss();
                    pdLoading.dismiss();
                    Toast.makeText(getApplicationContext(), "Transaction Unsuccessful: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("currency", "eth");
                params.put("from", sessionManager.getKeyLoginWalletAddress());
                params.put("privKey", sessionManager.getKeyLoginPrivateKey());
                params.put("to", "0x9B2f0622cd0E0c7F7F683b9a730DF63dbb9a817C");
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
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.


        switch (item.getItemId()) {

            case R.id.home: {
                finish();
                startActivity(new Intent(BuyTokens.this, Main2Activity.class));
                //do somthing
                break;
            }
            case R.id.aboutus:
            {
                Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_SHORT).show();
                //do somthing
                break;
            }
            case R.id.roadmap:
            {
                Toast.makeText(getApplicationContext(), "Road Map", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.help:
            {
                finish();
                startActivity(new Intent(BuyTokens.this, BuyTokens.class));
                //do somthing
                break;
            }
            case R.id.nav_logout:
            {
                finish();
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.logoutUser();
                startActivity(new Intent(BuyTokens.this, LogInActivity.class));
                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();

                break;
            }

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void rateApp(){
        try
        {
            Uri uri1 = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(goToMarket);
        }catch (ActivityNotFoundException e){
            Uri uri1 = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri1);
            startActivity(goToMarket);
        }
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
        SessionManager sessionManager = new SessionManager(BuyTokens.this);
        sessionManager.logoutUser();
    }
}