package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.Announcement;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.Beans.Job;
import com.techcamp.aauj.rawabi.Beans.Transportation;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;

import java.util.ArrayList;

/**
 * Created by alaam on 11/15/2017.
 */

public interface AnnouncmentWebApi {
    void getAnnouns(IResponeTriger<ArrayList<Announcement>> eventITriger);
    void getJobs(IResponeTriger<ArrayList<Job>> triger);
    void getTransportation(IResponeTriger<Transportation> triger);
}
