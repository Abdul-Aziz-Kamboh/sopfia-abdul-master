package com.codextech.kabir.bills_app.sync;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codextech.kabir.bills_app.SessionManager;
import com.codextech.kabir.bills_app.utils.NetworkAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.halfbit.tinybus.TinyBus;

public class DataSenderAsync {
    public static final String TAG = "DataSenderAsync";
//    private static final String TAG = "AppInitializationTest";

    private static DataSenderAsync instance = null;
    private static int currentState = 1;
    private static final int IDLE = 1;
    private static final int PENDING = 2;
    private static boolean firstThreadIsRunning = false;
    private SessionManager sessionManager;
    private Context mContext;
    private long totalSize = 0;
    private static RequestQueue queue;
    private final int MY_TIMEOUT_MS = 30000;
    private final int MY_MAX_RETRIES = 0;


    protected DataSenderAsync(Context context) {
        Log.d(TAG, "DataSenderAsync: ==========================================================================================================================");
        mContext = context;
        queue = Volley.newRequestQueue(mContext);
        firstThreadIsRunning = false;
    }

    public static DataSenderAsync getInstance(Context context) {
        final Context appContext = context.getApplicationContext();
        if (instance == null) {
//            synchronized ((Object) firstThreadIsRunning){
            if (!firstThreadIsRunning) {
                firstThreadIsRunning = true;
                instance = new DataSenderAsync(appContext);
            }
//            }
        }
        return instance;
    }

    public void run() {
        Log.d(TAG, "run: ");
        if (currentState == IDLE) {
            currentState = PENDING;
            Log.d(TAG, "run: InsideRUNING" + this.toString());
            queue.setmAllFinishedListener(new RequestQueue.AllFinishedListener() {
                @Override
                public void onAllFinished() {
                    currentState = IDLE;
                    Log.d(TAG, "onRequestFinished: EVERYTHING COMPLETED");
                }
            });
            new AsyncTask<Object, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    sessionManager = new SessionManager(mContext);
                    Log.d(TAG, "DataSenderAsync onPreExecute: ");
                }

                @Override
                protected Void doInBackground(Object... params) {
                    try {
                        if (NetworkAccess.isNetworkAvailable(mContext)) {
                            if (sessionManager.isUserSignedIn() && sessionManager.getCanSync()) {
                                Log.d(TAG, "Syncing");
                                Log.d(TAG, "Token : " + sessionManager.getLoginToken());
                                Log.d(TAG, "user_id : " + sessionManager.getKeyLoginId());
                                Log.d(TAG, "Syncing");
                            } else {
                                Toast.makeText(mContext, ".", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "SyncNoInternet");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "SyncingException: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Void... values) {
                    super.onProgressUpdate(values);
                }

                //this method is executed when doInBackground function finishes
                @Override
                protected void onPostExecute(Void result) {
                    queue.isIdle();
                }
            }.execute();
        } else {
            Log.d(TAG, "run: NotRunning");
        }
    }

}