package com.mobile.cometchat.Presenters;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.LoginActivityContract;
import com.mobile.cometchat.Contracts.StringContract;

public class LoginAcitivityPresenter extends Presenter<LoginActivityContract.LoginActivityView> implements
LoginActivityContract.LoginActivityPresenter{


    @Override
    public void Login(String uid) {

        CometChat.login(uid, StringContract.AppDetails.API_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                getBaseView().startCometChatActivity();
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }

        });
    }
    @Override
    public void loginCheck() {

        try {
            if (CometChat.getLoggedInUser()!=null)
            {    if (isViewAttached())
               getBaseView().startCometChatActivity();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
