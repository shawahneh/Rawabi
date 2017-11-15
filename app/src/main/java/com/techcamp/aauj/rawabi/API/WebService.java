package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("tag","Response : "+ response);
                Gson gson = new Gson();
                try {
//                JSONObject jsonObject = gson.fromJson(response,JSONObject.class);
                JSONObject jsonObject = new JSONObject(response);
                Log.d("tag",jsonObject.toString());
                result.onTriger(jsonObject);

                    Toast.makeText(context, jsonObject.getString("auth") +"", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(context, "Error get auth", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag","ERROR "+ error.toString());
                errorResponse.onTriger(error);
                Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> paramss = new HashMap<String, String>();
//                paramss.put("action","userAuth");
//                paramss.put("username","driver1");
//                paramss.put("password","driver1");
//                return paramss;
                return params;
            }
        };
        requestQueue.add(stringRequest);
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
                Log.d("tag","On userAuth : " + value.toString());
                try {
                    Toast.makeText(context, value.getString("auth").toString(), Toast.LENGTH_SHORT).show();
                    if (value.getString("auth").equals("true")){

                        result.onTriger(true);
                    }else
                    {
                        result.onTriger(false);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
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
