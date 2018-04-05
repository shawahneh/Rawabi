package com.techcamp.aauj.rawabi.API;

import android.util.Log;

import com.techcamp.aauj.rawabi.model.Job;
import com.techcamp.aauj.rawabi.model.Journey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonToModelParser {
    public static List<Journey> parseJourneys(){

        return null;
    }

    public static List<Job> parseGetJobs(JSONObject value) throws Exception{
        if(value.has("jobs")) {

            JSONArray jsonArray = value.getJSONArray("jobs");
            JSONObject jsonTemp;
            Job jobTemp;
            ArrayList<Job> jobsArray = new ArrayList<Job>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jobTemp = new Job();
                jsonTemp = jsonArray.getJSONObject(i);
                jobTemp.setId(jsonTemp.getInt("id"));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Log.d("tag", "endDate: " + jsonTemp.getString("endDate"));
                jobTemp.setDate(simpleDateFormat.parse(jsonTemp.getString("endDate")));
                Log.d("tag", "endDate: " + simpleDateFormat.parse(jsonTemp.getString("endDate")));


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
    }
}
