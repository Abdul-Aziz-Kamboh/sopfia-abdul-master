package com.outstarttech.kabir.property.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import com.bumptech.glide.Glide;
import com.codextech.kabir.bills_app.LockableScrollView;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;
import com.google.android.material.navigation.NavigationView;
import com.inventioncore.kabir.sopfia.R;
import com.outstarttech.kabir.property.activities.LogInActivity;
import com.outstarttech.kabir.property.activities.marketplace.MarketPlace;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.amirs.JSON;

import static com.outstarttech.kabir.property.activities.LogInActivity.SHARED_PREFS;
import static com.outstarttech.kabir.property.activities.LogInActivity.VERIFIED;

public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView1;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public Toolbar mToolbar;
    private SessionManager sessionManager;

    private static RequestQueue queue;
    private LinearLayout cardAlgebra, cardDictionary, cardMarketplace, cardQRCodes, appcreditDetail, ethmnytokenDetail;
    TextView nameText;
    TextView emailText;
    private TextView appCredit;
    private TextView tokenValue;
    private TextView datenew;
    private TextView lateInv;
    private DecimalFormat formatter;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        queue = Volley.newRequestQueue(Main2Activity.this, new HurlStack());

        navigationView1 = (NavigationView) findViewById(R.id.drawerMainActivity);
        navigationView1.setNavigationItemSelectedListener(this);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMainActivity);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_format_align_center_black_24dp);


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
        getSupportActionBar().setTitle("");
        mToolbar.setBackgroundColor(Color.TRANSPARENT);


        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF45BB99);

        // Need To Uncomment
        checkifVerified();



        sessionManager = new SessionManager(this);
//        Toast.makeText(getApplicationContext(), "GOT Credits : " + sessionManager.getAppCredit(), Toast.LENGTH_SHORT).show();
        view = findViewById(R.id.include2);

        appCredit = view.findViewById(R.id.appCreditVal);
        tokenValue = view.findViewById(R.id.tokenBalanceVal);
        appcreditDetail = view.findViewById(R.id.appCreditDetail);
        ethmnytokenDetail = view.findViewById(R.id.ethmnyTokenDetail);

        cardAlgebra = (LinearLayout) findViewById(R.id.cardAlgebraCalculator);
        cardDictionary = (LinearLayout) findViewById(R.id.cardDictionary);
        cardMarketplace = (LinearLayout) findViewById(R.id.cardMarketplace);
        cardQRCodes = (LinearLayout) findViewById(R.id.cardQRCodes);

        appcreditDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Main2Activity.this, CreditsDetail.class));
                Intent intent = new Intent(getBaseContext(), CreditsDetail.class);
                intent.putExtra("APP_CREDIT", appCredit.getText().toString());
                startActivity(intent);
            }
        });

        ethmnytokenDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EthmnyDetail.class);
                intent.putExtra("TOKEN_CREDIT", tokenValue.getText().toString());
                startActivity(intent);
            }
        });

//        lateInv = view.findViewById(R.id.lateInvValue);

//        loadData();


        View headerView = navigationView1.getHeaderView(0);
        TextView text = (TextView) headerView.findViewById(R.id.navName);
        TextView text2 = (TextView) headerView.findViewById(R.id.navEmail);
        ImageView imageView = (ImageView) headerView.findViewById(R.id.navImage);
        if(sessionManager.getKeyLoginId().length() > 1)
        {
            text.setText(sessionManager.getLoginUsername());
            text2.setText(sessionManager.getKeyLoginEmail());
        }
        else {
            text.setText("Guest");
            text2.setText("SignIn For Complete Features");
        }
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Main2Activity.this, LogInActivity.class));
            }
        });
//        Glide.with(Main2Activity.this)
//                .load(sessionManager.getKeyLoginImagePath())
//                .into(imageView);

//        home.setBackgroundColor(Color.parseColor("#c1d8f8"));
// android:name="com.outstarttech.kabir.property.activities.LogInActivity"
        cardAlgebra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, Calculations.class));
            }
        });

        cardDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, Dictionaryclass.class));
            }
        });

        cardMarketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, MarketPlace.class));
            }
        });

        cardQRCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, Qrcodes.class));
            }
        });
