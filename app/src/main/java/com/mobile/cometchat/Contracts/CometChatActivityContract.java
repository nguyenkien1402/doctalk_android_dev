package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.mobile.cometchat.Base.BasePresenter;

public interface CometChatActivityContract {

    interface CometChatActivityView {
    }

    interface CometChatActivityPresenter extends BasePresenter<CometChatActivityView> {

        void addMessageListener(Context context, String listenerId);

        void removeMessageListener(String listenerId);

        void addCallEventListener(Context context, String listenerId);

        void removeCallEventListener(String tag);

        void getBlockedUser(Context context);

        void logOut(Context context);

    }
}
