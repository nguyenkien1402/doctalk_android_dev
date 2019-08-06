package com.mobile.cometchat.Base;

public interface BasePresenter<V> {

    void attach(V baseView);

    void detach();


}
