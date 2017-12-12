package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.MyPlace;
import com.techcamp.aauj.rawabi.IResponeTriger;

import java.util.List;

/**
 * Created by alaam on 12/7/2017.
 */

public interface PoolingPlace {
    void getPlaces(IResponeTriger<List<MyPlace>> listIResponeTriger);
}
