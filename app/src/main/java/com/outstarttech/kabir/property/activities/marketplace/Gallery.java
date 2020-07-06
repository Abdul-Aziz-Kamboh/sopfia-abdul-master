package com.outstarttech.kabir.property.activities.marketplace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.inventioncore.kabir.sopfia.R;
import com.outstarttech.kabir.property.activities.Main2Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class Gallery extends AppCompatActivity {


    private SessionManager sessionManager;

    private static RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("S0PFiA Gallery");
        }

        queue = Volley.newRequestQueue(Gallery.this, new HurlStack());

        sessionManager = new SessionManager(this);

        loadProducts();


    }

    public void loadProducts()
    {

        final int MY_SOCKET_TIMEOUT_MS = 60000;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Products...");
        progressDialog.show();


        //Toast.makeText(InitService.this, "Transactions Got: ", Toast.LENGTH_SHORT).show();

        StringRequest sr = new StringRequest(Request.Method.GET, MyURLs.GET_PRODUCTS, new Response.Listener<String>() {
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
                        JSONArray jObjReponse = jObj.getJSONArray("response");

                        final ListView listView;
                        listView = findViewById(R.id.product_list);
                        final ArrayList idArrayList = new ArrayList();
                        final ArrayList nameArrayList = new ArrayList();
                        final ArrayList desArrayList = new ArrayList();
                        final ArrayList priceArrayList = new ArrayList();
                        final ArrayList imgArrayList = new ArrayList();
                        final ArrayList watermarkedArrayList = new ArrayList();

                        for(int i=0; i<jObjReponse.length(); i++){
                            idArrayList.add(jObjReponse.getJSONObject(i).get("product_id").toString());
                            nameArrayList.add(jObjReponse.getJSONObject(i).get("product_name").toString());
                            desArrayList.add(jObjReponse.getJSONObject(i).get("product_description").toString());
                            priceArrayList.add(jObjReponse.getJSONObject(i).get("product_price").toString());
                            imgArrayList.add(jObjReponse.getJSONObject(i).get("product_url").toString());
                            watermarkedArrayList.add(jObjReponse.getJSONObject(i).get("product_watermarked").toString());


                            String mId[] = GetStringArray(idArrayList);
                            String mName[] = GetStringArray(nameArrayList);
                            String mDes[] = GetStringArray(desArrayList);
                            String mPrice[] = GetStringArray(priceArrayList);
                            String mImage[] = GetStringArray(imgArrayList);
                            String mWater[] = GetStringArray(watermarkedArrayList);

                            ProductsAdapter adapter = new ProductsAdapter(Gallery.this, mId, mName,mDes, mPrice, mImage, mWater);
                            listView.setAdapter(adapter);
                        }

//                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Getting Products Failed, Try Again Later", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Getting Products Failed" + error.getMessage() + error.networkResponse.toString(), Toast.LENGTH_SHORT).show();
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

    public static String[] GetStringArray(ArrayList<String> arr) {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // Convert ArrayList to object array
        Object[] objArr = arr.toArray();

        // Iterating and converting to String
        int i = 0;
        for (Object obj : objArr) {
            str[i++] = (String) obj;
        }

        return str;
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
