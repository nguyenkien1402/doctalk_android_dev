package com.mobile.cometchat.Contracts;

import com.mobile.cometchat.Base.BasePresenter;

public interface LoginActivityContract {

    interface LoginActivityView {

        void startCometChatActivity();
    }

    interface LoginActivityPresenter extends BasePresenter<LoginActivityView> {

        void Login(String uid);

        void loginCheck();
    }
}
