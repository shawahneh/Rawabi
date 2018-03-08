package com.techcamp.aauj.rawabi;

/**
 * Created by alaam on 12/7/2017.
 */

public interface ICallBack<T> {
    void onResponse(T item);
    void onError(String err);
}
