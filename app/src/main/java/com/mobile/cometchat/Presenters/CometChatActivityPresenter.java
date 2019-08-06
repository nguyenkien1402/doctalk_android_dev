package com.mobile.cometchat.Presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.BlockedUsersRequest;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.mobile.cometchat.Activity.CometChatActivity;
import com.mobile.cometchat.Activity.LoginActivity;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.CometChatActivityContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.Helper.PreferenceHelper;
import com.mobile.cometchat.Utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CometChatActivityPresenter extends Presenter<CometChatActivityContract.CometChatActivityView>
implements CometChatActivityContract.CometChatActivityPresenter {

    private static final String TAG = "CometChatActivityPresen";

    @Override
    public void addMessageListener(final Context context, String listnerId) {

        CometChat.addMessageListener(listnerId, new CometChat.MessageListener() {

            @Override
            public void onTextMessageReceived(TextMessage message) {

            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {

            }
        });
    }

    @Override
    public void removeMessageListener(String listenerId) {
        CometChat.removeMessageListener(listenerId);
    }

    @Override
    public void addCallEventListener(Context context,String listenerId) {
        CometChat.addCallListener(listenerId, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {

                Log.d(TAG, "onIncomingCallReceived: "+call.toString());

                        if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {

                            CommonUtils.startCallIntent(context, (User) call.getCallInitiator(), call.getType(),
                                    false, call.getSessionId());
                        } else if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {

                            CommonUtils.startCallIntent(context, (Group) call.getCallReceiver(), call.getType(),
                                    false, call.getSessionId());
                        }
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {

                Log.d(TAG, "onOutgoingCallAccepted: "+call.toString());
            }

            @Override
            public void onOutgoingCallRejected(Call call) {

                Log.d(TAG, "onOutgoingCallRejected: "+call.toString());
            }

            @Override
            public void onIncomingCallCancelled(Call call) {

                Log.d(TAG, "onIncomingCallCancelled: "+call.toString());
            }

        });
    }

    @Override
    public void removeCallEventListener(String tag) {
        CometChat.removeCallListener(tag);
    }

    @Override
    public void getBlockedUser(Context context) {

        PreferenceHelper.init(context);

        JSONObject jsonObject=new JSONObject();

        BlockedUsersRequest blockedUsersRequest = new BlockedUsersRequest.BlockedUsersRequestBuilder().setLimit(100).build();

        blockedUsersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                for(User user : users){
                    try {
                        jsonObject.put(user.getUid(),user.getUid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                PreferenceHelper.saveString(StringContract.PreferenceString.BLOCKED_USERS,jsonObject.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, e.getMessage());
            }
        });

    }

    @Override
    public void logOut(Context context) {
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Intent intent=new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((CometChatActivity)context).finish();
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });
    }



}
