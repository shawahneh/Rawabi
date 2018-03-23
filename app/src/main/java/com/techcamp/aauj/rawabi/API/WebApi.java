package com.techcamp.aauj.rawabi.API;

import android.content.Context;
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
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.Announcement;
import com.techcamp.aauj.rawabi.model.Event;
import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.Journey;
import com.techcamp.aauj.rawabi.model.MediaItem;
import com.techcamp.aauj.rawabi.model.Ride;
import com.techcamp.aauj.rawabi.model.Transportation;
import com.techcamp.aauj.rawabi.model.TransportationElement;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.controllers.SPController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ALa on 11/15/2017.
 */

/**
 * Implementations using volley
 */
public class WebApi implements BasicApi,AuthWebApi
    ,CarpoolApi{
    public String apiUrl = "https://tcamp.000webhostapp.com/api/index.php";
    RequestQueue requestQueue;
    private static WebApi instance;
    private Context mContext;

    
    private WebApi(Context context) {
        mContext = context;
    }
    public static void init(Context context){
        if (instance == null)
            instance = new WebApi(context);
    }
    public static WebApi getInstance() {
        return instance;
    }

    private void send(final Map<String,String> params, final ICallBack<JSONObject> result)
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
                try {
//                JSONObject jsonObject = gson.fromJson(response,JSONObject.class);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("tagWebApi",jsonObject.toString());
                    result.onResponse(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                    result.onError(e.getMessage());
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

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    public Boolean isLogin() {
        return mContext != null && SPController.getLocalUser(mContext) != null;
    }
    @Nullable
    public User getLocalUser(){
        if(mContext == null)
            return null;
        return SPController.getLocalUser(mContext);
    }

    //DONE
    @Override
    public void userRegister(User user, final ICallBack<Boolean> booleanITriger) {
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
        send(params, new ICallBack<JSONObject>() {
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

    // Done By Maysara
    @Override
    public void setUserDetails(User user, String OldPassword, final ICallBack<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();

        params.put("action","setUserDetails");
        params.put("username",user.getUsername());
        params.put("password",OldPassword);
        params.put("fullname",user.getFullname());
        params.put("gender",user.getGender()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
        params.put("birthdate",simpleDateFormat.format(user.getBirthdate()));
        params.put("address",user.getAddress());
        params.put("image",user.getImageurl());
        params.put("phone",user.getPhone());
        params.put("newPassword" , user.getPassword());
        params.put("oldPassword" , OldPassword);

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject item) {
                try {
                    if (item.getString("status").equals("success")){
                        Log.i("tagWebApi", "setUserDetails process is done");
                        booleanITriger.onResponse(true);
                    }else
                    {
                        Log.i("tagWebApi", "setUserDetails process is failed");
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

    //DONE
    // if userId <= 0 then give me the details of current user
    // if username = null give me the current user
    // if username = null gime me the current user
    @Override
    public void getUserDetails(int userId, final ICallBack<User> resultUser) {


        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getUserDetails");
        final User localUser = getLocalUser();
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");

        send(params, new ICallBack<JSONObject>() {
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
    public void login(final String username, final String password, final ICallBack<User> resultUser) {
        Map<String,String> params = new HashMap<String, String>();
        Log.i("tagWebApi", "login: u: "+username+" p: "+password);
        params.put("action","getUserDetails");
        params.put("username",username);
        params.put("password",password);
        params.put("userId","-1");

        send(params, new ICallBack<JSONObject>() {
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
    public void checkAuth(String username, String password, final ICallBack<Boolean> booleanITriger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new ICallBack<JSONObject>() {
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
    public void getJourneys(int userId, int limitStart, int limitNum, final IListCallBack<Journey> journeys) {
        User localUser = getLocalUser();
        // if the userId <= 0 then get the logged in user
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getJourneys");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");
        params.put("start",limitStart+"");
        params.put("num",limitNum+"");

        send(params, new ICallBack<JSONObject>() {
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


    //Done By Maysara
    @Override
    public void getJourneyDetails(final int id, final ICallBack<Journey> journey) {

        Map<String,String> params = new HashMap<String, String>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final User localUser = getLocalUser();
        params.put("action","getJourneyDetails");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId",id+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                Journey tempJourney = new Journey();
                JSONObject temp = value;

                tempJourney.setId(id);
                tempJourney.setUser(localUser);
                tempJourney.setStatus(0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                tempJourney.setGoingDate(simpleDateFormat.parse(temp.getString("goingDate")));
                tempJourney.setSeats(temp.getInt("seats"));
                tempJourney.setGenderPrefer(temp.getInt("genderPrefer"));
                tempJourney.setStartPoint(new LatLng(temp.getDouble("startLocationX"),temp.getDouble("startLocationY")));
                tempJourney.setEndPoint(new LatLng(temp.getDouble("endLocationX"),temp.getDouble("endLocationY")));
                tempJourney.setCarDescription(temp.getString("carDescription"));

                journey.onResponse(tempJourney);

                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    journey.onError(e.toString());
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    journey.onError(e.toString());
                }


            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journey.onError(err);

            }
        });

    }

    //DONE
    @Override
    public void setNewJourney(Journey newJourney, final ICallBack<Integer> journeyId) {
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

        send(params, new ICallBack<JSONObject>() {
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


    //Done By Maysara
    @Override
    public void filterJourneys(final LatLng startPoint, final LatLng endPoint, Date goingDate, int sortBy, final IListCallBack<Journey> Journeys) {
        User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","filterJourneys");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("startPointX" , startPoint.latitude+"");
        params.put("startPointY" , startPoint.longitude+"");
        params.put("endPointX" , endPoint.latitude+"");
        params.put("endPointY" , endPoint.longitude+"");
        params.put("goingDate" , goingDate+"");
        params.put("sortBy" , sortBy+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try{
                    if(value.has("journeys")){
                        JSONArray jsonArray = value.getJSONArray("journeys");
                        JSONObject jsonTemp;
                        Journey journeyTemp;
                        ArrayList<Journey> journeysArray = new ArrayList<Journey>();
                        for (int i=0 ; i<jsonArray.length() ; i++){
                            jsonTemp = jsonArray.getJSONObject(i);
                            journeyTemp = new Journey();
                            User tempUser = new User();
                            journeyTemp.setId(jsonTemp.getInt("id"));
                            journeyTemp.setStartPoint(new LatLng(jsonTemp.getDouble("startLocationX"),jsonTemp.getDouble("startLocationY")));
                            journeyTemp.setEndPoint(new LatLng(jsonTemp.getDouble("endLocationX"),jsonTemp.getDouble("endLocationY")));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                            journeyTemp.setGoingDate(simpleDateFormat.parse(jsonTemp.getString("goingDate")));
                            journeyTemp.setSeats(jsonTemp.getInt("seats"));
                            journeyTemp.setGenderPrefer(jsonTemp.getInt("genderPrefer"));
                            journeyTemp.setCarDescription(jsonTemp.getString("carDescription"));
                            JSONObject jsonUser = jsonTemp.getJSONObject("user");
                            tempUser.setUsername(jsonUser.getString("username"));
                            tempUser.setFullname(jsonUser.getString("fullname"));
                            tempUser.setId(jsonUser.getInt("id"));
                            tempUser.setGender(jsonUser.getInt("gender"));
                            tempUser.setPhone(jsonUser.getString("phone"));
                            tempUser.setAddress(jsonUser.getString("address"));
                            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("YYYY-MM-DD");
                            tempUser.setBirthdate(simpleDateFormat2.parse(jsonUser.getString("birthdate")));
                            journeyTemp.setUser(tempUser);
                            journeyTemp.setStatus(jsonTemp.getInt("status"));
                            journeysArray.add(journeyTemp);
                        }

                        Journeys.onResponse(journeysArray);

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Log.i("tagWebApi","Error on JSON getting item");
                    Journeys.onError(e.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    Journeys.onError(e.toString());
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                Journeys.onError(err);

            }
        });


    }

    @Override
    public void getNumberOfJourneys(ICallBack<Integer> trigger) {

    }

    //Done By Maysara
    @Override
    public void changeJourneyStatus(Journey journey, int status, final ICallBack<Boolean> triger) {
        User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","changeJourneyStatusAndGetRiders");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyid" , journey.getId()+"");
        params.put("status" , journey.getStatus()+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {

                try {
                    if(value.getString("status").equals("success")){
                        Log.i("tagWebApi", "journey status changed successfully");
                        triger.onResponse(true);

                    }else {
                        Log.i("tagWebApi", "failed to change journey status");
                        triger.onResponse(false);
                    }
                } catch (JSONException e) {
                    triger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                triger.onError(err);
            }
        });

    }


//    @Override
//    public void getCustomJourney(int jid, ICallBack<CustomJourney> triger) {
//
//    }


    //DONE
    @Override
    public void getRides(int userId, int limitStart, int limitNum, final IListCallBack<Ride> rides) {

        User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRides");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",localUser.getId()+"");
        params.put("start",limitStart+"");
        params.put("num",limitNum+"");

        send(params, new ICallBack<JSONObject>() {
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

    // Done By Maysara
    @Override
    public void getRideDetails(int rideId, final ICallBack<Ride> ride) {

        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRideDetails");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("rideId", rideId+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject item)  {


                Ride tempRide = new Ride();
                int userId;
                User tempUser = new User();
                Journey tempJourney = new Journey();


                try {
                    JSONObject temp = item;
                    tempRide.setId(temp.getInt("id"));


                    tempUser.setUsername(localUser.getUsername());
                    tempUser.setFullname(localUser.getFullname());
                    tempUser.setId(localUser.getId());
                    tempUser.setGender(localUser.getGender());
                    tempUser.setPhone(localUser.getPhone());
                    tempUser.setAddress(localUser.getAddress());
                    tempUser.setBirthdate(localUser.getBirthdate());
                    tempRide.setUser(tempUser);

                    JSONObject jsonJourney = temp.getJSONObject("journey");
                    tempJourney.setId(jsonJourney.getInt("id"));
                    tempJourney.setUser(tempUser);
                    tempJourney.setCarDescription(jsonJourney.getString("carDescription"));
                    //tempJourney.setStatus(jsonJourney.getInt("status"));
                    tempJourney.setSeats(jsonJourney.getInt("seats"));
                    tempJourney.setGenderPrefer(jsonJourney.getInt("genderPrefer"));
                    tempJourney.setStartPoint(new LatLng(jsonJourney.getDouble("startLocationX"),jsonJourney.getDouble("startLocationY")));
                    tempJourney.setEndPoint(new LatLng(jsonJourney.getDouble("endLocationX"),jsonJourney.getDouble("endLocationY")));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                    tempJourney.setGoingDate(simpleDateFormat.parse(jsonJourney.getString("goingDate")));

                    // need attention.......
                    tempJourney.setStatus(0);
                    // ............

                    tempRide.setJourney(tempJourney);

                    tempRide.setMeetingLocation(new LatLng(temp.getDouble("meetingLocationX"),temp.getDouble("meetingLocationY")));
                    tempRide.setOrderStatus(jsonJourney.getInt("orderStatus"));

                    ride.onResponse(tempRide);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    ride.onError(e.toString());
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                ride.onError(err);
            }
        });

    }

    // Done By Maysara
    @Override
    public void getRidersOfJourney(final Journey journey, final IListCallBack<Ride> callBack) {
        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRidersOfJourney");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId", journey.getId()+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try{
                    if(value.has("rides")){
                        JSONArray jsonArray = value.getJSONArray("rides");
                        JSONObject jsonTemp;
                        Ride rideTemp;
                        ArrayList<Ride> ridesArray = new ArrayList<Ride>();
                        for(int i=0;i<jsonArray.length();i++){
                            rideTemp = new Ride();
                            jsonTemp = jsonArray.getJSONObject(i);
                            rideTemp.setId(jsonTemp.getInt("id"));
                            rideTemp.setJourney(journey);
                            rideTemp.setOrderStatus(jsonTemp.getInt("orderStatus"));
                            rideTemp.setUser(localUser);
                            rideTemp.setMeetingLocation(new LatLng(jsonTemp.getDouble("meetingLocationX"),jsonTemp.getDouble("meetingLocationY")));
                            ridesArray.add(rideTemp);
                        }
                        callBack.onResponse(ridesArray);

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    callBack.onError(e.toString());
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                callBack.onError(err);
            }
        });
    }

    // Done By Maysara
    @Override
    public void setRideOnJourney(Ride newRide, final ICallBack<Integer> rideId) {
        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","setRideOnJourney");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId",  newRide.getJourney().getId()+"");
        params.put("meetingLocationX",newRide.getMeetingLocation().latitude+"");
        params.put("meetingLocationY",newRide.getMeetingLocation().longitude+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    int rideIdTemp;
                    if((rideIdTemp = value.getInt("rideId")) > 0){
                        Log.i("tagWebApi", "rideId returned successfully");
                        rideId.onResponse(rideIdTemp);

                    }else {
                        if(value.get("status").equals("noAvailableSeats")){
                            Log.i("tagWebApi", "noAvailableSeats");
                        }else {

                            Log.i("tagWebApi", "something went wrong");
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    rideId.onError(e.toString());
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                rideId.onError(err);
            }
        });
    }

    //Done By Maysara
    @Override
    public void changeRideStatus(int rideId, int status, final ICallBack<Boolean> result) {

        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","changeRideStatus");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("rideId", rideId+"");
        params.put("orderStatus" , status+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {

                try {
                    if (value.getString("status").equals("success")){

                        Log.i("tagWebApi", "ride status changes successfully ");
                        result.onResponse(true);
                    }else
                    {

                        Log.i("tagWebApi", "error in changing ride's status ");
                        result.onResponse(false);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    result.onError(e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                result.onError(err);

            }
        });



    }

    //Done By Maysara
    @Override
    public void getStatusOfRide(int rideId, final ICallBack<Integer> triger) {
        User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getStatusOfRide");
        params.put("rideId" , rideId+"");
        params.put("username" , localUser.getUsername());
        params.put("password" , localUser.getPassword());

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {

                    int rideStatus;
                    if(value.has("rideStatus")){
                        rideStatus = value.getInt("rideStatus");
                        triger.onResponse(rideStatus);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    triger.onError(e.toString());
                }


            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                triger.onError(err);
            }
        });

    }

    //Done By Maysara
    @Override
    public void getEventAtDate(Date date, final IListCallBack<Event> eventITriger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getEventAtDate");
        params.put("date" , date+"");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if(value.has("events")){

                        JSONArray jsonArray = value.getJSONArray("events");
                        JSONObject jsonTemp;
                        Event eventTemp;
                        ArrayList<Event> eventsArray = new ArrayList<Event>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            eventTemp = new Event();
                            jsonTemp = jsonArray.getJSONObject(i);
                            eventTemp.setId(jsonTemp.getInt("id"));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                            eventTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("date")));
                            eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            eventTemp.setDescription(jsonTemp.getString("description"));
                            eventTemp.setName(jsonTemp.getString("name"));
                            eventsArray.add(eventTemp);

                        }

                        eventITriger.onResponse(eventsArray);


                    }
                } catch (JSONException e) {
                    eventITriger.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    eventITriger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                eventITriger.onError(err);

            }
        });

    }
    //Done By Maysara
    @Override
    public void getEvents(final IListCallBack<Event> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getEvents");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                if(value.has("events")){

                    JSONArray jsonArray = value.getJSONArray("events");
                    JSONObject jsonTemp;
                    Event eventTemp;
                    ArrayList<Event> eventsArray = new ArrayList<Event>();

                    for (int i=0 ; i< jsonArray.length() ; i++){
                        eventTemp = new Event();
                        jsonTemp = jsonArray.getJSONObject(i);
                        eventTemp.setId(jsonTemp.getInt("id"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                        eventTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("date")));
                        eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                        eventTemp.setDescription(jsonTemp.getString("description"));
                        eventTemp.setName(jsonTemp.getString("name"));
                        eventsArray.add(eventTemp);

                    }

                    callBack.onResponse(eventsArray);


                }
                } catch (JSONException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });


    }
    //Done By Maysara
    @Override
    public void getAnnouns(final IListCallBack<Announcement> eventITriger) {


        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getAnnouns");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {

                try{
                    if(value.has("announcement")){
                        JSONArray jsonArray = value.getJSONArray("announcement");
                        JSONObject jsonTemp;
                        Announcement announcementTemp;
                        ArrayList<Announcement> announcementsArray = new ArrayList<Announcement>();

                        for (int i=0 ; i<jsonArray.length() ; i++){
                            announcementTemp = new Announcement();
                            jsonTemp = jsonArray.getJSONObject(i);
                            announcementTemp.setId(jsonTemp.getInt("id"));
                            announcementTemp.setName(jsonTemp.getString("name"));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                            announcementTemp.setStartDate(simpleDateFormat.parse(jsonTemp.getString("startDate")));
                            announcementTemp.setEndDate(simpleDateFormat.parse(jsonTemp.getString("endData")));
                            announcementTemp.setDescription(jsonTemp.getString("description"));
                            announcementTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            announcementsArray.add(announcementTemp);
                        }

                        eventITriger.onResponse(announcementsArray);
                    }


                }catch (JSONException e){
                    eventITriger.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    eventITriger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                eventITriger.onError(err);
            }
        });


    }
    //Done By Maysara
    @Override
    public void getJobs(final IListCallBack<Job> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getJobs");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                    if(value.has("jobs")){

                        JSONArray jsonArray = value.getJSONArray("jobs");
                        JSONObject jsonTemp;
                        Job jobTemp;
                        ArrayList<Job> jobsArray = new ArrayList<Job>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            jobTemp = new Job();
                            jsonTemp = jsonArray.getJSONObject(i);
                            jobTemp.setId(jsonTemp.getInt("id"));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                            jobTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("date")));
                            jobTemp.setName(jsonTemp.getString("name"));
                            jobTemp.setDescription(jsonTemp.getString("description"));
                            jobTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            jobsArray.add(jobTemp);
                        }

                        callBack.onResponse(jobsArray);


                    }
                } catch (JSONException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });

    }
    //Done By Maysara
    @Override
    public void getTransportation(final ICallBack<Transportation> triger) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getTransportation");

        send(params, new ICallBack<JSONObject>() {
            @Override
            public void onResponse(JSONObject value) {
                try {
                if(value.has("fromRawabi") && value.has("fromRamallah")){

                    JSONArray fromRawabiJsonArray = value.getJSONArray("fromRawabi");
                    JSONArray fromRamallahJsonArray = value.getJSONArray("fromRamallah");
                    JSONObject fromRawabiJsonTemp , fromRamallahJsonTemp;
                    TransportationElement fromRawabiTE , fromRamallahTE;
                    ArrayList<TransportationElement>fromRamallahList = new ArrayList<TransportationElement>(), fromRawabiList = new ArrayList<TransportationElement>();
                    Transportation transportation = new Transportation();
                    if(fromRawabiJsonArray.length() == fromRamallahJsonArray.length()){

                        for (int i=0 ; i<fromRamallahJsonArray.length() ; i++){

                            fromRamallahTE = new TransportationElement();
                            fromRawabiTE = new TransportationElement();
                            fromRamallahJsonTemp = fromRamallahJsonArray.getJSONObject(i);
                            fromRawabiJsonTemp = fromRawabiJsonArray.getJSONObject(i);
                            // from ramallah
                            fromRamallahTE.setId(fromRamallahJsonTemp.getInt("id"));
                            fromRamallahTE.setTime(fromRamallahJsonTemp.getString("time"));
                            fromRamallahTE.setType(fromRamallahJsonTemp.getInt("type"));
                            //from rawabi
                            fromRawabiTE.setId(fromRawabiJsonTemp.getInt("id"));
                            fromRawabiTE.setType(fromRawabiJsonTemp.getInt("type"));
                            fromRawabiTE.setTime(fromRawabiJsonTemp.getString("time"));

                            fromRamallahList.add(fromRamallahTE);
                            fromRawabiList.add(fromRawabiTE);

                        }
                        transportation.setFromRamallah(fromRamallahList);
                        transportation.setFromRawabi(fromRawabiList);
                        triger.onResponse(transportation);
                    }else{
                        for (int i=0 ; i<fromRamallahJsonArray.length() ; i++){
                            fromRamallahTE = new TransportationElement();
                            fromRamallahJsonTemp = fromRamallahJsonArray.getJSONObject(i);
                            fromRamallahTE.setId(fromRamallahJsonTemp.getInt("id"));
                            fromRamallahTE.setTime(fromRamallahJsonTemp.getString("time"));
                            fromRamallahTE.setType(fromRamallahJsonTemp.getInt("type"));
                            fromRamallahList.add(fromRamallahTE);
                        }
                        for (int i=0 ; i<fromRawabiJsonArray.length() ; i++){
                            fromRawabiTE = new TransportationElement();
                            fromRawabiJsonTemp = fromRawabiJsonArray.getJSONObject(i);
                            //from rawabi
                            fromRawabiTE.setId(fromRawabiJsonTemp.getInt("id"));
                            fromRawabiTE.setType(fromRawabiJsonTemp.getInt("type"));
                            fromRawabiTE.setTime(fromRawabiJsonTemp.getString("time"));
                            fromRawabiList.add(fromRawabiTE);

                        }
                        transportation.setFromRamallah(fromRamallahList);
                        transportation.setFromRawabi(fromRawabiList);
                        triger.onResponse(transportation);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                    triger.onError(e.toString());
                }

            }

            @Override
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                triger.onError(err);
            }
        });

    }

    @Override
    public void getWeather(ICallBack<String> callBack) {

    }

    @Override
    public void getMedia(IListCallBack<MediaItem> callBack) {


    }
}
