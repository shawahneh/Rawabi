package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.model.AlbumItem;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ALa on 11/15/2017.
 */

/**
 * Implementations using volley
 */
public class WebApi {

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
            
            public void onResponse(String response) {
                Log.i("tagWebApi","Response : "+ response);
                try {
                    Log.d("tagWebApi","onResponse send");
                    Log.d("tagWebApi","onResponse " + response);
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
            
            public void onErrorResponse(VolleyError error) {
                Log.i("tagWebApi","ERROR "+ error.toString());
                result.onError(error.toString());
            }
        }){
            
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
    
    public void userRegister(User user, final ICallBack<Boolean> booleanIcallBack) {
        Map<String,String> params = new HashMap<String, String>();

        //($username,$password,$fullname,$gender,$birthdate,$address,$userType,$image,$phone)
        params.put("action","userRegister");
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());
        params.put("fullname",user.getFullname());
        params.put("gender",user.getGender()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        params.put("birthdate",simpleDateFormat.format(user.getBirthdate()));
        params.put("address",user.getAddress());
        params.put("userType","1");
        //params.put("image",user.getImageurl());
        params.put("phone",user.getPhone());
        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                        Log.i("tagWebApi", "on respons: "+value.toString());
                try {
                    if(value.has("registration")){
                        Log.i("tagWebApi", "on registration");

                    if (value.getString("registration").equals("success")){
                        Log.i("tagWebApi", "register process is done");
                        booleanIcallBack.onResponse(true);
                    }else
                    {
                        Log.i("tagWebApi", "register process is failed");
                        booleanIcallBack.onResponse(false);
                    }
                    }else{
                        Log.i("tagWebApi", "no registration");
                        booleanIcallBack.onError("error, no registration");
                    }

                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    booleanIcallBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.i("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                booleanIcallBack.onError(err);
            }

        });
    }

    // Done By Maysara
    
    public void setUserDetails(User user, String OldPassword, final ICallBack<Boolean> booleanIcallBack) {
        Map<String,String> params = new HashMap<String, String>();

        params.put("action","setUserDetails");
        params.put("username",user.getUsername());
        params.put("fullname",user.getFullname());
        params.put("gender",user.getGender()+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        params.put("birthdate",simpleDateFormat.format(user.getBirthdate()));
        params.put("address",user.getAddress());
        params.put("phone",user.getPhone());
        params.put("newPassword" , user.getPassword());
        params.put("oldPassword" , OldPassword);

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject item) {
                try {
                    if (item.getString("status").equals("success")){
                        Log.i("tagWebApi", "setUserDetails process is done");
                        booleanIcallBack.onResponse(true);
                    }else
                    {
                        Log.i("tagWebApi", "setUserDetails process is failed");
                        booleanIcallBack.onResponse(false);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    booleanIcallBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.i("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                booleanIcallBack.onError(err);
            }
        });


    }

    //DONE
    // if userId <= 0 then give me the details of current user
    // if username = null give me the current user
    // if username = null gime me the current user
    //done for offline
    
    public void getUserDetails(int userId, final ICallBack<User> resultUser) {


        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getUserDetails");
        final User localUser = getLocalUser();
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("userId",userId+"");

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {

                    if (value.has("username") && !value.isNull("username")){

                        User userDetails = new User();
                        userDetails.setUsername(localUser.getUsername());
                        userDetails.setPassword(localUser.getPassword());
                        userDetails.setFullname(value.getString("fullname"));
                        userDetails.setAddress(value.getString("address"));
                        userDetails.setPhone(value.getString("phone"));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                resultUser.onError(err);
            }
        });
    }

    //DONE
    
