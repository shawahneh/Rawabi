package com.techcamp.aauj.rawabi;

/**
 * Created by ALa on 12/7/2017.
 */

/** Callback interface for delivering parsed responses. */
public interface ICallBack<T> {
    /** Called when a response is received. */
    void onResponse(T item);
    /**Called when an error has been occurred */
    void onError(String err);
}
