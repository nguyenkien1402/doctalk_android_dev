package com.mobile.cometchat.Helper;

public interface RecordListener {
    void onStart();
    void onCancel();
    void onFinish(long time);
    void onLessTime();
}