package com.outstarttech.kabir.property.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.codextech.kabir.bills_app.migrations.VersionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;
import com.codextech.kabir.bills_app.utils.NetworkAccess;
import com.inventioncore.kabir.sopfia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import eu.amirs.JSON;

import static com.outstarttech.kabir.property.activities.LogInActivity.FIRSTLOGIN;
import static com.outstarttech.kabir.property.activities.LogInActivity.SHARED_PREFS;
import static com.outstarttech.kabir.property.activities.LogInActivity.VERIFIED;

public class CodeConfirmation extends AppCompatActivity {

    public static final String TAG = "LogInActivity";
    private ProgressDialog pdLoading;
    private EditText etEmail, etPassword;
    private TextView bSignup;
    private TextView bReset;
    private String email, password;
    private Button verifyButton, skipLogin;
    private SessionManager sessionManager;
    private static RequestQueue queue;
    private String check;
    Dialog MyDialog;
    private Button notVerifiedokay;
    public static Activity activity;
    private String conCodeText;
    private EditText conCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_confirmation);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity = this;
        queue = Volley.newRequestQueue(CodeConfirmation.this, new HurlStack());

//        Version Control
        VersionManager versionManager = new VersionManager(getApplicationContext());
        if (!versionManager.runMigrations()) {
            // if migration has failed
            Toast.makeText(getApplicationContext(), "Migration Failed", Toast.LENGTH_SHORT).show();
        }

        conCodeEditText = (EditText) findViewById(R.id.conCodeEdit);
        verifyButton = (Button) findViewById(R.id.verifybutton);


        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isUserSignedIn()) {
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        }
//        else{
//
//            Toast.makeText(getApplicationContext(), "Not Signed In", Toast.LENGTH_SHORT).show();
//        }

//        Toast.makeText(getApplicationContext(), "Data: " + sessionManager.getKeyLoginEmail(), Toast.LENGTH_SHORT).show();
        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                conCodeText = conCodeEditText.getText().toString();
                if (conCodeText.length() < 6) {
                    conCodeEditText.setError("Enter 6 Digit Code");
                }
                else {
                    checkCode(sessionManager.getKeyLoginId(), conCodeText);
                }
            }
        });
//        hardcoding number and password for development speedup purposes
//        etEmail.setText("michealscofield321@gmail.com");
//        etPassword.setText("newnewnew321");


//        bReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(MyURLs.FORGOT_PASSWORD));
//                startActivity(i);
//                String projectToken = MixpanelConfig.projectToken;
//                MixpanelAPI mixpanel = MixpanelAPI.getInstance(LogInActivity.this, projectToken);
//                mixpanel.track("Password Reset");
//            }
//        });


    }

    public void checkCode(final String keyLoginId, final String con_code)
    {
        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.GET, MyURLs.GET_USER_BY_ID + keyLoginId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    JSON json = new JSON(response);
                    String tempCode = json.key("response").key("confirmation_code").stringValue();
                    if(tempCode.equals(con_code)){
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(VERIFIED, "1");
                        editor.putString(FIRSTLOGIN, "1");
                        editor.apply();
                        finish();
//                        String testVerified = sharedPreferences.getString(VERIFIED,"");
//
//                        Toast.makeText(getApplicationContext(), "Test Verified Code COnfirmation" + testVerified, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Invalid Code Entered", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        if (pdLoading != null && pdLoading.isShowing()) {
            pdLoading.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        sessionManager.logoutUser();
        finish();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
    }

    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(CodeConfirmation.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.notverified);
        MyDialog.setTitle("Add Subscriber");

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

//    private void updateFirebaseIdAndInitConfigMakeRequest(Activity activity, @Nullable JSONObject returnInitJson) {
//
//        final int MY_SOCKET_TIMEOUT_MS = 60000;
//        final String BASE_URL = MyURLs.UPDATE_AGENT;
//        Uri builtUri;
//        if (returnInitJson != null) {
//            builtUri = Uri.parse(BASE_URL)
//                    .buildUpon()
//                    .appendQueryParameter("config", "" + returnInitJson)
//                    .appendQueryParameter("device_id", "" + sessionManager.getKeyLoginFirebaseRegId())
//                    .appendQueryParameter("api_token", "" + sessionManager.getLoginToken())
//                    .build();
//
//        } else {
//            builtUri = Uri.parse(BASE_URL)
//                    .buildUpon()
//                    .appendQueryParameter("device_id", "" + sessionManager.getKeyLoginFirebaseRegId())
//                    .appendQueryParameter("api_token", "" + sessionManager.getLoginToken())
//                    .build();
//        }
//        final String myUrl = builtUri.toString();
//        StringRequest sr = new StringRequest(Request.Method.PUT, myUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "onResponse() updateFirebaseIdAndInitConfigMakeRequest: response = [" + response + "]");
//                try {
//                    if (pdLoading != null && pdLoading.isShowing()) {
//                        pdLoading.dismiss();
//                    }
//                    JSONObject jObj = new JSONObject(response);
//                    int responseCode = jObj.getInt("responseCode");
//                    if (responseCode == 200) {
//
//                        JSONObject responseObject = jObj.getJSONObject("response");
//                        Log.d(TAG, "onResponse : FirebaseLocalRegID : " + sessionManager.getKeyLoginFirebaseRegId());
//                        Log.d(TAG, "onResponse : FirebaseServerRegID : " + responseObject.getString("device_id"));
//
////                        TheCallLogEngine theCallLogEngine = new TheCallLogEngine(getApplicationContext());
////                        theCallLogEngine.execute();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (pdLoading != null && pdLoading.isShowing()) {
//                    pdLoading.dismiss();
//                }
//                Log.d(TAG, "onErrorResponse: CouldNotUpdateInitConfigMakeRequest OR CouldNotSyncAgentFirebaseRegId");
//
////                RecordingManager recordingManager = new RecordingManager();
////                recordingManager.execute();
////                TheCallLogEngine theCallLogEngine = new TheCallLogEngine(getApplicationContext());
////                theCallLogEngine.execute();
//            }
//        }) {
//        };
//        sr.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(sr);
//    }
}
