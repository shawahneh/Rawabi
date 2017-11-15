package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.DriverUser;
import com.techcamp.aauj.rawabi.Beans.RiderUser;
import com.techcamp.aauj.rawabi.ITriger;

/**
 * Created by alaam on 11/15/2017.
 */

public interface AuthWebApi {
    void RiderRegister(RiderUser user, ITriger<Boolean> booleanITriger);
    void DriverRegister(DriverUser user, ITriger<Boolean> booleanITriger);
    void checkAuth(String username,String pass,ITriger<Boolean> booleanITriger);

}
