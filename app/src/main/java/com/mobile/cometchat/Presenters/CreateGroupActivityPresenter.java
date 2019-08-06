package com.mobile.cometchat.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.CreateGroupActivityContract;
import com.mobile.cometchat.Utils.CommonUtils;


public class CreateGroupActivityPresenter extends Presenter<CreateGroupActivityContract.CreateGroupView>
        implements CreateGroupActivityContract.CreateGroupPresenter {


    @Override
    public void createGroup(final Context context, Group group) {


        CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {

                CommonUtils.startActivityIntent(group, context, true, null);

                Toast.makeText(context, group.getGroupType() + " group created ", Toast.LENGTH_SHORT).show();

                Log.d("createGroup", "onSuccess: "+group);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("createGroup", "onError: "+e.getMessage());
            }

        });

    }
}