    public void login(final String username, final String password, final ICallBack<User> resultUser) {
        Map<String,String> params = new HashMap<String, String>();
        Log.i("tagWebApi", "login: u: "+username+" p: "+password);
        params.put("action","getUserDetails");
        params.put("username",username);
        params.put("password",password);
        params.put("userId","-1");

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {
                    if (value.has("username")){

                        User userDetails = new User();
                        userDetails.setUsername(username);
                        userDetails.setPassword(password);
                        userDetails.setFullname(value.getString("fullname").toString());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                resultUser.onError(err);
            }
        });
    }

    //DONE
    
    public void checkAuth(String username, String password, final ICallBack<Boolean> booleanIcallBack) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","userAuth");
        params.put("username",username);
        params.put("password",password);
        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {
                    if (value.getString("auth").equals("true")){

                        Log.i("tagWebApi", "user auth is ok");
                        booleanIcallBack.onResponse(true);
                    }else
                    {

                        Log.i("tagWebApi", "user auth is not ok");
                        booleanIcallBack.onResponse(false);
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi","Error on JSON getting item");
                    booleanIcallBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                booleanIcallBack.onError(err);
            }
        });
    }

    
    public void setImageForUser(Uri uri, final ICallBack<String> callBack) {
        final User user = getLocalUser();
        RequestParams params = new RequestParams();
        params.put("action","uploadUserImage");
        params.put("username",user.getUsername());
        params.put("password",user.getPassword());

        InputStream imageStream = null;
        try {
            imageStream = mContext.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        String encodedImage = encodeImage(selectedImage);
        params.put("image",encodedImage);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(apiUrl,
                params, new AsyncHttpResponseHandler() {
                    
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("tagWebApi", "setImageForUser, onSuccess");
                        Log.d("tagWebApi", "setImageForUser, response body=" + responseBody);
                        String imageUrl;
                        try {
                            JSONObject tmp = new JSONObject(new String(responseBody));
                            if(tmp.has("success")){
                                imageUrl = tmp.getString("success");
                        Log.d("tagWebApi", "imageUrl, " + imageUrl);
                                user.setImageurl(imageUrl);
                                callBack.onResponse(imageUrl);
                            }else
                                callBack.onResponse(null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("tagWebApi","JSONObject, "+e.getMessage());
                            callBack.onError(e.getMessage());
                        }

                    }

                    
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("tagWebApi", "onFailure error code " + statusCode);
                        callBack.onError(error.getMessage());
                    }
                });
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    //DONE
    
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
            
            public void onResponse(JSONObject value) {
                Log.d("tagWebApi", "getJourneys response="+value.toString());
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
                              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                             Jtemp.setGoingDate(simpleDateFormat.parse(temp.getString("goingDate")));
                             Jtemp.setSeats(temp.getInt("seats"));
                             Jtemp.setGenderPrefer(temp.getInt("genderPrefer"));
                             Jtemp.setCarDescription(temp.getString("carDescription"));
                             Jtemp.setStatus(temp.getInt("status"));

                              JSONObject jsonUser = temp.getJSONObject("user");
                              User tempUser = new User();
                              tempUser.setUsername(jsonUser.getString("username"));
                              tempUser.setFullname(jsonUser.getString("fullname"));
                              tempUser.setId(jsonUser.getInt("id"));
                              tempUser.setGender(jsonUser.getInt("gender"));
                              tempUser.setPhone(jsonUser.getString("phone"));
                              tempUser.setAddress(jsonUser.getString("address"));
                              SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
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

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journeys.onError(err);
            }
        });
    }


    //Done By Maysara
    public void getJourneyDetails(final int id, final ICallBack<Journey> journey) {

        Map<String,String> params = new HashMap<String, String>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final User localUser = getLocalUser();
        params.put("action","getJourneyDetails");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId",id+"");

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {
                Journey tempJourney = new Journey();
                JSONObject temp = value;

                tempJourney.setId(id);
                tempJourney.setUser(localUser);
                tempJourney.setStatus(temp.getInt("status"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journey.onError(err);

            }
        });

    }

    //DONE
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

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                journeyId.onError(err);
            }
        });
    }


    //Done By Maysara
    
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                Journeys.onError(err);

            }
        });


    }

    
    public void getNumberOfJourneys(ICallBack<Integer> trigger) {

    }

    //Done By Maysara
    
    public void changeJourneyStatus(Journey journey, int status, final ICallBack<Boolean> trigger) {
        User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","changeJourneyStatusAndGetRiders");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId" , journey.getId()+"");
        params.put("status" , status+"");

        Log.d("tagWebApi","journeyId="+journey.getId()+", status="+status);
        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {

                try {
                    if(value.getString("status").equals("success")){
                        Log.i("tagWebApi", "journey status changed successfully");
                        trigger.onResponse(true);

                    }else {
                        Log.i("tagWebApi", "failed to change journey status");
                        trigger.onResponse(false);
                    }
                } catch (JSONException e) {
                    trigger.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                trigger.onError(err);
            }
        });

    }


