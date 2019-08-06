package com.mobile.cometchat.Contracts;

import com.cometchat.pro.models.User;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.HashMap;


public interface ContactsContract {

    interface ContactView{

        void setContactAdapter(HashMap<String, User> userHashMap);

        void updatePresence(User user);

        void setLoggedInUser(User user);

        void setUnreadMap(HashMap<String, Integer> stringIntegerHashMap);

        void setFilterList(HashMap<String, User> hashMap);

    }

    interface ContactPresenter extends BasePresenter<ContactView> {

          void fetchUsers();

          void addPresenceListener(String presenceListener);

          void removePresenceListener(String string);

          void getLoggedInUser();

          void searchUser(String s);

          void fetchCount();
    }
}
