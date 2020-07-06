package com.outstarttech.kabir.property.activities;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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

public class UpdateDetails extends AppCompatActivity{

    private NavigationView navigationView1;
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mToggle;
    public Toolbar mToolbar;
    private SessionManager sessionManager;

    private static RequestQueue queue;
    private CardView mCard, sCard, tCard, bCard;
    TextView nameText;
    TextView emailText;
    private TextView ethValue;
    private TextView tokenValue;
    private TextView lateInv;
    private DecimalFormat formatter;
    private View view;

    private EditText userName, userEmail, userPassword, userWalletAddress, userWalletKey;
    private String uName, uEmail,uPassword, uWalletAddress, uWalletKey, checkPassChange;
    private Button saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Update Details");
        }

        queue = Volley.newRequestQueue(UpdateDetails.this, new HurlStack());




        sessionManager = new SessionManager(this);
        //Toast.makeText(getApplicationContext(), "GOT Wallet : " + sessionManager.getKeyLoginWalletBalance(), Toast.LENGTH_SHORT).show();
//        view = findViewById(R.id.include2);


        userName = (EditText) findViewById(R.id.fullName);
        userEmail = (EditText) findViewById(R.id.email);
        userPassword = (EditText) findViewById(R.id.password);
        userWalletAddress = (EditText) findViewById(R.id.walletAddress);
        userWalletKey = (EditText) findViewById(R.id.privateKey);
        saveChanges = (Button) findViewById(R.id.saveButton);

        userName.setText(sessionManager.getLoginUsername());
        userEmail.setText(sessionManager.getKeyLoginEmail());
        userPassword.setText(sessionManager.getLoginPassword());
//        userPassword.setText(sessionManager.get);
        userWalletAddress.setText(sessionManager.getKeyLoginWalletAddress());
        userWalletKey.setText(sessionManager.getKeyLoginPrivateKey());
        checkPassChange = sessionManager.getLoginPassword();
//        lateInv = view.findViewById(R.id.lateInvValue);


        if(sessionManager.isUserSignedIn())
        {
        }
        else {
//            text.setText("Guest");
//            text2.setText("SignIn For Complete Features");

            finish();
            startActivity(new Intent(UpdateDetails.this, LogInActivity.class));
        }
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    public void saveData(){
        userPassword.setError(null);
        Boolean passwordVarified = true, emailVarified = true;
        uPassword = userPassword.getText().toString();
        uName = userName.getText().toString();
        uEmail = userEmail.getText().toString();
        uWalletAddress = userWalletAddress.getText().toString();
        uWalletKey = userWalletKey.getText().toString();

        if (uPassword.length() < 8) {
            passwordVarified = false;
        }
        if (uEmail.length() < 7) {
            emailVarified = false;
        }
        if (!emailVarified) {
            userEmail.setError("Invalid Email!");
        }
        if (!passwordVarified) {
            userPassword.setError("Invalid Password!");
        }
        if (emailVarified && passwordVarified) {
            updateDetails();
        }
    }

    public void updateDetails()
    {
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.PUT, MyURLs.UPDATE_DETAILS + sessionManager.getKeyLoginId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
//                    int responseCode = jObj.getInt("responseCode");
//                    if (responseCode == 200) {
                    JSON json = new JSON(response);
                    Boolean checkstatus;
                    checkstatus = json.key("status").booleanValue();

                    JSONObject jObjUser = jObj.getJSONObject("response");

                    String user_id = jObjUser.getString("user_id");
                    String user_name = jObjUser.getString("user_name");
                    String user_image = jObjUser.getString("user_img");
                    String user_email = jObjUser.getString("user_email");
                    String user_password = jObjUser.getString("user_password");
                    String user_privatekey = jObjUser.getString("user_privatekey");
                    String user_status = jObjUser.getString("user_status");
                    String user_confirm = jObjUser.getString("user_confirm");
                    String user_scope = jObjUser.getString("user_scope");
                    String wallet_address = jObjUser.getString("wallet_address");
                    String fingerprint_auth = jObjUser.getString("fingerprint_auth");
                    String faceid_auth = jObjUser.getString("face_auth");
                    String app_credit = jObjUser.getString("app_credit");
                    String created_at = jObjUser.getString("created_at");
                    String updated_at = jObjUser.getString("updated_at");
//                    Long iat = jObjUser.getLong("iat");


                    if(checkstatus){
                        Toast.makeText(getApplicationContext(), "User Updated Successfully", Toast.LENGTH_SHORT).show();

                        sessionManager.loginnUser(user_id, sessionManager.getLoginToken(), user_name, user_image, user_email, user_password, user_privatekey
                                , user_status, user_confirm, user_scope, wallet_address,fingerprint_auth, faceid_auth,
                                app_credit, created_at, updated_at, sessionManager.getLoginTimestamp());

                    }
                    else {

                        Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Getting Data Failed: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user[user_name]", uName);
                params.put("user[user_email]", uEmail);
                if(!checkPassChange.equals(userPassword.getText().toString())){
                    params.put("user[user_password]", uPassword);
                }
                params.put("user[wallet_address]", uWalletAddress);
                params.put("user[user_privatekey]", uWalletKey);
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
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}
