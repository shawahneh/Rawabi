package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi,AnnouncmentWebApi,AuthWebApi ,
PoolingJourney{
    public String apiUrl = "https://tcamp.000webhostapp.com/api/index.php";
    RequestQueue requestQueue;
    private static WebApi instance;
    private Context mContext;

    private WebApi(Context context) {
        mContext = context;
    }
    public static WebApi getInstance(Context context) {
        if (instance == null)
            instance = new WebApi(context);
        return instance;
    }

    private void send(final Map<String,String> params, final ITriger<JSONObject> result, final ITriger<VolleyError> errorResponse)
    {
        Log.d("tag","send");

        if (requestQueue==null)
        {

             requestQueue = Volley.newRequestQueue(mContext);
        }
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tag","ERROR "+ error.toString());
                errorResponse.onTriger(error);
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



    public Boolean isLogin(){

        if(mContext != null) {
            SharedPreferences sp = mContext.getSharedPreferences("db", Context.MODE_PRIVATE);
            return sp.getBoolean("login", false);
        }
        return false;
    }
    @Nullable
    public User getLocalUser(){
        if(mContext == null)
            return null;
        SharedPreferences sp = mContext.getSharedPreferences("db",Context.MODE_PRIVATE);

        String username = sp.getString("username",null);
        String password = sp.getString("password",null);
        int id = sp.getInt("id",-1);
        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setId(id);

        if(username == null)
            return null;
        return user;
    }

    @Override
    public void userRegister(User user, final ITriger<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();

        //($username,$password,$fullname,$gender,$birthdate,$address,$userType,$image,$phone)
        params.put("action","userRegister");
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        params.put("fullname",user.getFullname());
        params.put("gender",user.getGender()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
        params.put("birthdate",simpleDateFormat.format(user.getBirthdate()));
        params.put("address",user.getAddress());
        params.put("userType","1");
        params.put("image",user.getImageurl());
        params.put("phone",user.getPhone());
        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {
                    if (value.getString("registration").equals("success")){
                        Log.i("tag", "register process is done");
                        booleanITriger.onTriger(true);
                    }else
                    {
                        Log.i("tag", "register process is failed");
                        booleanITriger.onTriger(false);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    booleanITriger.onTriger(false);
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.i("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }

    @Override
    public void setUserDetails(User user, String OldPassword, ITriger<Boolean> booleanITriger) {

    }
    // if userId <= 0 then give me the details of current user
    @Override
    public void getUserDetails(int userId, final ITriger<User> resultUser) {
        final User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getUserDetails");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");

        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {

                    if (value.has("username") && !value.isNull("username")){

                        User userDetails = new User();
                        userDetails.setUsername(localUser.getUsername());
                        userDetails.setPassword(localUser.getPassword());
                        userDetails.setFullname(value.getString("fullname"));
                        userDetails.setAddress(value.getString("address"));
                        userDetails.setPhone(value.getString("phone"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
                        userDetails.setBirthdate(simpleDateFormat.parse(value.getString("birthdate")));
                        userDetails.setGender(value.getInt("gender"));
                        userDetails.setId(value.getInt("id"));
                        userDetails.setImageurl(value.getString("image"));
                        Log.i("tag", "Getting user details for user : "+userDetails.getUsername());
                        resultUser.onTriger(userDetails);
                    }else
                    {

                        Log.i("tag", "Getting user details");
                        resultUser.onTriger(null);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    resultUser.onTriger(null);
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.d("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }


    @Override
    public void checkAuth(String username, String password, final ITriger<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {
                    if (value.getString("auth").equals("true")){

                        Log.i("tag", "user auth is ok");
                        booleanITriger.onTriger(true);
                    }else
                    {

                        Log.i("tag", "user auth is not ok");
                        booleanITriger.onTriger(false);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    booleanITriger.onTriger(false);
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.d("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }


    @Override
    public void getJourneys(int userId, int limitStart, int limitNum, ITriger<ArrayList<Journey>> journeys) {

    }

    @Override
    public void getJourneyDetails(int id, ITriger<Journey> journey) {

    }

    @Override
    public void setNewJourney(Journey newJourney, final ITriger<Integer> journeyId) {
        User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","setNewJourney");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("startLocationX",newJourney.getStartPoint().latitude+"");
        params.put("startLocationY",newJourney.getStartPoint().longitude+"");
        params.put("endLocationX",newJourney.getEndPoint().latitude+"");
        params.put("endLocationY",newJourney.getEndPoint().longitude+"");
        params.put("goingDate",newJourney.getGoingDate().toString());
        params.put("seats",newJourney.getSeats()+"");
        params.put("genderPrefer",newJourney.getGenderPrefer()+"");
        params.put("carDescription",newJourney.getCarDescription());

        send(params, new ITriger<JSONObject>() {
            @Override
            public void onTriger(JSONObject value) {
                try {
                    int id = Integer.parseInt(value.getString("status"));
                    if (id>0){

                        Log.i("tag", "Creating Journey Process Done With id : "+id);
                        journeyId.onTriger(id);
                    }else
                    {

                        Log.i("tag", "Creating Journey Process Failed");
                        journeyId.onTriger(-1);
                    }
                } catch (JSONException e) {
                    Log.i("tag","Error on JSON getting item");
                    journeyId.onTriger(-1);
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new ITriger<VolleyError>() {
            @Override
            public void onTriger(VolleyError value) {
                Log.d("tag", "Error while getting data from send() method ");
                value.printStackTrace();
            }
        });
    }


    @Override
    public void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate,int sortBy, ITriger<ArrayList<Journey>> Journeys) {
        ArrayList<Journey> journeys = new ArrayList<>();


        Journey j = new Journey();
        j.setGoingDate(new Date());
        j.setStartPoint(startPoint);
        j.setEndPoint(endPoint);
        User u = new User();
        u.setFullname("ALA AMARNEH");
        u.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        j.setUser(u);

        journeys.add(j );



        Journey journey = new Journey();
        journey.setGoingDate(new Date());
        journey.setStartPoint(new LatLng(32.01183468173907,35.18930286169053));
        journey.setEndPoint(endPoint);
        User user = new User();
        user.setFullname("ALA AMARNEH");
        user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        journey.setUser(user);

        journeys.add(journey );

        Journey j2 = new Journey();
        j2.setGoingDate(new Date());
        j2.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
        j2.setEndPoint(endPoint);
        User user2 = new User();
        user.setFullname("Moh AMARNEH");
        user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        j2.setUser(user2);
        journeys.add(j2 );

        Journey j3 = new Journey();
        j3.setGoingDate(new Date());
        j3.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
        j3.setEndPoint(endPoint);
        User user3 = new User();
        user.setFullname("Moh sfdfdsf");
        user.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        j3.setUser(user3);
        journeys.add(j3 );

        Journeys.onTriger(journeys);
    }


}
