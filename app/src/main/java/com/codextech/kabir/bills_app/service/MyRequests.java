package com.codextech.kabir.bills_app.service;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequests {

    private static MyRequests mInstance;

    private RequestQueue requestQueue;
    private static Context mCtx;


    private MyRequests (Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MyRequests getmInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = new MyRequests(context);
        }
        return mInstance;
    }

    public <T>void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }


}
