package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;

/**
 * Created by alaam on 11/15/2017.
 */

public interface AuthWebApi {
    void userRegister(User user, ITriger<Boolean> booleanITriger);
    void setUserDetails(User user,String OldPassword,ITriger<Boolean> booleanITriger);
    void checkAuth(String id,String pass,ITriger<Boolean> booleanITriger);
}
