package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.IResponeTriger;
import com.techcamp.aauj.rawabi.ITriger;

/**
 * Created by alaam on 11/15/2017.
 */

public interface AuthWebApi {
    void userRegister(User user, IResponeTriger<Boolean> booleanITriger);
    void setUserDetails(User user,String OldPassword,IResponeTriger<Boolean> booleanITriger);
    void getUserDetails(int userId,IResponeTriger<User> resultUser);
    void login(String username,String password,IResponeTriger<User> resultUser);
    void checkAuth(String username,String password,IResponeTriger<Boolean> booleanITriger);
}
