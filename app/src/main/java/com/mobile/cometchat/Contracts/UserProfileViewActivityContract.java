package com.mobile.cometchat.Contracts;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.User;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.List;

public interface UserProfileViewActivityContract {

    interface UserProfileActivityView{

        void setTitle(String name);

        void setStatus(User user);

        void setUserImage(String avatar);

        void setUserId(String uid);

        void setAdapter(List<MediaMessage> messageList);
    }

    interface UserProfileActivityPresenter extends BasePresenter<UserProfileActivityView> {

        void handleIntent(Intent data);

        void setContactAvatar(Context context, String avatar, ImageView userAvatar);

        void addUserPresenceListener(String presenceListener);

        void removeUserPresenceListener(String presenceListener);


        void sendCallRequest(Context context, String contactUid, String receiverType, String callTyp);

        void getMediaMessage(String contactUid, int limit);
    }
}
