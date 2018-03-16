package com.techcamp.aauj.rawabi.API;

import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ICallBack;

/**
 * Created by ALa on 11/15/2017.
 */

public interface AuthWebApi {
    void userRegister(User user, ICallBack<Boolean> booleanITriger);
    void setUserDetails(User user,String OldPassword,ICallBack<Boolean> booleanITriger);// new password exists in user object i.e. newPassword =  user.getPassword
    void getUserDetails(int userId,ICallBack<User> resultUser);
    void login(String username,String password,ICallBack<User> resultUser);
    void checkAuth(String username,String password,ICallBack<Boolean> booleanITriger);
}
