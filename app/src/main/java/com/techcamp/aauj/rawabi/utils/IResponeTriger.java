package com.techcamp.aauj.rawabi.utils;

/**
 * Created by alaam on 12/7/2017.
 */

public interface IResponeTriger<T> {
    void onResponse(T item);
    void onError(String err);
}
