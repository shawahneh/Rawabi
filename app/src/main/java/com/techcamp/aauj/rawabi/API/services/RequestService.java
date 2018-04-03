package com.techcamp.aauj.rawabi.API.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.techcamp.aauj.rawabi.API.VolleySingleton;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

import org.json.JSONObject;

import java.util.Map;


public abstract class RequestService<T> {
    public RequestService(Context mContext, String url, ICallBack<T> mCallBack) {
        this.mContext = mContext;
        this.url = url;
        this.mCallBack = mCallBack;

        getAll();
    }

    private Context mContext;
    private String url;
    private ICallBack<T> mCallBack;
    private StringRequest request;
    public void getAll(){
        //...
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    deliverResponse(parseResponse(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace();
                    deliverError(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getParameters();
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }
    public abstract Map<String,String> getParameters();
    public abstract T parseResponse(JSONObject Response);
    public void cancel(){
        if(request != null)
            request.cancel();
        mCallBack = null;
    }
    private void finish(){mCallBack = null;}

    protected void deliverResponse(T response){
        if(mCallBack != null){
            mCallBack.onResponse(response);
            finish();
        }
    }
    protected void deliverError(String err){
        if(mCallBack != null){
            mCallBack.onError(err);
            finish();
        }
    }

}
