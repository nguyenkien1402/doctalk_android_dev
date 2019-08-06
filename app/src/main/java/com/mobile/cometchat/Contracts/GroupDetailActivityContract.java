package com.mobile.cometchat.Contracts;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.cometchat.pro.models.User;
import com.mobile.cometchat.Activity.GroupDetailActivity;
import com.mobile.cometchat.Base.BasePresenter;

public interface GroupDetailActivityContract {

    interface GroupDetailView{

        void setGroupName(String groupName);

        void setGroupId(String groupId);

        void setOwnerDetail(User user);

        void setGroupOwnerName(String owner);

        void setGroupIcon(String icon);

        void setGroupDescription(String description);

        void setUserScope(String scope);
    }

    interface GroupDetailPresenter extends BasePresenter<GroupDetailView> {

        void handleIntent(Intent data, Context context);

        void leaveGroup(String gUid);

        void clearConversation(String gUid);

        void setIcon(GroupDetailActivity groupDetailActivity, String icon, ImageView groupImage);


    }
}
