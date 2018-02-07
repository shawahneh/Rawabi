package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.MediaItem;
import com.techcamp.aauj.rawabi.IResponeTriger;

import java.util.ArrayList;

/**
 * Created by alaam on 2/3/2018.
 */

public interface BasicApi {
    void getMedia(IResponeTriger<ArrayList<MediaItem>> triger);
}