//        View view1 = findViewById(R.id.);
//        TextView tvName1 = view1.findViewById(R.id.tvName);
//        tvName1.setText(sessionManager.getKeyLoginRoleName());

    }
        // Need To Uncomment
    public void checkifVerified(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sharedPreferences.getString(VERIFIED,"").equals("0")){
            finish();
            startActivity(new Intent(Main2Activity.this, CodeConfirmation.class));
        }
    }



    public void loadData(){
        if(sessionManager.getKeyLoginWalletAddress().equals("none"))
        {
            Toast.makeText(getApplicationContext(), "Kindly SignIn To View Points & Tokens", Toast.LENGTH_SHORT).show();
            appCredit.setText("N/A");
            tokenValue.setText("N/A");
        }
        else if(sessionManager.getKeyLoginWalletAddress().length() == 42){
//            getEthereumBalance();
            getAppCredit();
            getEthmnyBalance();
        }
        else {
            Toast.makeText(getApplicationContext(), "Kindly SignIn To View Points & Tokens", Toast.LENGTH_SHORT).show();
            appCredit.setText("N/A");
            tokenValue.setText("N/A");
        }
    }

    private String getFormatedData(String unformatedData) {
        if(unformatedData != null) {
            try {
                //unformatedData.replaceAll(",", "");
                Double result = Double.valueOf(unformatedData);
                DecimalFormat myFormatter = new DecimalFormat("###,##0.00");
                //DecimalFormat myFormatter = new DecimalFormat("#,###,###");
                //If you don't want to show .00 format
                return myFormatter.format(result);
            } catch (NumberFormatException e) {
                return unformatedData;
            }

        } else {
            return "0.00";
        }
    }

    public void getAppCredit(){
        if(sessionManager.isUserSignedIn()){
            UpdateUserDetails();
        }
        else {
            appCredit.setText("N/A");
        }
    }

    public void UpdateUserDetails()
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.GET, MyURLs.GET_USER_BY_ID + sessionManager.getKeyLoginId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jObj = null;
                try {
                        jObj = new JSONObject(response);
                        Boolean statusCode = jObj.getBoolean("status");

//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
                        if (statusCode) {

//                        Toast.makeText(getApplicationContext(), "Got Data", Toast.LENGTH_SHORT).show();
                            JSONObject jObjReponse = jObj.getJSONObject("response");
//                            String access_token = jObjReponse.getString("access_token");
//                            Log.d(TAG, "onResponse: access_token: " + access_token);
//                            JSONObject jObjUser = jObjReponse.getJSONObject("user");
                            //parsing user object
                            String user_id = jObjReponse.getString("user_id");
                            String user_name = jObjReponse.getString("user_name");
                            String user_image = jObjReponse.getString("user_img");
                            String user_email = jObjReponse.getString("user_email");
                            String user_password = jObjReponse.getString("user_password");
                            String user_privatekey = jObjReponse.getString("user_privatekey");
                            String user_status = jObjReponse.getString("user_status");
                            String user_confirm = jObjReponse.getString("user_confirm");
                            String user_scope = jObjReponse.getString("user_scope");
                            String wallet_address = jObjReponse.getString("wallet_address");
                            String fingerprint_auth = jObjReponse.getString("fingerprint_auth");
                            String faceid_auth = jObjReponse.getString("face_auth");
                            String app_credit = jObjReponse.getString("app_credit");
                            String created_at = jObjReponse.getString("created_at");
                            String updated_at = jObjReponse.getString("updated_at");
//                            Long iat = jObjReponse.getLong("iat");

                            sessionManager.setAppCredit(app_credit);
                            String appCreditFormatted = getFormatedData(app_credit);
                            appCredit.setText(appCreditFormatted);

//                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Getting Data Failed, Try Again Later", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("APPCREDIT", "Failed with error msg:\t" + error.getMessage());
                Log.d("APPCREDIT", "Error StackTrace: \t" + error.getStackTrace());
                // edited here
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e("APPCREDIT", new String(htmlBodyBytes), error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Getting Data Failed" + error.getMessage() + error.networkResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        })


        {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("module", "account");
//                params.put("action", "tokenbalance");
//                params.put("contractaddress", "0xbf4a2ddaa16148a9d0fa2093ffac450adb7cd4aa");
//                params.put("address", sessionManager.getKeyLoginWalletAddress());
//                params.put("tag", "latest");
//                params.put("apikey", "YourApiKeyToken");
//                Log.d(TAG, "Login getParams: " + params);
//                return params;
//            }


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

    public void getEthmnyBalance()
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.GET, MyURLs.GET_ETHMNY_BALANCE + "?module=account&action=tokenbalance&contractaddress=0xbf4a2ddaa16148a9d0fa2093ffac450adb7cd4aa&address=" + sessionManager.getKeyLoginWalletAddress() + "&tag=latest&apikey=YourApiKeyToken", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    JSON json = new JSON(response);

                    String ethmbalance, statusCode;
                    int tempBalance;
                    ethmbalance = json.key("result").stringValue();
                    statusCode = json.key("status").stringValue();
                    if (statusCode.equals("1")){
                        tempBalance = Integer.parseInt(ethmbalance);
                        tempBalance = tempBalance/100;
                        ethmbalance = Integer.toString(tempBalance);
                        String ethmbalanceformatted = getFormatedData(ethmbalance);
                        tokenValue.setText(ethmbalanceformatted);
                    }
                    else{
                        tokenValue.setText("0");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Getting Data Failed, Try Again Later", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Getting Data Failed", Toast.LENGTH_SHORT).show();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("module", "account");
//                params.put("action", "tokenbalance");
//                params.put("contractaddress", "0xbf4a2ddaa16148a9d0fa2093ffac450adb7cd4aa");
//                params.put("address", sessionManager.getKeyLoginWalletAddress());
//                params.put("tag", "latest");
//                params.put("apikey", "YourApiKeyToken");
//                Log.d(TAG, "Login getParams: " + params);
                return params;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/json");
//                params.put("Authorization", "Bearer " + sessionManager.getLoginToken());
//                return params;
//            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);

    }

    public void getEthereumBalance()
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.GET_ETHEREUM_BALANCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
//                    int responseCode = jObj.getInt("responseCode");
//                    if (responseCode == 200) {
                    JSON json = new JSON(response);
                    String ethbalance;
//                    String paidInvoice;
//                    String lateInvoice;

//                    JSONObject jObjReponse = jObj.getJSONObject("wallet");
                    //Toast.makeText(getApplicationContext(), "IN TRY", Toast.LENGTH_SHORT).show();

                        ethbalance = json.key("wallet").key("balance").stringValue();

                    //Toast.makeText(getApplicationContext(), generatedInvoice, Toast.LENGTH_SHORT).show();
//                        paidInvoice = json.key("data").key("ppaid_rent").stringValue();
//                        lateInvoice = json.key("data").key("late_unpaid_rent").stringValue();


                    String ethbalanceFormated = getFormatedData(ethbalance);
                    appCredit.setText(ethbalanceFormated);
//                    tokenValue.setText("0.000000");

//                    Toast.makeText(getApplicationContext(), "GOT Name : " + tempName, Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Getting Data Failed, Try Again Later", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Getting Data Failed", Toast.LENGTH_SHORT).show();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("currency", "eth");
                params.put("walletAddress", sessionManager.getKeyLoginWalletAddress());
//                Log.d(TAG, "Login getParams: " + params);
                return params;
            }


//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Accept", "application/json");
//                params.put("Authorization", "Bearer " + sessionManager.getLoginToken());
//                return params;
//            }
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
                startActivity(new Intent(Main2Activity.this, Main2Activity.class));
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
                startActivity(new Intent(Main2Activity.this, Roadmap.class));
                break;
            }
            case R.id.help:
            {
//                finish();
                startActivity(new Intent(Main2Activity.this, UpdateDetails.class));
                //do somthing
                break;
            }
            case R.id.nav_logout:
            {
                finish();
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.logoutUser();
                startActivity(new Intent(Main2Activity.this, LogInActivity.class));
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
        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        SessionManager sessionManager = new SessionManager(Main2Activity.this);
        sessionManager.logoutUser();
    }
}
