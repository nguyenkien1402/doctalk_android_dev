package com.mobile.cometchat.Presenters;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.mobile.R;
import com.mobile.cometchat.Activity.GroupDetailActivity;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.GroupDetailActivityContract;
import com.mobile.cometchat.Contracts.StringContract;

public class GroupDetailActivityPresenter extends Presenter<GroupDetailActivityContract.GroupDetailView>
        implements GroupDetailActivityContract.GroupDetailPresenter {


    private Context context;

    @Override
    public void handleIntent(Intent data, Context context) {
        this.context = context;

        if (data.hasExtra(StringContract.IntentStrings.INTENT_GROUP_ID)) {
            String groupId = data.getStringExtra(StringContract.IntentStrings.INTENT_GROUP_ID);

            CometChat.getGroup(groupId, new CometChat.CallbackListener<Group>() {
                @Override
                public void onSuccess(Group group) {
                    if (isViewAttached()) {
                        getBaseView().setGroupId(group.getGuid());
                        getBaseView().setGroupName(group.getName());
                        getBaseView().setGroupOwnerName(group.getOwner());
                        getBaseView().setGroupIcon(group.getIcon());
                        getBaseView().setUserScope(group.getScope());
                        getBaseView().setGroupDescription(group.getDescription());
                    }
                }

                @Override
                public void onError(CometChatException e) {

                }

            });

        }

        if (CometChat.getLoggedInUser() != null) {
            if (isViewAttached())
            getBaseView().setOwnerDetail(CometChat.getLoggedInUser());
        }
    }

    @Override
    public void leaveGroup(String gUid) {

        CometChat.leaveGroup(gUid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Toast.makeText(context, "You left the group", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CometChatException e) {

            }
        });

    }

    @Override
    public void clearConversation(String gUid) {

    }

    @Override
    public void setIcon(GroupDetailActivity groupDetailActivity, String icon, ImageView groupImage) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(groupDetailActivity.getResources().getDrawable(R.drawable.ic_broken_image));
        Glide.with(groupDetailActivity).load(icon).apply(requestOptions).into(groupImage);
    }

}