//    
//    public void getCustomJourney(int jid, ICallBack<CustomJourney> callBack) {
//
//    }


    //DONE
    
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
                                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                                tempUser.setBirthdate(simpleDateFormat2.parse(jsonUser.getString("birthdate")));
                                Rtemp.setUser(tempUser);
                                JSONObject jsonJourney = temp.getJSONObject("journey");
                                    Journey Jtemp = new Journey();
                                    Jtemp.setId(jsonJourney.getInt("id"));
                                    Jtemp.setStartPoint(new LatLng(jsonJourney.getDouble("startLocationX"),jsonJourney.getDouble("startLocationY")));
                                    Jtemp.setEndPoint(new LatLng(jsonJourney.getDouble("endLocationX"),jsonJourney.getDouble("endLocationY")));
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                rides.onError(err);
            }
        });

    }

    // Done By Maysara
    
    public void getRideDetails(int rideId, final ICallBack<Ride> ride) {

        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRideDetails");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("rideId", rideId+"");
        Log.d("tagWebApi","rideId="+rideId);

        send(params, new ICallBack<JSONObject>() {
            
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                ride.onError(err);
            }
        });

    }

    // Done By Maysara
    //done for offline
    
    public void getRidersOfJourney(final Journey journey, final IListCallBack<Ride> callBack) {
        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getRidersOfJourney");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("journeyId", journey.getId()+"");

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                Log.d("tagWebApi","response="+value.toString() );
                try{
                    if(value.has("rides")){
                        JSONArray jsonArray = value.getJSONArray("rides");
                        JSONObject jsonTemp;
                        Ride rideTemp;
                        ArrayList<Ride> ridesArray = new ArrayList<Ride>();
                        for(int i=0;i<jsonArray.length();i++){
                            rideTemp = new Ride();
                            User user = new User();

                            jsonTemp = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectUser = jsonTemp.getJSONObject("user") ;
                            user.setFullname(jsonObjectUser.getString("fullname"));
                            user.setUsername(jsonObjectUser.getString("username"));

                            user.setGender(jsonObjectUser.getInt("gender"));
                            user.setImageurl(jsonObjectUser.getString("image"));
                            user.setPhone(jsonObjectUser.getString("phone"));

                            rideTemp.setId(jsonTemp.getInt("id"));
                            rideTemp.setJourney(journey);
                            rideTemp.setOrderStatus(jsonTemp.getInt("orderStatus"));
                            rideTemp.setUser(user);
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                callBack.onError(err);
            }
        });
    }

    // Done By Maysara
    //done for offline
    
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method in getRideDetails ");
                Log.e("tagWebApi",err);
                rideId.onError(err);
            }
        });
    }

    /* created by ala, this method used to change the status of user's rides only  */
    //done for offline
    public void changeMyRideStatus(int rideId,int status, final ICallBack<Boolean> result){

        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","changeMyRideStatus");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("rideId", rideId+"");
        params.put("orderStatus" , status+"");
        Log.d("tagWebApi","rideId=" + rideId + ",status="+status);
        send(params, new ICallBack<JSONObject>() {
            
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

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                result.onError(err);

            }
        });

    }
    //Done By Maysara
    //done for offline
    
    public void changeRideStatus(int rideId, int status, final ICallBack<Boolean> result) {

        final User localUser = getLocalUser();

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","changeRideStatus");
        params.put("username",localUser.getUsername());
        params.put("password",localUser.getPassword());
        params.put("rideId", rideId+"");
        params.put("orderStatus" , status+"");

        send(params, new ICallBack<JSONObject>() {
            
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

            
            public void onError(String err) {

                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                result.onError(err);

            }
        });



    }

    //Done By Maysara
    //done for offline
    
    public void getStatusOfRide(int rideId, final ICallBack<Integer> callBack) {
        User localUser = getLocalUser();
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getStatusOfRide");
        params.put("rideId" , rideId+"");
        params.put("username" , localUser.getUsername());
        params.put("password" , localUser.getPassword());

        Log.d("tagWebApi","rideId="+rideId);

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {

                    int rideStatus;
                    if(value.has("rideStatus")){
                        rideStatus = value.getInt("rideStatus");
                        callBack.onResponse(rideStatus);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onError(e.toString());
                }


            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);
            }
        });

    }

    //Done By Maysara
    //done for offline
    
    public void getEventAtDate(Date date, final IListCallBack<Event> eventIcallBack) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getEventAtDate");
        params.put("date" , date+"");

        send(params, new ICallBack<JSONObject>() {
            
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            eventTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("date")));
                            eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            eventTemp.setDescription(jsonTemp.getString("description"));
                            eventTemp.setName(jsonTemp.getString("name"));
                            eventsArray.add(eventTemp);

                        }

                        eventIcallBack.onResponse(eventsArray);


                    }
                } catch (JSONException e) {
                    eventIcallBack.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    eventIcallBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                eventIcallBack.onError(err);

            }
        });

    }
    //Done By Maysara
    //done for offline
    
    public void getEvents(final IListCallBack<Event> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getEvents");

        send(params, new ICallBack<JSONObject>() {
            
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
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        eventTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("startDateTime")));
                        eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                        eventTemp.setDescription(jsonTemp.getString("description"));
                        eventTemp.setName(jsonTemp.getString("title"));
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });


    }
    //Done By Maysara
    //done for offline
    
    public void getAnnouns(final IListCallBack<Announcement> eventIcallBack) {


        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getAnnouns");

        send(params, new ICallBack<JSONObject>() {
            
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            announcementTemp.setStartDate(simpleDateFormat.parse(jsonTemp.getString("startDate")));

                            /* no end date */
//                            announcementTemp.setEndDate(simpleDateFormat.parse(jsonTemp.getString("endData")));
                            announcementTemp.setDescription(jsonTemp.getString("description"));
                            announcementTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            announcementsArray.add(announcementTemp);
                        }

                        eventIcallBack.onResponse(announcementsArray);
                    }


                }catch (JSONException e){
                    eventIcallBack.onError(e.toString());
                    e.printStackTrace();
                } catch (ParseException e) {
                    eventIcallBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                eventIcallBack.onError(err);
            }
        });


    }
    //Done By Maysara
    //done for offline
    
    public void getJobs(final IListCallBack<Job> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getJobs");

        send(params, new ICallBack<JSONObject>() {
            
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                            Log.d("tag","endDate: " + jsonTemp.getString("endDate"));
                            jobTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("endDate")));
                            Log.d("tag","endDate: " + simpleDateFormat.parse(jsonTemp.getString("endDate")));


                            jobTemp.setName(jsonTemp.getString("jobTitle"));
                            jobTemp.setDescription(jsonTemp.getString("description"));
                            jobTemp.setCompanyName(jsonTemp.getString("companyName"));

                            if(jsonTemp.has("imageUrl"))
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

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });

    }
    //Done By Maysara
    //done for offline
    
    public void getTransportation(final ICallBack<Transportation> callBack) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getTransportation");

        send(params, new ICallBack<JSONObject>() {
            
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
                        callBack.onResponse(transportation);
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
                        callBack.onResponse(transportation);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onError(e.toString());
                }

            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);
            }
        });

    }

    
    public void getWeather(ICallBack<String> callBack) {

    }
    //Done by shawahneh
    //done for offline
    
    public void getAlbums(final IListCallBack<AlbumItem> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getAlbums");

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {
                    if(value.has("media")){

                        JSONArray jsonArray = value.getJSONArray("media");
                        JSONObject jsonTemp;
                        AlbumItem albumTemp;
                        ArrayList<AlbumItem> albumArray = new ArrayList<AlbumItem>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            albumTemp = new AlbumItem();
                            jsonTemp = jsonArray.getJSONObject(i);
                            albumTemp.setId(jsonTemp.getInt("id"));

                            albumTemp.setTitle(jsonTemp.getString("name"));
                            albumTemp.setDescription(jsonTemp.getString("description"));
                            albumTemp.setImgUrl(jsonTemp.getString("imageUrl"));
                            albumArray.add(albumTemp);
                        }

                        callBack.onResponse(albumArray);


                    }
                } catch (JSONException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });
    }
    //done for offline
    
    public void getGalleryForAlbum(int albumId, final IListCallBack<MediaItem> callBack) {

        Map<String,String> params = new HashMap<String, String>();
        params.put("action","getAlbumImages");
        params.put("albumId",""+albumId);

        send(params, new ICallBack<JSONObject>() {
            
            public void onResponse(JSONObject value) {
                try {
                    if(value.has("album")){
                        JSONObject jsonAlbum = value.getJSONObject("album");
                        JSONArray jsonArray = jsonAlbum.getJSONArray("images");
                        JSONObject jsonTemp;
                        MediaItem mediaTemp;
                        ArrayList<MediaItem> mediaArray = new ArrayList<MediaItem>();

                        for (int i=0 ; i< jsonArray.length() ; i++){
                            mediaTemp = new MediaItem();
                            jsonTemp = jsonArray.getJSONObject(i);
                            mediaTemp.setId(jsonTemp.getInt("id"));
                            mediaTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            mediaArray.add(mediaTemp);
                        }

                        callBack.onResponse(mediaArray);


                    }
                } catch (JSONException e) {
                    callBack.onError(e.toString());
                    e.printStackTrace();
                }
            }

            
            public void onError(String err) {
                Log.d("tagWebApi", "Error while getting data from send() method ");
                Log.e("tagWebApi",err);
                callBack.onError(err);

            }
        });
    }


}
