package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techcamp.aauj.rawabi.ITriger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 11/15/2017.
 */

public class WebService {
    public String apiUrl = "https://tcamp.000webhostapp.com/api/index.php";
    RequestQueue requestQueue;
    Context context;
    public WebService(Context context){
       this.context = context;

    }
    private void send(final Map<String,String> params, final ITriger<JSONObject> result, final ITriger<VolleyError> errorResponse)
    {
        Log.d("tag","send");

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                result.onTriger(response);
                Log.d("tag","response");
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse.onTriger(error);
                Log.d("tag","error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
    public void getAuth(String username, String password, final ITriger<Boolean> result){
        Log.d("tag","getAuth");
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                Log.d("tag","" + value.toString());
                try {
                    if (value.get("auth").equals("true")){
                        result.onTriger(true);
                    }else
                    {
                        result.onTriger(false);
                    }
                } catch (JSONException e) {
                    result.onTriger(false);
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                value.printStackTrace();
            }
        });
    }
}
