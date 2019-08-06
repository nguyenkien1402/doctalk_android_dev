package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.cometchat.pro.models.User;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.HashMap;

public interface BlockedUserListActivityContract {

    interface BlockedUserListActivityView{

        void setAdapter(HashMap<String, User> userHashMap);

        void userUnBlocked(String uid);
    }

    interface BlockedUserListActivityPresenter extends BasePresenter<BlockedUserListActivityView> {

        void getBlockedUsers();

        void unBlockUser(Context context, User user);
    }
}
