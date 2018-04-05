package com.techcamp.aauj.rawabi.API.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.techcamp.aauj.rawabi.API.OfflineApi;
import com.techcamp.aauj.rawabi.API.VolleySingleton;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ALa on 4/4/2017.
 */


public abstract class RequestService<T> extends Request<T> {

    private Context mContext;
    /** Lock to guard mListener as it is cleared on cancel() and read on delivery. */
    private final Object mLock = new Object();

    // @GuardedBy("mLock")
    private ICallBack<T> mListener;
    private boolean saveOffline = false;
    private String codeOffline;
    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the T response
     */
    public RequestService(Context context,int method, String url, ICallBack<T> listener) {
        super(method, url,  null /* we override deliverError so we handled the error at the method*/);
        mListener = listener;
        this.mContext = context;
    }

    /**
     * Creates a new POST request
     *
     * @param listener Listener to receive the T response
     */
    public RequestService(Context context,String url,ICallBack<T> listener) {
        this(context,Method.POST, url, listener);
    }
    @Override
    public void deliverError(VolleyError error) {
        ICallBack<T> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onError(error.getMessage());
        }
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return getParameters();
    }
    public abstract Map<String,String> getParameters();

    @Override
    public void cancel() {
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }
    public abstract T parseResponse(String Response);
    @Override
    protected void deliverResponse(T response) {
        ICallBack<T> listener;
        synchronized (mLock) {
            listener = mListener;
        }
        if (listener != null) {
            listener.onResponse(response);
        }
    }
    public RequestService<T> start(){
        VolleySingleton.getInstance(mContext).addToRequestQueue(this);
        return this;
    }
    public RequestService<T> saveOffline(String code){
        saveOffline = true;
        codeOffline = code;
        return this;
    }
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            if(saveOffline)
                OfflineApi.setData(mContext,codeOffline,new JSONObject(jsonString));

            T result = parseResponse(jsonString);
            if(result == null)
                return Response.error(new ParseError(new Exception("null parsing")));
            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

}
