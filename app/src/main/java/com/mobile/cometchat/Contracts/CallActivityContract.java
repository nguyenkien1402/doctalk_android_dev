package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.mobile.cometchat.Base.BasePresenter;

public interface CallActivityContract {

    interface CallActivityView{

    }

    interface CallActivityPresenter extends BasePresenter<CallActivityView> {

        void removeCallListener(String listener);

        void addCallListener(Context context, String listener);

    }
}
