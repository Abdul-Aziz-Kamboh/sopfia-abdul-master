package com.codextech.kabir.bills_app.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.sync.MyURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import de.halfbit.tinybus.TinyBus;
import eu.amirs.JSON;


public class InitService extends IntentService {
    public static final String TAG = "InitService";
    private int result = Activity.RESULT_CANCELED;
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.codextech.ibtisam.bills_app";

    private Context mContext;
    private static RequestQueue queue;
    AtomicInteger requestsCounter;
    private SessionManager sessionManager;
//    boolean initError = false;

    public InitService() {
        super("InitService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        queue = Volley.newRequestQueue(mContext);
        sessionManager = new SessionManager(getApplicationContext());
        requestsCounter = new AtomicInteger(0);
        return super.onStartCommand(intent, flags, startId);
    }

    // called asynchronously be Android
    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        //fetchMerchants();
        getUserData();
        //fetchTransactions();

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(TAG, "onRequestFinished: " + requestsCounter.get());
                requestsCounter.decrementAndGet();
                if (requestsCounter.get() == 0) {
                    Log.d(TAG, "onRequestFinished: +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    publishResults(result);
                }
            }
        });
    }

    private void getUserData(){

            Log.d(TAG, "fetchBPTransDataFunc: Fetching Transactions...");
            final int MY_SOCKET_TIMEOUT_MS = 60000;
            final String BASE_URL = MyURLs.GET_USER;
            Uri builtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("limit", "50000")
//                .appendQueryParameter("page", "1")
                    .build();
            final String myUrl = builtUri.toString();
            Log.d(TAG, "fetchTransactions: URL: " + myUrl);
            StringRequest sr1 = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "onResponse() makeLoginRequest called with: response = [" + response + "]");
//                pdLoading.dismiss();

                        JSON json = new JSON(response);

                    String user_id = json.key("response").key("user").key("user_id").stringValue();
                    String user_name = json.key("response").key("user").key("user_name").stringValue();
                    String user_image = json.key("response").key("user").key("user_img").stringValue();
                    String user_email = json.key("response").key("user").key("user_email").stringValue();
                    String user_privatekey = json.key("response").key("user").key("user_privatekey").stringValue();
                    String user_status = json.key("response").key("user").key("user_status").stringValue();
                    String user_confirm = json.key("response").key("user").key("user_confirm").stringValue();
                    String user_scope = json.key("response").key("user").key("user_scope").stringValue();
                    String wallet_address = json.key("response").key("user").key("wallet_address").stringValue();
                    String fingerprint_auth = json.key("response").key("user").key("fingerprint_auth").stringValue();
                    String faceid_auth = json.key("response").key("user").key("faceid_auth").stringValue();
                    String app_credit = json.key("response").key("user").key("app_credit").stringValue();
                    String created_at = json.key("response").key("user").key("created_at").stringValue();
                    String updated_at = json.key("response").key("user").key("updated_at").stringValue();
                    Long iat = json.key("response").key("user").key("iat").longValue();

//
                            sessionManager = new SessionManager(getApplicationContext());

                            sessionManager.getUser(user_id, user_name, user_email, user_image, user_privatekey,
                                    user_status, user_confirm, user_scope, wallet_address, fingerprint_auth, faceid_auth, app_credit, created_at, updated_at, iat);

//
//                          }

//                result = Activity.RESULT_OK;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: fetchBPMerchantsDataFunc" + error.networkResponse.statusCode);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Bearer " + sessionManager.getLoginToken());
                    return params;
                }
            };
            sr1.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(sr1);
            requestsCounter.incrementAndGet();
    }

    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}