package com.outstarttech.kabir.property.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.app.MixpanelConfig;
import com.codextech.kabir.bills_app.migrations.VersionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;
import com.codextech.kabir.bills_app.utils.NetworkAccess;
import com.inventioncore.kabir.sopfia.R;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private ProgressDialog pdLoading;
    private EditText etEmail, etPassword, etUsername, cPassword;
    private TextView bSignup;
    private TextView bReset;
    private String email, password, username,confirmPass;
    private Button signupButton;
    private SessionManager sessionManager;
    private static RequestQueue queue;
    private String check;
    Dialog MyDialog;
    private Button notVerifiedokay;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity = this;
        queue = Volley.newRequestQueue(SignUpActivity.this, new HurlStack());

//        Version Control
        VersionManager versionManager = new VersionManager(getApplicationContext());
        if (!versionManager.runMigrations()) {
            // if migration has failed
            Toast.makeText(getApplicationContext(), "Migration Failed", Toast.LENGTH_SHORT).show();
        }

        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isUserSignedIn()) {
            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            finish();
        }
        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        cPassword = (EditText) findViewById(R.id.confirmPassword);
        etUsername = (EditText) findViewById(R.id.username);
        bReset = (TextView) findViewById(R.id.alreadyMember);
        etEmail.getBackground().clearColorFilter();
        etPassword.getBackground().clearColorFilter();

        signupButton = (Button) findViewById(R.id.signUp);

//        hardcoding number and password for development speedup purposes
//        etEmail.setText("kabeerhafeez65@gmail.com");
//        etPassword.setText("kabir@123");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setError(null);
                etPassword.setError(null);
                etUsername.setError(null);
                cPassword.setError(null);
                Boolean emailVarified = true, passwordVarified = true, passconfirmed = true;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                username = etUsername.getText().toString();
                confirmPass = cPassword.getText().toString();

                if(!confirmPass.equals(password))
                {
                    passconfirmed = false;
                }

                if (email.length() < 7) {
                    emailVarified = false;
                }
                if (password.length() < 4) {
                    passwordVarified = false;
                }

//                if (!PhoneNumberAndCallUtils.isValidPassword(password)) {
//                    passwordVarified = false;
//                }
                if (!emailVarified) {
                    etEmail.setError("Invalid Email!");
                }
                if (!passwordVarified) {
                    etPassword.setError("Invalid Password!");
                }
                if(!passconfirmed)
                {
                    cPassword.setError("Password does not match");
                }
                if (emailVarified && passwordVarified && passconfirmed) {
                    pdLoading.show();
                    makeSignUpRequest(SignUpActivity.this, username, email, password);
                }
            }
        });
        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (pdLoading != null && pdLoading.isShowing()) {
            pdLoading.dismiss();
        }
        super.onDestroy();
    }

    public void makeSignUpRequest(final Activity activity, final String username, final String email, final String password) {
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.SIGNUP_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() makeLoginRequest called with: response = [" + response + "]");
//                pdLoading.dismiss();
                try {
                    if (pdLoading != null && pdLoading.isShowing()) {
                        pdLoading.dismiss();
                    }
                    JSONObject jObj = new JSONObject(response);
                    int responseCode = jObj.getInt("responseCode");

//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
                    if (responseCode == 200) {
//                        JSONObject jObjReponse = jObj.getJSONObject("response");
//                        String access_token = jObjReponse.getString("access_token");
//                        Log.d(TAG, "onResponse: access_token: " + access_token);
//                        JSONObject jObjUser = jObjReponse.getJSONObject("user");
//                        //parsing user object
//                        String user_id = jObjUser.getString("user_id");
//                        String user_name = jObjUser.getString("user_name");
//                        String user_image = jObjUser.getString("user_img");
//                        String user_email = jObjUser.getString("user_email");
//                        String user_password = jObjUser.getString("user_password");
//                        String user_privatekey = jObjUser.getString("user_privatekey");
//                        String user_status = jObjUser.getString("user_status");
//                        String user_confirm = jObjUser.getString("user_confirm");
//                        String user_scope = jObjUser.getString("user_scope");
//                        String wallet_address = jObjUser.getString("wallet_address");
//                        String created_at = jObjUser.getString("created_at");
//                        String updated_at = jObjUser.getString("updated_at");
//                        Long iat = jObjUser.getLong("iat");

//                        sessionManager.loginnUser(user_id, access_token, user_name, user_image, user_email, user_password, user_privatekey
//                                , user_status, user_confirm, user_scope, wallet_address, created_at, updated_at, iat);

                        Toast.makeText(getApplicationContext(), "SignUp Success", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pdLoading != null && pdLoading.isShowing()) {
                    pdLoading.dismiss();
                }
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                if (!NetworkAccess.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Turn on wifi or Mobile Data", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (error.networkResponse != null) {
                            if (error.networkResponse.statusCode == 401) {
                                Toast.makeText(activity, "Email or password already exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(activity, "Email or password already exists", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet Connectivity might be poor", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user[user_name]", username);
                params.put("user[user_email]", email);
                params.put("user[user_password]", password);
                Log.d(TAG, "SignUp getParams: " + params);
                return params;
            }

//            @Override
//            public byte[] getBody() throws com.android.volley.AuthFailureError {
//                String str = "{\"user\" : {\"user_email\": \"ibtisamasif@gmail.com\",\"user_password\": \"123456\"}}";
//                Log.d(TAG, "getBody: str: " + str);
//                return str.getBytes();
//            }

//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                return params;
//            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(SignUpActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.notverified);
        MyDialog.setTitle("Email Verification");

        notVerifiedokay = (Button)MyDialog.findViewById(R.id.notokay);

        notVerifiedokay.setEnabled(true);

        //final String newmerchantid = getMerchantId(submerchantid);


        notVerifiedokay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });


        MyDialog.show();
    }

        @Override
        public void onBackPressed() {
            super.onBackPressed();

            finish();
            SessionManager sessionManager = new SessionManager(SignUpActivity.this);
            sessionManager.logoutUser();
        }
}
