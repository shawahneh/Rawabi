package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

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
import com.techcamp.aauj.rawabi.Beans.MyPlace;
import com.techcamp.aauj.rawabi.Beans.Ride;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.IResponeTriger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alaam on 11/15/2017.
 */

public class WebApi implements CalendarWebApi,AnnouncmentWebApi,AuthWebApi ,
PoolingJourney,PoolingRides,PoolingPlace{
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

    private void send(final Map<String,String> params, final IResponeTriger<JSONObject> result)
    {
        Log.d("tagWebApi","send");

        if (requestQueue==null)
        {

             requestQueue = Volley.newRequestQueue(mContext);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("tagWebApi","Response : "+ response);
                Gson gson = new Gson();
                try {
//                JSONObject jsonObject = gson.fromJson(response,JSONObject.class);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("tagWebApi",jsonObject.toString());
                    result.onResponse(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tagWebApi","ERROR "+ error.toString());
                result.onError(error.toString());
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

    //DONE
    @Override
    public void userRegister(User user, final IResponeTriger<Boolean> booleanITriger) {
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
        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if (value.getString("registration").equals("success")){
                        Log.i("tagWebApi", "register process is done");
                        booleanITriger.onResponse(true);
                    }else
                    {
                        Log.i("tagWebApi", "register process is failed");
                        booleanITriger.onResponse(false);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    booleanITriger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.i("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                booleanITriger.onError(err);
            }

        });
    }

    @Override
    public void setUserDetails(User user, String OldPassword, IResponeTriger<Boolean> booleanITriger) {

    }

    //DONE
    // if userId <= 0 then give me the details of current user
    // if username = null give me the current user
    // if username = null gime me the current user
    @Override
    public void getUserDetails(int userId, final IResponeTriger<User> resultUser) {


        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getUserDetails");
        final User localUser = getLocalUser();
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");

        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
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
                        Log.i("tagWebApi", "Getting user details for user : "+userDetails.getUsername());
                        resultUser.onResponse(userDetails);
                    }else
                    {

                        Log.i("tagWebApi", "Getting user details");
                        resultUser.onResponse(null);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    resultUser.onError(e.toString());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    resultUser.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                resultUser.onError(err);
            }
        });
    }

    //DONE
    @Override
    public void login(final String username, final String password, final IResponeTriger<User> resultUser) {
        Map<String,String> params = new HashMap<String, String>();
        Log.i("tagWebApi", "login: u: "+username+" p: "+password);
        params.put("action","getUserDetails");
        params.put("username",username);
        params.put("password",password);
        params.put("userId","-1");

        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if (value.has("username")){

                        User userDetails = new User();
                        userDetails.setUsername(username);
                        userDetails.setPassword(password);
                        userDetails.setFullname(value.getString("fullname").toString());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
                        userDetails.setBirthdate(simpleDateFormat.parse(value.getString("birthdate")));
                        userDetails.setGender(value.getInt("gender"));
                        userDetails.setId(value.getInt("id"));
                        userDetails.setAddress(value.getString("address").toString());
                        userDetails.setPhone(value.getString("phone").toString());
                        userDetails.setImageurl(value.getString("image").toString());
                        Log.i("tagWebApi", "Getting user details for user : "+userDetails.getUsername());
                        resultUser.onResponse(userDetails);
                    }else
                    {

                        Log.i("tagWebApi", "Getting user details failed");
                        resultUser.onResponse(null);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    resultUser.onError(e.toString());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    resultUser.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                resultUser.onError(err);
            }
        });
    }

    //DONE
    @Override
    public void checkAuth(String username, String password, final IResponeTriger<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if (value.getString("auth").equals("true")){

                        Log.i("tagWebApi", "user auth is ok");
                        booleanITriger.onResponse(true);
                    }else
                    {

                        Log.i("tagWebApi", "user auth is not ok");
                        booleanITriger.onResponse(false);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    booleanITriger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                booleanITriger.onError(err);
            }
        });
    }

    //DONE
    @Override
    public void getJourneys(int userId, int limitStart, int limitNum, final IResponeTriger<ArrayList<Journey>> journeys) {
        User localUser = getLocalUser();
        // if the userId <= 0 then get the logged in user
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getJourneys");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");
        params.put("start",limitStart+"");
        params.put("num",limitNum+"");

        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if (value.has("journeys"))
                    {
                        JSONArray JArr = value.getJSONArray("journeys");
                        JSONObject temp;
                        Journey Jtemp;
                        ArrayList<Journey> objArr = new ArrayList<Journey>();
                        for (int i=0;i<JArr.length();i++)
                        {
                            temp = JArr.getJSONObject(i);
                             Jtemp = new Journey();
                             Jtemp.setId(temp.getInt("id"));
                             Jtemp.setStartPoint(new LatLng(temp.getDouble("startLocationX"),temp.getDouble("startLocationY")));
                             Jtemp.setEndPoint(new LatLng(temp.getDouble("endLocationX"),temp.getDouble("endLocationY")));
                              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                             Jtemp.setGoingDate(simpleDateFormat.parse(temp.getString("goingDate")));
                             Jtemp.setSeats(temp.getInt("seats"));
                             Jtemp.setGenderPrefer(temp.getInt("genderPrefer"));
                             Jtemp.setCarDescription(temp.getString("carDescription"));
                              JSONObject jsonUser = temp.getJSONObject("user");
                              User tempUser = new User();
                              tempUser.setUsername(jsonUser.getString("username"));
                              tempUser.setFullname(jsonUser.getString("fullname"));
                              tempUser.setId(jsonUser.getInt("id"));
                              tempUser.setGender(jsonUser.getInt("gender"));
                              tempUser.setPhone(jsonUser.getString("phone"));
                              tempUser.setAddress(jsonUser.getString("address"));
                              SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYYY-MM-DD");
                              tempUser.setBirthdate(simpleDateFormat2.parse(jsonUser.getString("birthdate")));
                              Jtemp.setUser(tempUser);
                                objArr.add(Jtemp);

                        }
                        journeys.onResponse(objArr);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    journeys.onError(e.toString());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    journeys.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journeys.onError(err);
            }
        });
    }

    @Override
    public void getJourneyDetails(int id, IResponeTriger<Journey> journey) {

    }

    //DONE
    @Override
    public void setNewJourney(Journey newJourney, final IResponeTriger<Integer> journeyId) {
        User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        params.put("action","setNewJourney");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("startLocationX",newJourney.getStartPoint().latitude+"");
        params.put("startLocationY",newJourney.getStartPoint().longitude+"");
        params.put("endLocationX",newJourney.getEndPoint().latitude+"");
        params.put("endLocationY",newJourney.getEndPoint().longitude+"");
        params.put("goingDate",df.format(newJourney.getGoingDate())+"");
        params.put("seats",newJourney.getSeats()+"");
        params.put("genderPrefer",newJourney.getGenderPrefer()+"");
        params.put("carDescription",newJourney.getCarDescription());

        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    int id = Integer.parseInt(value.getString("status"));
                    if (id>0){

                        Log.i("tagWebApi", "Creating Journey Process Done With id : "+id);
                        journeyId.onResponse(id);
                    }else
                    {

                        Log.i("tagWebApi", "Creating Journey Process Failed");
                        journeyId.onError("Creating Journey Process Failed");
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    journeyId.onError(e.toString());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    journeyId.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journeyId.onError(err);
            }
        });
    }


    @Override
    public void filterJourneys(LatLng startPoint, LatLng endPoint, Date goingDate,int sortBy, IResponeTriger<ArrayList<Journey>> Journeys) {
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
        user.setPhone("0592355");
        journey.setUser(user);

        journeys.add(journey );

        Journey j2 = new Journey();
        j2.setGoingDate(new Date());
        j2.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
        j2.setEndPoint(endPoint);
        User user2 = new User();
        user2.setFullname("Moh AMARNEH");
        user2.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        j2.setUser(user2);
        journeys.add(j2 );

        Journey j3 = new Journey();
        j3.setGoingDate(new Date());
        j3.setStartPoint(new LatLng(32.01305201874965,35.19094504415989));
        j3.setEndPoint(endPoint);
        User user3 = new User();
        user3.setFullname("Moh sfdfdsf");
        user3.setImageurl("https://scontent.fjrs2-1.fna.fbcdn.net/v/t1.0-9/23376279_1508595089223011_6837471793707392618_n.jpg?oh=2d620ecf5841f11c2a550b75a2fbb650&oe=5A990C1E");

        j3.setUser(user3);
        journeys.add(j3 );

        Journeys.onResponse(journeys);
    }


    @Override
    public void getPlaces(IResponeTriger<List<MyPlace>> listIResponeTriger) {
        // TODO: 12/7/2017
        // Dummy places
        ArrayList<MyPlace> places = new ArrayList<>();
        for (int i =0;i<10;i++){
        MyPlace place = new MyPlace();
        place.setName("place #"+i);
        place.setSummery("s#"+i);
        place.setImageurl(null);

        places.add(place);
        }
        listIResponeTriger.onResponse(places);
    }

    //DONE
    @Override
    public void getRides(int userId, int limitStart, int limitNum, final IResponeTriger<ArrayList<Ride>> rides) {
        User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRides");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");
        params.put("start",limitStart+"");
        params.put("num",limitNum+"");

        send(params, new IResponeTriger<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if (value.has("rides"))
                    {
                        JSONArray JArr = value.getJSONArray("rides");
                        JSONObject temp;
                        Ride Rtemp;
                        ArrayList<Ride> objArr = new ArrayList<Ride>();
                        for (int i=0;i<JArr.length();i++)
                        {
                            temp = JArr.getJSONObject(i);
                            Rtemp = new Ride();
                            Rtemp.setId(temp.getInt("id"));
                            Rtemp.setMeetingLocation(new LatLng(temp.getDouble("meetingLocationX"),temp.getDouble("meetingLocationY")));
                            Rtemp.setOrderStatus(temp.getInt("orderStatus"));

                                JSONObject jsonUser = temp.getJSONObject("user");
                                User tempUser = new User();
                                tempUser.setUsername(jsonUser.getString("username"));
                                tempUser.setFullname(jsonUser.getString("fullname"));
                                tempUser.setId(jsonUser.getInt("id"));
                                tempUser.setGender(jsonUser.getInt("gender"));
                                tempUser.setPhone(jsonUser.getString("phone"));
                                tempUser.setAddress(jsonUser.getString("address"));
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYYY-MM-DD");
                                tempUser.setBirthdate(simpleDateFormat2.parse(jsonUser.getString("birthdate")));
                            Rtemp.setUser(tempUser);
                                JSONObject jsonJourney = temp.getJSONObject("journey");
                                    Journey Jtemp = new Journey();
                                    Jtemp.setId(jsonJourney.getInt("id"));
                                    Jtemp.setStartPoint(new LatLng(jsonJourney.getDouble("startLocationX"),jsonJourney.getDouble("startLocationY")));
                                    Jtemp.setEndPoint(new LatLng(jsonJourney.getDouble("endLocationX"),jsonJourney.getDouble("endLocationY")));
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                                    Jtemp.setGoingDate(simpleDateFormat.parse(jsonJourney.getString("goingDate")));
                                    Jtemp.setSeats(jsonJourney.getInt("seats"));
                                    Jtemp.setGenderPrefer(jsonJourney.getInt("genderPrefer"));
                                    Jtemp.setCarDescription(jsonJourney.getString("carDescription"));
                                    JSONObject jsonJUser = jsonJourney.getJSONObject("user");
                                    User tempJUser = new User();
                                tempJUser.setUsername(jsonJUser.getString("username"));
                                tempJUser.setFullname(jsonJUser.getString("fullname"));
                                tempJUser.setId(jsonJUser.getInt("id"));
                                tempJUser.setGender(jsonJUser.getInt("gender"));
                                tempJUser.setPhone(jsonJUser.getString("phone"));
                                tempJUser.setAddress(jsonJUser.getString("address"));
                                    SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("YYYY-MM-DD");
                                tempJUser.setBirthdate(simpleDateFormat3.parse(jsonUser.getString("birthdate")));
                                    Jtemp.setUser(tempJUser);
                            Rtemp.setJourney(Jtemp);
                            objArr.add(Rtemp);

                        }
                        rides.onResponse(objArr);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    rides.onError(e.toString());
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    rides.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                rides.onError(err);
            }
        });
    }

    @Override
    public void getRideDetails(int rideId, IResponeTriger<Ride> ride) {

    }

    @Override
    public void setRideOnJourney(Ride newRide, IResponeTriger<Integer> rideId) {

    }

    @Override
    public void changeRideStatus(int rideId, int status, IResponeTriger<Boolean> result) {

    }
}
