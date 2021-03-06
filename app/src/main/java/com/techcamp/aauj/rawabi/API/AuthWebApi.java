package com.techcamp.aauj.rawabi.API;

import android.content.Context;
import android.net.Uri;

import com.techcamp.aauj.rawabi.API.services.RequestService;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

/**
 * Created by ALa on 11/15/2017.
 */

public interface AuthWebApi {

    /**
     * userRegister
     * this method used for register new user
     * @param user : input, new user
     * @param callBack : if registration succeed -> return true, else -> return false
     */
    RequestService userRegister(User user, ICallBack<Boolean> callBack);

    /** setUserDetails
     *  this method used for update current user info
     * @param user : current user, the object holds the new password i.e. newPassword =  user.getPassword
     * @param OldPassword : old password entered in passwordEditText
     * @param callBack : return true or false
     */
    RequestService setUserDetails(User user, String OldPassword, ICallBack<Boolean> callBack);

    /** getUserDetails
     * this method used to get current user info
     * @param userId : current user id
     * @param callBack : if operation succeed -> return user = value, else return user = null
     */
    RequestService getUserDetails(int userId,ICallBack<User> callBack);

    /** login
     * this method used to login
     * @param username : email from input form
     * @param password : password from input form
     * @param callBack : if username and password valid -> return the user match, else -> return null
     */
    RequestService login(String username,String password,ICallBack<User> callBack);

    /**
     * checkAuth
     * this method is unused for now
     * @param username : email
     * @param password : password
     * @param callBack : ...
     */
    RequestService checkAuth(String username,String password,ICallBack<Boolean> callBack);


    RequestService setImageForUser(String imageUrl, ICallBack<String> callBack);
}
