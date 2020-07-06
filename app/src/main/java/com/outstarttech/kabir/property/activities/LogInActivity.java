package com.outstarttech.kabir.property.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.codextech.kabir.bills_app.app.MixpanelConfig;
import com.codextech.kabir.bills_app.migrations.VersionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;
import com.codextech.kabir.bills_app.utils.NetworkAccess;

import com.inventioncore.kabir.sopfia.R;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class LogInActivity extends AppCompatActivity {


    private String loginTime = "0";
    public static final String TAG = "LogInActivity";
    private ProgressDialog pdLoading;
    private EditText etEmail, etPassword;
    private TextView bSignup;
    private TextView bReset;
    private String email, password;
    private Button loginButtonLoginScreen, skipLogin;
    private SessionManager sessionManager;
    private static RequestQueue queue;
    private String check;
    Dialog MyDialog, FingerprintDiaglogue;
    private Button notVerifiedokay;
    public static Activity activity;

    Dialog fingerMyDialog;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String VERIFIED = "verified";
    public static final String FIRSTLOGIN = "firstlogin";
    private SwitchCompat fingerPrintSwitch;
    private Boolean firstTimeLoginEmail = true;

    // For Fingerprint
    private TextView mHeadingLabel;
    private ImageView mFingerprintImage;
    private TextView mParaLabel;

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private KeyStore keyStore;
    private Cipher cipher;
    private String KEY_NAME = "AndroidKey";
    public SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        prefs = getSharedPreferences("com.inventioncore.kabir.sopfia", MODE_PRIVATE);
        //Progress Dialogue
        pdLoading = new ProgressDialog(this);
        pdLoading.setTitle("Loading data");
        //this method will be running on UI thread
        pdLoading.setMessage("Please Wait...");
        pdLoading.setCancelable(false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity = this;
        queue = Volley.newRequestQueue(LogInActivity.this, new HurlStack());

//        Version Control
        VersionManager versionManager = new VersionManager(getApplicationContext());
        if (!versionManager.runMigrations()) {
            // if migration has failed
            Toast.makeText(getApplicationContext(), "Migration Failed", Toast.LENGTH_SHORT).show();
        }

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        // Task to do when App runs for the first time
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task
            loginTime = "0";
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        else {
            String extraStr;
            try {
                extraStr = getIntent().getExtras().getString("status");
                if(extraStr.equals("1"))
                {
                    prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    loginTime = "1";
                    String testEmail = prefs.getString("email", "");
                    String testPassword = prefs.getString("password", "");
                    pdLoading.show();
                    makeLoginRequest(LogInActivity.this, testEmail, testPassword);
                    Toast.makeText(getApplicationContext(), "Fingerprint Success", Toast.LENGTH_SHORT).show();
                }
                else if(extraStr.equals("0")){
                    Toast.makeText(getApplicationContext(), "Fingerprint Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e ) {
                extraStr = "None";
                if(prefs.getString("fingerprint_activated", "").equals("1")){
                    displayfingerPrintDialogue();
                }
            }


//            Toast.makeText(getApplicationContext(), "Running Second Time : ", Toast.LENGTH_SHORT).show();
        }

        final SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(!sharedPreferences1.getString(VERIFIED,"").equals("1")){
            loginTime = "0";
        }
        else {
            loginTime = "1";
        }



// Get the extras (if there are any)


        // Getting Shared Prefrences if the fingerprint is activated



        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String testVerified = sharedPreferences.getString(VERIFIED,"");
//        Toast.makeText(getApplicationContext(), "Verified : " + testVerified, Toast.LENGTH_SHORT).show();
        if (sessionManager.isUserSignedIn()) {

//            Toast.makeText(getApplicationContext(), "Verified : " + sharedPreferences.getString(VERIFIED, ""), Toast.LENGTH_SHORT).show();


            // Need To Uncomment and Change
            if(!sharedPreferences.getString(VERIFIED,"").equals("1")){
                finish();
                startActivity(new Intent(getApplicationContext(), CodeConfirmation.class));
            }
            else {
                finish();
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));
            }
//           // Need To Remove
//            finish();
//            startActivity(new Intent(getApplicationContext(), Main2Activity.class));
        }




        // Finger Print Switch
        fingerPrintSwitch = (SwitchCompat) findViewById(R.id.fingerPrintSwitch);


//        else{
//
//            Toast.makeText(getApplicationContext(), "Not Signed In", Toast.LENGTH_SHORT).show();
//        }

//        Toast.makeText(getApplicationContext(), "Data: " + sessionManager.getKeyLoginEmail(), Toast.LENGTH_SHORT).show();
        loginButtonLoginScreen = (Button) findViewById(R.id.loginButtonLoginScreen);
        skipLogin = (Button) findViewById(R.id.skipLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bReset = (TextView) findViewById(R.id.bReset);
        etEmail.getBackground().clearColorFilter();
        etPassword.getBackground().clearColorFilter();


//        hardcoding number and password for development speedup purposes
        etEmail.setText("inventioncorepk@gmail.com");
        etPassword.setText("newnewnew321");

        loginButtonLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setError(null);
                etPassword.setError(null);
                Boolean emailVarified = true, passwordVarified = true;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

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
                if (emailVarified && passwordVarified) {
                    if(fingerPrintSwitch.isChecked()){
                        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
                        editor.putString("fingerprint_activated", "1");
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
//                        Toast.makeText(getApplicationContext(), "Fingerprint Activated: ", Toast.LENGTH_SHORT).show();
                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Fingerprint Not Activated: ", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
                        editor.putString("fingerprint_activated", "0");
                        editor.apply();
                    }
                    pdLoading.show();
                    makeLoginRequest(LogInActivity.this, email, password);
                }
            }
        });

        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Main2Activity.class));

            }
        });


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

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(VERIFIED, "0");
            editor.putString(FIRSTLOGIN, "0");
            editor.apply();
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    public void displayfingerPrintDialogue(){
        fingerMyDialog = new Dialog(LogInActivity.this);
        fingerMyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fingerMyDialog.setContentView(R.layout.customdialogue);
        fingerMyDialog.setTitle("Fingerprint Authentication");


        Button cancelFingerPrint = (Button) fingerMyDialog.findViewById(R.id.cancelFingerprint);
        Button fingerPrintButton = (Button)fingerMyDialog.findViewById(R.id.fingerprintButton);

        cancelFingerPrint.setEnabled(true);
        fingerPrintButton.setEnabled(true);

        cancelFingerPrint.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                fingerMyDialog.dismiss();
            }
        });

        fingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getString("fingerprint_activated", "").equals("1")){
                    Intent newintent=new Intent(LogInActivity.this,FingerprintAuthentication.class);
                    startActivityForResult(newintent, 2);
                }
                else if (prefs.getString("fingerprint_activated", "").equals("0")){
                    Toast.makeText(getApplicationContext(), "Fingerprint Login Is Not Activated", Toast.LENGTH_SHORT).show();
                    fingerMyDialog.dismiss();

                }
            }
        });

        fingerMyDialog.show();
    }


    @Override
    protected void onDestroy() {
        if (pdLoading != null && pdLoading.isShowing()) {
            pdLoading.dismiss();
        }
        super.onDestroy();
    }

    public void makeLoginRequest(final Activity activity, final String email, final String password) {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
//        Toast.makeText(getApplicationContext(), "Params" + email + password, Toast.LENGTH_SHORT).show();
        StringRequest sr = new StringRequest(Request.Method.POST, MyURLs.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse() makeLoginRequest called with: response = [" + response + "]");

//                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                pdLoading.dismiss();
                try {
                    if (pdLoading != null && pdLoading.isShowing()) {
                        pdLoading.dismiss();
                    }
                    JSONObject jObj = new JSONObject(response);
                    int responseCode = jObj.getInt("responseCode");

//                    Toast.makeText(getApplicationContext(), "Got Response" + responseCode, Toast.LENGTH_SHORT).show();
                    if (responseCode == 200) {

//                        Toast.makeText(getApplicationContext(), "Got Data", Toast.LENGTH_SHORT).show();
                        JSONObject jObjReponse = jObj.getJSONObject("response");
                        String access_token = jObjReponse.getString("access_token");
                        Log.d(TAG, "onResponse: access_token: " + access_token);
                        JSONObject jObjUser = jObjReponse.getJSONObject("user");
                        //parsing user object
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
                        Long iat = jObjUser.getLong("iat");

                        sessionManager.loginnUser(user_id, access_token, user_name, user_image, user_email, user_password, user_privatekey
                                                    , user_status, user_confirm, user_scope, wallet_address,fingerprint_auth, faceid_auth,
                                                    app_credit, created_at, updated_at, iat);

                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                        finish();
//                        startActivity(new Intent(LogInActivity.this, CodeConfirmation.class));
                        startActivity(new Intent(LogInActivity.this, Main2Activity.class));
                    }
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pdLoading != null && pdLoading.isShowing()) {
                    pdLoading.dismiss();
                }

//                Toast.makeText(activity, "Error" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                if (!NetworkAccess.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "Turn on wifi or Mobile Data", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (error.networkResponse != null) {
//                            Toast.makeText(getApplicationContext(), "Network Error" + error.networkResponse.statusCode + " : " + error.networkResponse, Toast.LENGTH_LONG).show();
                            if (error.networkResponse.statusCode == 401) {
                                Toast.makeText(activity, "Invalid email or password entered", Toast.LENGTH_SHORT).show();
                                updateSharedPrefrencesFalse();
                            }
                            else if(error.networkResponse.statusCode == 409){

//                                Toast.makeText(getApplicationContext(), "Email Not Verified, Please Verify", Toast.LENGTH_SHORT).show();
                                MyCustomAlertDialog();
                                updateSharedPrefrencesFalse();
                            }
                            else if(error.networkResponse.statusCode == 404){
                                Toast.makeText(getApplicationContext(), "User does not exists", Toast.LENGTH_SHORT).show();
                                updateSharedPrefrencesFalse();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Internet Connectivity might be poor", Toast.LENGTH_LONG).show();
                            updateSharedPrefrencesFalse();
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
                params.put("user[user_email]", email);
                params.put("user[user_password]", password);
                params.put("user[loginTime]", loginTime);

                Log.d(TAG, "Login getParams: " + params);
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
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", getBodyContentType());
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
        MyDialog = new Dialog(LogInActivity.this);
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

    public void updateSharedPrefrencesFalse(){
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString("fingerprint_activated", "1");
    }
}
