package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.techcamp.aauj.rawabi.Beans.DriverUser;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.RiderUser;
import com.techcamp.aauj.rawabi.ITriger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi,AnnouncmentWebApi,AuthWebApi {
    private static WebApi instance;
    private Context mContext;

    private WebApi(Context context) {
        mContext = context;
    }
    private WebApi() {
    }
    public static WebApi getInstance() {
        if (instance == null)
            instance = new WebApi();
        return instance;
    }

    @Override
    public void getEventAtDate(Date date, ITriger<ArrayList<Event>> eventITriger) {
        ArrayList<Event> dummyEvents = new ArrayList<>();
        Event event = new Event();
        event.setDate(date);
        event.setName("Event name");
        event.setDescription("Description ...");
        event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);

        eventITriger.onTriger(dummyEvents);
    }

    @Override
    public void getAnnouns(ITriger<ArrayList<Event>> eventITriger) {
        ArrayList<Event> dummyEvents = new ArrayList<>();
        Event event = new Event();
        event.setDate(new Date());
        event.setName("Event name");
        event.setDescription("Description ...");
        event.setImageUrl("http://edugate.aauj.edu/faces/javax.faces.resource/images/logo1.png?ln=demo");

        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);
        dummyEvents.add(event);

        eventITriger.onTriger(dummyEvents);
    }

    @Override
    public void RiderRegister(RiderUser user, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void DriverRegister(DriverUser user, ITriger<Boolean> booleanITriger) {

    }

    @Override
    public void checkAuth(String username, String pass, ITriger<Boolean> booleanITriger) {
//        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.i("tag","Response : "+ response);
//                Gson gson = new Gson();
//                JSONObject jsonObject = gson.fromJson(response,JSONObject.class);
//                try {
//                    Toast.makeText(context, jsonObject.getString("auth") +"", Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    Toast.makeText(context, "Error get auth", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i("tag","ERROR "+ error.toString());
//                errorResponse.onTriger(error);
//                Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
////                Map<String,String> paramss = new HashMap<String, String>();
////                paramss.put("action","userAuth");
////                paramss.put("username","driver1");
////                paramss.put("password","driver1");
////                return paramss;
//                return params;
//            }
//        };
//        requestQueue.add(stringRequest);
//
//    }

    }
}
