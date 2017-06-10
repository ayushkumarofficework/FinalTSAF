package com.example.federation.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HP-PC on 14-05-2017.
 */

public class MyVolleyClass {
    private static MyVolleyClass myVolleyClass;
    private static Context context;
    private RequestQueue requestQueue;
    public MyVolleyClass(Context context){
        this.context=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized MyVolleyClass getInstance(Context context){
        if(myVolleyClass==null){
            myVolleyClass=new MyVolleyClass(context);
        }
        return myVolleyClass;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
