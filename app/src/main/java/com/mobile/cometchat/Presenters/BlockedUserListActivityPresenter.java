package com.mobile.cometchat.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.BlockedUsersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.BlockedUserListActivityContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockedUserListActivityPresenter extends Presenter<BlockedUserListActivityContract.BlockedUserListActivityView> implements BlockedUserListActivityContract.BlockedUserListActivityPresenter{

    private static final String TAG = "BlockedUserListActivity";

    private HashMap<String, User> userHashMap=new HashMap<>();

    @Override
    public void getBlockedUsers() {

       BlockedUsersRequest blockedUsersRequest = new BlockedUsersRequest.BlockedUsersRequestBuilder().setLimit(50).build();

        blockedUsersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                for(User user : users){
                  Log.d(TAG, "blockedUsersRequest onSuccess: "+user.toString());
                  userHashMap.put(user.getUid(),user);
                  getBaseView().setAdapter(userHashMap);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "blockedUsersRequest "+e.getMessage());
            }
        });


    }

    @Override
    public void unBlockUser(Context context, User user) {
        List<String > uids=new ArrayList<>();
        uids.add(user.getUid());

        CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();

                getBaseView().userUnBlocked(user.getUid());
            }

            @Override
            public void onError(CometChatException e) {

            }
        });

    }
}
