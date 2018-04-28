package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.callBacks.IListCallBack;
import com.techcamp.aauj.rawabi.controllers.SPController;
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
import com.techcamp.aauj.rawabi.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public class WebService implements AuthWebApi,BasicApi,CarpoolApi {
    private static final String url = "https://tcamp.000webhostapp.com/api/index.php";
    private Context mContext;
    private static WebService instance;

    public WebService(Context mContext) {
        this.mContext = mContext;
    }

    public static void init(Context context) {
        if (instance == null)
            instance = new WebService(context);
    }

    public static WebService getInstance() {
        return instance;
    }

    public User getLocalUser() {
        if (mContext == null)
            return null;
        return SPController.getLocalUser(mContext);
    }

    @Override
    public RequestService getAnnouns(IListCallBack<Announcement> callBack) {
        return new RequestService<List<Announcement>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getAnnouns");
                return params;
            }

            @Override
            public List<Announcement> parseResponse(JSONObject response) {
                try {
                    if (response.has("announcement")) {
                        JSONArray jsonArray = response.getJSONArray("announcement");
                        JSONObject jsonTemp;
                        Announcement announcementTemp;
                        ArrayList<Announcement> announcementsArray = new ArrayList<Announcement>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            announcementTemp = new Announcement();
                            jsonTemp = jsonArray.getJSONObject(i);
                            announcementTemp.setId(jsonTemp.getInt("id"));
                            announcementTemp.setName(jsonTemp.getString("name"));
                            announcementTemp.setStartDate(DateUtil.parseFromDateOnly(jsonTemp.getString("startDate")));

                            /* no end date */
//                            announcementTemp.setEndDate(simpleDateFormat.parse(jsonTemp.getString("endData")));
                            announcementTemp.setDescription(jsonTemp.getString("description"));
                            announcementTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            announcementsArray.add(announcementTemp);
                        }

                        return announcementsArray;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    public RequestService getJobs(IListCallBack<Job> callBack) {
        return new RequestService<List<Job>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getJobs");
                return params;
            }

            @Override
            public List<Job> parseResponse(JSONObject response) {
                try {
                    if (response.has("jobs")) {

                        JSONArray jsonArray = response.getJSONArray("jobs");
                        JSONObject jsonTemp;
                        Job jobTemp;
                        ArrayList<Job> jobsArray = new ArrayList<Job>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobTemp = new Job();
                            jsonTemp = jsonArray.getJSONObject(i);
                            jobTemp.setId(jsonTemp.getInt("id"));

                            jobTemp.setDate(DateUtil.parseFromDateOnly(jsonTemp.getString("endDate")));


                            jobTemp.setName(jsonTemp.getString("jobTitle"));
                            jobTemp.setDescription(jsonTemp.getString("description"));
                            jobTemp.setCompanyName(jsonTemp.getString("companyName"));

                            if (jsonTemp.has("imageUrl"))
                                jobTemp.setImageUrl(jsonTemp.getString("imageUrl"));

                            jobsArray.add(jobTemp);
                        }
                        return jobsArray;
                    }

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };


    }

    public RequestService<List<AlbumItem>> getAlbums(IListCallBack<AlbumItem> callBack) {
        return new RequestService<List<AlbumItem>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {

                /*   put parameters here   */
                Map<String, String> params = new HashMap<>();
                params.put("action", "getAlbums");

                return params;
            }

            @Override
            public List<AlbumItem> parseResponse(JSONObject Response) {

                /*        parse json response here         */
                try {
                    if (Response.has("media")) {

                        JSONArray jsonArray = Response.getJSONArray("media");
                        JSONObject jsonTemp;
                        AlbumItem albumTemp;
                        ArrayList<AlbumItem> albumArray = new ArrayList<AlbumItem>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            albumTemp = new AlbumItem();
                            jsonTemp = jsonArray.getJSONObject(i);
                            albumTemp.setId(jsonTemp.getInt("id"));

                            albumTemp.setTitle(jsonTemp.getString("name"));
                            albumTemp.setDescription(jsonTemp.getString("description"));
                            albumTemp.setImgUrl(jsonTemp.getString("imageUrl"));
                            albumArray.add(albumTemp);
                        }

                        return albumArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }


                return null;
            }
        };
    }

    @Override
    public RequestService getGalleryForAlbum(final int albumId, IListCallBack<MediaItem> callBack) {
        return new RequestService<List<MediaItem>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getAlbumImages");
                params.put("albumId", "" + albumId);
                return params;
            }

            @Override
            public List<MediaItem> parseResponse(JSONObject response) {
                try {
                    if (response.has("album")) {
                        JSONObject jsonAlbum = response.getJSONObject("album");
                        JSONArray jsonArray = jsonAlbum.getJSONArray("images");
                        JSONObject jsonTemp;
                        MediaItem mediaTemp;
                        ArrayList<MediaItem> mediaArray = new ArrayList<MediaItem>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            mediaTemp = new MediaItem();
                            jsonTemp = jsonArray.getJSONObject(i);
                            mediaTemp.setId(jsonTemp.getInt("id"));
                            mediaTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            mediaArray.add(mediaTemp);
                        }

                        return mediaArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    @Override
    public RequestService getEventAtDate(final Date date, IListCallBack<Event> callBack) {
        return new RequestService<List<Event>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getEventAtDate");
                params.put("date", date + "");
                return params;
            }

            @Override
            public List<Event> parseResponse(JSONObject response) {
                try {
                    if (response.has("events")) {

                        JSONArray jsonArray = response.getJSONArray("events");
                        JSONObject jsonTemp;
                        Event eventTemp;
                        ArrayList<Event> eventsArray = new ArrayList<Event>();

                        for (int i = 0; i < jsonArray.length(); i++) {
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

                        return eventsArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    public RequestService<List<Event>> getEvents(IListCallBack<Event> callBack) {
        return new RequestService<List<Event>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {

                /*   put parameters here   */
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getEvents");

                return params;
            }

            @Override
            public List<Event> parseResponse(JSONObject response) {

                /*        parse json response here         */
                try {
                    if (response.has("events")) {

                        JSONArray jsonArray = response.getJSONArray("events");
                        JSONObject jsonTemp;
                        Event eventTemp;
                        ArrayList<Event> eventsArray = new ArrayList<Event>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            eventTemp = new Event();
                            jsonTemp = jsonArray.getJSONObject(i);
                            eventTemp.setId(jsonTemp.getInt("id"));
                            eventTemp.setDate(DateUtil.parseFromUTC(jsonTemp.getString("startDateTime")));
                            eventTemp.setImageUrl(jsonTemp.getString("imageUrl"));
                            eventTemp.setDescription(jsonTemp.getString("description"));
                            eventTemp.setName(jsonTemp.getString("title"));
                            eventsArray.add(eventTemp);

                        }
                        return eventsArray;


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }

                return null;
            }
        };
    }

    public RequestService<Transportation> getTransportation(ICallBack<Transportation> callBack) {
        return new RequestService<Transportation>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getTransportation");
                return params;
            }

            @Override
            public Transportation parseResponse(JSONObject response) {
                try {
                    if (response.has("fromRawabi") && response.has("fromRamallah")) {

                        JSONArray fromRawabiJsonArray = response.getJSONArray("fromRawabi");
                        JSONArray fromRamallahJsonArray = response.getJSONArray("fromRamallah");
                        JSONObject fromRawabiJsonTemp, fromRamallahJsonTemp;
                        TransportationElement fromRawabiTE, fromRamallahTE;
                        ArrayList<TransportationElement> fromRamallahList = new ArrayList<TransportationElement>(), fromRawabiList = new ArrayList<TransportationElement>();
                        Transportation transportation = new Transportation();
                        if (fromRawabiJsonArray.length() == fromRamallahJsonArray.length()) {

                            for (int i = 0; i < fromRamallahJsonArray.length(); i++) {

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
                            return transportation;
                        } else {
                            for (int i = 0; i < fromRamallahJsonArray.length(); i++) {
                                fromRamallahTE = new TransportationElement();
                                fromRamallahJsonTemp = fromRamallahJsonArray.getJSONObject(i);
                                fromRamallahTE.setId(fromRamallahJsonTemp.getInt("id"));
                                fromRamallahTE.setTime(fromRamallahJsonTemp.getString("time"));
                                fromRamallahTE.setType(fromRamallahJsonTemp.getInt("type"));
                                fromRamallahList.add(fromRamallahTE);
                            }
                            for (int i = 0; i < fromRawabiJsonArray.length(); i++) {
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
                            return transportation;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    @Override
    public RequestService getWeather(ICallBack<String> callBack) {
        return null;
    }

    @Override
    public RequestService getJourneys(final int userId, final int limitStart, final int limitNum, IListCallBack<Journey> callBack) {
        return new RequestService<List<Journey>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                User localUser = getLocalUser();
                // if the userId <= 0 then get the logged in user
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getJourneys");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("userId", userId + "");
                params.put("start", limitStart + "");
                params.put("num", limitNum + "");
                return params;
            }

            @Override
            public List<Journey> parseResponse(JSONObject response) {
                try {
                    if (response.has("journeys")) {
                        JSONArray JArr = response.getJSONArray("journeys");
                        JSONObject temp;
                        Journey Jtemp;
                        ArrayList<Journey> objArr = new ArrayList<Journey>();
                        for (int i = 0; i < JArr.length(); i++) {
                            temp = JArr.getJSONObject(i);
                            Jtemp = new Journey();
                            Jtemp.setId(temp.getInt("id"));
                            Jtemp.setStartPoint(new LatLng(temp.getDouble("startLocationX"), temp.getDouble("startLocationY")));
                            Jtemp.setEndPoint(new LatLng(temp.getDouble("endLocationX"), temp.getDouble("endLocationY")));


                            Jtemp.setGoingDate(DateUtil.parseFromUTC(temp.getString("goingDate")));


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
//                            tempUser.setBirthdate(DateUtil.parseFromDateOnly(jsonUser.getString("birthdate")));
                            Jtemp.setUser(tempUser);
                            objArr.add(Jtemp);

                        }
                        return objArr;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

        };

    }

    @Override
    public RequestService getJourneyDetails(final int id, ICallBack<Journey> callBack) {
        return new RequestService<Journey>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "getJourneyDetails");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("journeyId", id + "");
                return params;
            }

            @Override
            public Journey parseResponse(JSONObject response) {
                try {
                    Journey tempJourney = new Journey();
                    JSONObject temp = response;

                    tempJourney.setId(id);
                    tempJourney.setUser(localUser);
                    tempJourney.setStatus(temp.getInt("status"));
                    tempJourney.setGoingDate(DateUtil.parseFromUTC(temp.getString("goingDate")));
                    tempJourney.setSeats(temp.getInt("seats"));
                    tempJourney.setGenderPrefer(temp.getInt("genderPrefer"));
                    tempJourney.setStartPoint(new LatLng(temp.getDouble("startLocationX"), temp.getDouble("startLocationY")));
                    tempJourney.setEndPoint(new LatLng(temp.getDouble("endLocationX"), temp.getDouble("endLocationY")));
                    tempJourney.setCarDescription(temp.getString("carDescription"));

                    return tempJourney;

                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

        };

    }

    @Override
    public RequestService setNewJourney(final Journey newJourney, ICallBack<Integer> callBack) {
        return new RequestService<Integer>(mContext, url, callBack) {

            @Override
            public Map<String, String> getParameters() {
                User localUser = getLocalUser();
                Map<String, String> params = new HashMap<String, String>();
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                params.put("action", "setNewJourney");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("startLocationX", newJourney.getStartPoint().latitude + "");
                params.put("startLocationY", newJourney.getStartPoint().longitude + "");
                params.put("endLocationX", newJourney.getEndPoint().latitude + "");
                params.put("endLocationY", newJourney.getEndPoint().longitude + "");
                params.put("goingDate", newJourney.getGoingDate().toString());
                params.put("seats", newJourney.getSeats() + "");
                params.put("genderPrefer", newJourney.getGenderPrefer() + "");
                params.put("carDescription", newJourney.getCarDescription());
                return params;
            }

            @Override
            public Integer parseResponse(JSONObject response) {
                Log.d("tag", "response=" + response);
                try {
                    int id = Integer.parseInt(response.getString("status"));
                    if (id > 0) {

                        Log.i("tagWebApi", "Creating Journey Process Done With id : " + id);
                        return id;
                    } else {

                        Log.i("tagWebApi", "Creating Journey Process Failed");
                        return null;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };

    }

    @Override
    public RequestService filterJourneys(final LatLng startPoint, final LatLng endPoint, final Date goingDate, final int sortBy, IListCallBack<Journey> callBack) {
        return new RequestService<List<Journey>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                User localUser = getLocalUser();

                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "filterJourneys");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("startPointX", startPoint.latitude + "");
                params.put("startPointY", startPoint.longitude + "");
                params.put("endPointX", endPoint.latitude + "");
                params.put("endPointY", endPoint.longitude + "");
                params.put("goingDate", goingDate + "");
                params.put("sortBy", sortBy + "");
                return params;
            }

            @Override
            public List<Journey> parseResponse(JSONObject response) {
                try {
                    if (response.has("journeys")) {
                        JSONArray jsonArray = response.getJSONArray("journeys");
                        JSONObject jsonTemp;
                        Journey journeyTemp;
                        ArrayList<Journey> journeysArray = new ArrayList<Journey>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonTemp = jsonArray.getJSONObject(i);
                            journeyTemp = new Journey();
                            User tempUser = new User();
                            journeyTemp.setId(jsonTemp.getInt("id"));
                            journeyTemp.setStartPoint(new LatLng(jsonTemp.getDouble("startLocationX"), jsonTemp.getDouble("startLocationY")));
                            journeyTemp.setEndPoint(new LatLng(jsonTemp.getDouble("endLocationX"), jsonTemp.getDouble("endLocationY")));

                            journeyTemp.setGoingDate(DateUtil.parseFromUTC(jsonTemp.getString("goingDate")));
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
//                            tempUser.setBirthdate(DateUtil.parseFromDateOnly(jsonUser.getString("birthdate")));
                            journeyTemp.setUser(tempUser);
                            journeyTemp.setStatus(jsonTemp.getInt("status"));
                            journeysArray.add(journeyTemp);
                        }

                        return journeysArray;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("tagWebApi", "Error on JSON getting item");
                    return null;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };

    }

    @Override
    public RequestService getNumberOfJourneys(ICallBack<Integer> callBack) {
        return null;
    }

    @Override
    public RequestService changeJourneyStatus(final Journey journey, final int status, ICallBack<Boolean> callBack) {
        Log.d("tag", "journeyId=" + journey.getId());
        return new RequestService<Boolean>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                User localUser = getLocalUser();

                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "changeJourneyStatusAndGetRiders");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("journeyId", journey.getId() + "");
                params.put("status", status + "");
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Log.i("tagWebApi", "journey status changed successfully");
                        return true;

                    } else {
                        Log.i("tagWebApi", "failed to change journey status");
                        return false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService getRides(int userId, final int limitStart, final int limitNum, IListCallBack<Ride> callBack) {
        return new RequestService<List<Ride>>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getRides");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("userId", localUser.getId() + "");
                params.put("start", limitStart + "");
                params.put("num", limitNum + "");
                return params;
            }

            @Override
            public List<Ride> parseResponse(JSONObject response) {
                try {
                    if (response.has("rides")) {
                        JSONArray JArr = response.getJSONArray("rides");
                        JSONObject temp;
                        Ride Rtemp;
                        ArrayList<Ride> objArr = new ArrayList<Ride>();
                        for (int i = 0; i < JArr.length(); i++) {
                            temp = JArr.getJSONObject(i);
                            Rtemp = new Ride();
                            Rtemp.setId(temp.getInt("id"));
                            Rtemp.setMeetingLocation(new LatLng(temp.getDouble("meetingLocationX"), temp.getDouble("meetingLocationY")));
                            Rtemp.setOrderStatus(temp.getInt("orderStatus"));

                            JSONObject jsonUser = temp.getJSONObject("user");
                            User tempUser = new User();
                            tempUser.setUsername(jsonUser.getString("username"));
                            tempUser.setFullname(jsonUser.getString("fullname"));
                            tempUser.setId(jsonUser.getInt("id"));
                            tempUser.setGender(jsonUser.getInt("gender"));
                            tempUser.setPhone(jsonUser.getString("phone"));
                            tempUser.setAddress(jsonUser.getString("address"));


//                            tempUser.setBirthdate(DateUtil.parseFromDateOnly(jsonUser.getString("birthdate")));
                            Rtemp.setUser(tempUser);
                            JSONObject jsonJourney = temp.getJSONObject("journey");
                            Journey Jtemp = new Journey();
                            Jtemp.setId(jsonJourney.getInt("id"));
                            Jtemp.setStartPoint(new LatLng(jsonJourney.getDouble("startLocationX"), jsonJourney.getDouble("startLocationY")));
                            Jtemp.setEndPoint(new LatLng(jsonJourney.getDouble("endLocationX"), jsonJourney.getDouble("endLocationY")));
                            Jtemp.setGoingDate(DateUtil.parseFromUTC(jsonJourney.getString("goingDate")));
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


//                            tempJUser.setBirthdate(DateUtil.parseFromDateOnly(jsonUser.getString("birthdate")));
                            Jtemp.setUser(tempJUser);
                            Rtemp.setJourney(Jtemp);
                            objArr.add(Rtemp);

                        }
                        return objArr;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

        };
    }

    @Override
    public RequestService getRideDetails(final int rideId, ICallBack<Ride> callBack) {
        return new RequestService<Ride>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getRideDetails");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("rideId", rideId + "");
                return params;
            }

            @Override
            public Ride parseResponse(JSONObject response) {
                Ride tempRide = new Ride();
                int userId;
                User tempUser = new User();
                Journey tempJourney = new Journey();


                try {
                    JSONObject temp = response;
                    tempRide.setId(temp.getInt("id"));


                    tempUser.setUsername(localUser.getUsername());
                    tempUser.setFullname(localUser.getFullname());
                    tempUser.setId(localUser.getId());
                    tempUser.setGender(localUser.getGender());
                    tempUser.setPhone(localUser.getPhone());
                    tempUser.setAddress(localUser.getAddress());
//                    tempUser.setBirthdate(localUser.getBirthdate());
                    tempRide.setUser(tempUser);

                    JSONObject jsonJourney = temp.getJSONObject("journey");
                    tempJourney.setId(jsonJourney.getInt("id"));
                    tempJourney.setUser(tempUser);
                    tempJourney.setCarDescription(jsonJourney.getString("carDescription"));
                    tempJourney.setSeats(jsonJourney.getInt("seats"));
                    tempJourney.setGenderPrefer(jsonJourney.getInt("genderPrefer"));
                    tempJourney.setStartPoint(new LatLng(jsonJourney.getDouble("startLocationX"), jsonJourney.getDouble("startLocationY")));
                    tempJourney.setEndPoint(new LatLng(jsonJourney.getDouble("endLocationX"), jsonJourney.getDouble("endLocationY")));

                    tempJourney.setGoingDate(DateUtil.parseFromUTC(jsonJourney.getString("goingDate")));
                    tempJourney.setStatus(jsonJourney.getInt("status"));
                    tempRide.setJourney(tempJourney);
                    tempRide.setMeetingLocation(new LatLng(temp.getDouble("meetingLocationX"), temp.getDouble("meetingLocationY")));
                    tempRide.setOrderStatus(temp.getInt("orderStatus"));

                    return tempRide;

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    @Override
    public RequestService getRidersOfJourney(final Journey journey, IListCallBack<Ride> callBack) {
        return new RequestService<List<Ride>>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                final User localUser = getLocalUser();
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getRidersOfJourney");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("journeyId", journey.getId() + "");
                return params;
            }

            @Override
            public List<Ride> parseResponse(JSONObject response) {
                try {
                    if (response.has("rides")) {
                        JSONArray jsonArray = response.getJSONArray("rides");
                        JSONObject jsonTemp;
                        Ride rideTemp;
                        ArrayList<Ride> ridesArray = new ArrayList<Ride>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            rideTemp = new Ride();
                            User user = new User();

                            jsonTemp = jsonArray.getJSONObject(i);
                            JSONObject jsonObjectUser = jsonTemp.getJSONObject("user");
                            user.setFullname(jsonObjectUser.getString("fullname"));
                            user.setUsername(jsonObjectUser.getString("username"));

                            user.setGender(jsonObjectUser.getInt("gender"));
                            user.setImageurl(jsonObjectUser.getString("image"));
                            user.setPhone(jsonObjectUser.getString("phone"));

                            rideTemp.setId(jsonTemp.getInt("id"));
                            rideTemp.setJourney(journey);
                            rideTemp.setOrderStatus(jsonTemp.getInt("orderStatus"));
                            rideTemp.setUser(user);
                            rideTemp.setMeetingLocation(new LatLng(jsonTemp.getDouble("meetingLocationX"), jsonTemp.getDouble("meetingLocationY")));
                            ridesArray.add(rideTemp);
                        }
                        return ridesArray;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    @Override
    public RequestService setRideOnJourney(final Ride newRide, final ICallBack<Integer> callBack) {
        return new RequestService<Integer>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "setRideOnJourney");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("journeyId", newRide.getJourney().getId() + "");
                params.put("meetingLocationX", newRide.getMeetingLocation().latitude + "");
                params.put("meetingLocationY", newRide.getMeetingLocation().longitude + "");
                return params;
            }

            @Override
            public Integer parseResponse(JSONObject response) {
                try {
                    int rideIdTemp;
                    if ((rideIdTemp = response.getInt("rideId")) > 0) {
                        Log.i("tagWebApi", "rideId returned successfully");
                        return rideIdTemp;

                    } else {
                        if (response.get("status").equals("noAvailableSeats")) {
                            Log.i("tagWebApi", "noAvailableSeats");
                            return -2;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return -1;
            }
        };
    }

    @Override
    public RequestService changeRideStatus(final int rideId, final int status, ICallBack<Boolean> callBack) {
        return new RequestService<Boolean>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "changeRideStatus");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("rideId", rideId + "");
                params.put("orderStatus", status + "");
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {

                        Log.i("tagWebApi", "ride status changes successfully ");
                        return true;
                    } else {

                        Log.i("tagWebApi", "error in changing ride's status ");
                        return false;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public RequestService changeMyRideStatus(final int rideId, final int journeyID, final int status, ICallBack<Boolean> callBack) {
        return new RequestService<Boolean>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "changeMyRideStatus");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("rideId", rideId + "");
                params.put("journeyId", journeyID + "");
                params.put("orderStatus", status + "");
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {

                        Log.i("tagWebApi", "ride status changes successfully ");
                        return true;
                    } else {

                        Log.i("tagWebApi", "error in changing ride's status ");
                        return false;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService getStatusOfRide(final int rideId, ICallBack<Integer> callBack) {
        return new RequestService<Integer>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getStatusOfRide");
                params.put("rideId", rideId + "");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                return params;
            }

            @Override
            public Integer parseResponse(JSONObject response) {
                try {

                    int rideStatus;
                    if (response.has("rideStatus")) {
                        rideStatus = response.getInt("rideStatus");
                        return rideStatus;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        };
    }

    @Override
    public RequestService sendUserTokenToServer(final String token, @Nullable ICallBack<Boolean> callBack) {
        return new RequestService<Boolean>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                User user = SPController.getLocalUser(mContext);
                params.put("action", "registerToken");
                params.put("token", token);
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject Response) {
                try {

                    if (Response.has("status")) {
                        if (Response.getString("status").equals("success")) {
                            return true;
                        }
                        return false;
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

        };
    }

    @Override
    public RequestService userRegister(final User user, ICallBack<Boolean> callBack) {

        return new RequestService<Boolean>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();

                //($username,$password,$fullname,$gender,$birthdate,$address,$userType,$image,$phone)
                params.put("action", "userRegister");
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());
                params.put("fullname", user.getFullname());
                params.put("gender", user.getGender() + "");


//                params.put("birthdate",user.getBirthdate().toString());
                params.put("address", user.getAddress());
                params.put("userType", "1");
                //params.put("image",user.getImageurl());
                params.put("phone", user.getPhone());
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.has("registration")) {
                        Log.i("tagWebApi", "on registration");

                        if (response.getString("registration").equals("success")) {
                            Log.i("tagWebApi", "register process is done");
                            return true;
                        } else {
                            Log.i("tagWebApi", "register process is failed");
                            return false;
                        }
                    } else {
                        Log.i("tagWebApi", "no registration");
                        return null;
                    }

                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService setUserDetails(final User user, final String OldPassword, ICallBack<Boolean> callBack) {
        return new RequestService<Boolean>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "setUserDetails");
                params.put("username", user.getUsername());
                params.put("fullname", user.getFullname());
                params.put("gender", user.getGender() + "");
//                params.put("birthdate",user.getBirthdate().toGMTString());
                params.put("address", user.getAddress());
                params.put("phone", user.getPhone());
                params.put("newPassword", user.getPassword());
                params.put("oldPassword", OldPassword);
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Log.i("tagWebApi", "setUserDetails process is done");
                        return true;
                    } else {
                        Log.i("tagWebApi", "setUserDetails process is failed");
                        return false;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService getUserDetails(final int userId, ICallBack<User> callBack) {
        return new RequestService<User>(mContext, url, callBack) {
            final User localUser = getLocalUser();

            @Override
            public Map<String, String> getParameters() {
                //final User localUser = SPController.getLocalUser(mContext);
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getUserDetails");
                params.put("username", localUser.getUsername());
                params.put("password", localUser.getPassword());
                params.put("userId", userId + "");

                return params;

            }

            @Override
            public User parseResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("events");
                    JSONObject jsonTemp;
                    User userTemp;
                    ArrayList<User> userArrayList = new ArrayList<User>();

                    if (response.has("username") && !response.isNull("username")) {

                        userTemp = new User();
                        userTemp.setUsername(localUser.getUsername());
                        userTemp.setPassword(localUser.getPassword());
                        userTemp.setFullname(response.getString("fullname"));
                        userTemp.setAddress(response.getString("address"));
                        userTemp.setPhone(response.getString("phone"));
//                        userTemp.setBirthdate(DateUtil.parseFromDateOnly(response.getString("birthdate")));
                        userTemp.setGender(response.getInt("gender"));
                        userTemp.setId(response.getInt("id"));
                        userTemp.setImageurl(response.getString("image"));
                        Log.i("tagWebApi", "Getting user details for user : " + userTemp.getUsername());
                        return userTemp;


                    } else {
                        Log.i("tagWebApi", "Getting user details");
                        return null;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService login(final String username, final String password, ICallBack<User> callBack) {
        return new RequestService<User>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                Log.i("tagWebApi", "login: u: " + username + " p: " + password);
                params.put("action", "getUserDetails");
                params.put("username", username);
                params.put("password", password);
                params.put("userId", "-1");
                return params;
            }

            @Override
            public User parseResponse(JSONObject response) {
                try {
                    if (response.has("username")) {

                        User userDetails = new User();
                        userDetails.setUsername(username);
                        userDetails.setPassword(password);
                        userDetails.setFullname(response.getString("fullname"));
//                        userDetails.setBirthdate(DateUtil.parseFromDateOnly(response.getString("birthdate")));
                        userDetails.setGender(response.getInt("gender"));
                        userDetails.setId(response.getInt("id"));
                        userDetails.setAddress(response.getString("address"));
                        userDetails.setPhone(response.getString("phone"));
                        userDetails.setImageurl(response.getString("image"));
                        Log.i("tagWebApi", "Getting user details for user : " + userDetails.getUsername());
                        return userDetails;
                    } else {

                        Log.i("tagWebApi", "Getting user details failed");
                        return null;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService checkAuth(final String username, final String password, ICallBack<Boolean> callBack) {
        return new RequestService<Boolean>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "userAuth");
                params.put("username", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Boolean parseResponse(JSONObject response) {
                try {
                    if (response.getString("auth").equals("true")) {

                        Log.i("tagWebApi", "user auth is ok");
                        return true;
                    } else {

                        Log.i("tagWebApi", "user auth is not ok");
                        return false;
                    }
                } catch (JSONException e) {
                    Log.i("tagWebApi", "Error on JSON getting item");
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public RequestService setImageForUser(final String imageUrl, final ICallBack<String> callBack) {

        return new RequestService<String>(mContext, url, callBack) {
            @Override
            public Map<String, String> getParameters() {
                User user = getLocalUser();
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "uploadUserImageFromUrl");
                params.put("username", user.getUsername());
                params.put("password", user.getPassword());
                params.put("imgUrl", imageUrl);
                return params;
            }

            @Override
            public String parseResponse(JSONObject response) {
                String imageUrlFromResponse;
                if (response.has("success")) {
                    try {
                        imageUrlFromResponse = response.getString("success");
                        Log.d("tagWebApi", "imageUrl, " + imageUrlFromResponse);
                        return imageUrlFromResponse;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }


                }
                return null;
            }



        };
    }
}
