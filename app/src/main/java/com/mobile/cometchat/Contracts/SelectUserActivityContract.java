package com.mobile.cometchat.Contracts;

import android.content.Intent;

import com.cometchat.pro.models.User;
import com.mobile.cometchat.Activity.SelectUserActivity;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.HashMap;
import java.util.Set;

public interface SelectUserActivityContract {


    interface SelectUserActivityView{

        void setScope(String scope);

        void setGUID(String guid);

        void setContactAdapter(HashMap<String, User> userHashMap);
    }

    interface SelectUserActivityPresenter extends BasePresenter<SelectUserActivityView> {

        void getIntent(Intent intent);

        void getUserList(int i);


        void addMemberToGroup(String guid, SelectUserActivity selectUserActivity, Set<String> keySet);
    }
}