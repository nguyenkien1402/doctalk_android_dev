package com.mobile.cometchat.Presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.mobile.R;
import com.mobile.cometchat.Activity.OneToOneChatActivity;
import com.mobile.cometchat.Base.Presenter;
import com.mobile.cometchat.Contracts.OneToOneActivityContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.CustomView.CircleImageView;
import com.mobile.cometchat.Utils.CommonUtils;
import com.mobile.cometchat.Utils.Logger;
import com.mobile.cometchat.Utils.MediaUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OneToOneActivityPresenter extends Presenter<OneToOneActivityContract.OneToOneView>
        implements OneToOneActivityContract.OneToOnePresenter {


    private Context context;

    private MessagesRequest messagesRequest;

    private static final String TAG = "OneToOneActivityPresent";

    @Override
    public void sendMessage(String message, String uId) {

       TextMessage textMessage = new TextMessage(uId, message, CometChatConstants.MESSAGE_TYPE_TEXT, CometChatConstants.RECEIVER_TYPE_USER);

        if (OneToOneChatActivity.isReply) {
            textMessage.setMetadata(OneToOneChatActivity.metaData);
            OneToOneChatActivity.hideReply();
        }

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
             @Override
            public void onSuccess(TextMessage textMessage1) {
                if (isViewAttached()) {
                    MediaUtils.playSendSound(context, R.raw.send);
                    getBaseView().addSendMessage(textMessage1);
                    Log.d(TAG, "sendMessage onSuccess: "+textMessage1.toString());
                }
            }

             @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "sendMessage onError: " + e.getMessage());
            }
        });

    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }


    @Override
    public void handleIntent(Intent intent) {

        if (intent.hasExtra(StringContract.IntentStrings.USER_ID)) {
            String uid = intent.getStringExtra(StringContract.IntentStrings.USER_ID);
            if (isViewAttached()) {
                getBaseView().setContactUid(uid);
                OneToOneChatActivity.contactId = uid;
            }

            CometChat.getUser(uid, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    if (isViewAttached()) {
                        Log.d(TAG, "getUser onSuccess: "+user.toString());
                        getBaseView().setPresence(user);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "onError: " + e.getMessage());
                }
            });
        }
        if (intent.hasExtra(StringContract.IntentStrings.USER_AVATAR)) {
            if (isViewAttached())
                getBaseView().setAvatar(intent.getStringExtra(StringContract.IntentStrings.USER_AVATAR));
        }
        if (intent.hasExtra(StringContract.IntentStrings.USER_NAME)) {
            if (isViewAttached())
                getBaseView().setTitle(intent.getStringExtra(StringContract.IntentStrings.USER_NAME));
        }
    }

    @Override
    public void addMessageReceiveListener(final String contactUid) {

        CometChat.addMessageListener(context.getString(R.string.message_listener), new CometChat.MessageListener() {

            @Override
            public void onTextMessageReceived(TextMessage message) {
                if (isViewAttached()) {
                    Log.d(TAG, "onTextMessageReceived: "+message.toString());
                    if (message.getSender().getUid().equals(contactUid)) {
                        MediaUtils.playSendSound(context, R.raw.receive);
                        Log.d(TAG, "onTextMessageReceived: "+message.toString());
                        getBaseView().addSendMessage(message);
                    }
                }
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                if (isViewAttached()) {
                    if (message.getSender().getUid().equals(contactUid)) {
                        MediaUtils.playSendSound(context, R.raw.receive);
                        getBaseView().addSendMessage(message);
                    }
                }
            }

            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                getBaseView().setTyping();
                Log.d(TAG, "onTypingStarted: ");
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                getBaseView().endTyping();
                Log.d(TAG, "onTypingEnded:");
            }

            @Override
            public void onMessageDelivered(MessageReceipt messageReceipt) {
                Log.d(TAG, "onMessageDelivered: "+messageReceipt);
                getBaseView().setMessageDelivered(messageReceipt);

            }

            @Override
            public void onMessageRead(MessageReceipt messageReceipt) {
                Log.d(TAG, "onMessageRead: "+messageReceipt.toString());
                getBaseView().onMessageRead(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                getBaseView().setEditedMessage(message);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                getBaseView().setDeletedMessage(message);
            }

        });



    }

    @Override
    public void sendMediaMessage(File filepath, String receiverUid, String type) {

        final MediaMessage mediaMessage = new MediaMessage(receiverUid, filepath, type,
                CometChatConstants.RECEIVER_TYPE_USER);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("path", filepath.getAbsolutePath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mediaMessage.setMetadata(jsonObject);

        if (OneToOneChatActivity.isReply) {
            mediaMessage.setMetadata(OneToOneChatActivity.metaData);
            OneToOneChatActivity.hideReply();
        }

        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                MediaUtils.playSendSound(context, R.raw.send);
                getBaseView().addMessage(mediaMessage);
            }

            @Override
            public void onError(CometChatException e) {
            }

        });
    }

    @Override
    public void fetchPreviousMessage(String contactUid, int limit) {

        List<BaseMessage> list=new ArrayList<>();
        if (messagesRequest == null) {

            messagesRequest = new MessagesRequest.MessagesRequestBuilder().setUID(contactUid).setLimit(limit).build();
            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> baseMessages) {
                    if (isViewAttached()) {
                        for (BaseMessage baseMessage : baseMessages) {
                            Log.d(TAG, "onSuccess: delete "+baseMessage.getDeletedAt());
                            if (!baseMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION)&&baseMessage.getDeletedAt()==0) {
                                list.add(baseMessage);
                            }
                        }
                        getBaseView().setAdapter(list);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, " onError: "+e.getMessage());
                }

            });
        } else {
            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> baseMessages) {

                    Logger.error("old message request obj");

                    if (baseMessages.size() != 0) {
                        if (isViewAttached()) {
                            for (BaseMessage baseMessage : baseMessages) {
                                if (!baseMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION)&&baseMessage.getDeletedAt()==0) {
                                    list.add(baseMessage);
                                }
                            }
                            getBaseView().setAdapter(list);
                        }
                    }
                }

                @Override
                public void onError(CometChatException e) {

                }

            });
        }
    }

    @Override
    public void getOwnerDetail() {

        User user = CometChat.getLoggedInUser();
        if (user != null) {
            if (isViewAttached())
                getBaseView().setOwnerDetail(user);
        }
    }

    @Override
    public void addPresenceListener(String presenceListener) {
        CometChat.addUserListener(presenceListener, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                if (isViewAttached())
                    getBaseView().setPresence(user);
            }

            @Override
            public void onUserOffline(User user) {
                if (isViewAttached())
                    getBaseView().setPresence(user);
            }
        });
    }

    @Override
    public void sendCallRequest(Context context, String contactUid, String receiverTypeUser, String callType) {

        Call call = new Call(contactUid, receiverTypeUser, callType);
        CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                CommonUtils.startCallIntent(context, ((User) call.getCallReceiver()), call.getType(), true, call.getSessionId());
            }

            @Override
            public void onError(CometChatException e) {

            }

        });
    }

    @Override
    public void addCallEventListener(String callEventListener) {

        CometChat.addCallListener(callEventListener, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
              CommonUtils.startCallIntent(context, (User) call.getCallInitiator(), call.getType(), false, call.getSessionId());
            }
            @Override
            public void onOutgoingCallAccepted(Call call) {

            }
            @Override
            public void onOutgoingCallRejected(Call call) {

            }
            @Override
            public void onIncomingCallCancelled(Call call) {

            }
        });
    }

    @Override
    public void removePresenceListener(String listenerId) {
        CometChat.removeUserListener(listenerId);
    }

    @Override
    public void removeCallListener(String listenerId) {
        CometChat.removeCallListener(listenerId);
    }

    @Override
    public void sendTypingIndicator(String receiverId) {
        TypingIndicator typingIndicator = new TypingIndicator(receiverId, CometChatConstants.RECEIVER_TYPE_USER);
        Log.d(TAG, "sendTypingIndicator: ");
        CometChat.startTyping(typingIndicator);
    }

    @Override
    public void endTypingIndicator(String receiverId) {
        Log.d(TAG, "endTypingIndicator: ");
        TypingIndicator typingIndicator = new TypingIndicator(receiverId, CometChatConstants.RECEIVER_TYPE_USER);
        CometChat.endTyping(typingIndicator);
    }

    @Override
    public void blockUser(String contactId) {

        List<String> uids=new ArrayList<>();
          uids.add(contactId);

         CometChat.blockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Toast.makeText(context, "Blocked Successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, " blockUsers onSuccess: "+stringStringHashMap.toString());
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "blockUsers onError: "+e.getMessage());
            }
        });
    }

    @Override
    public void unBlockUser(String uid, Context context) {
        List<String> uids = new ArrayList<>();
        uids.add(uid);
        CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Toast.makeText(context, "Unblocked Successfully", Toast.LENGTH_SHORT).show();
                 getBaseView().hideBanner();
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    @Override
    public void deleteMessage(BaseMessage baseMessage) {

        CometChat.deleteMessage(baseMessage.getId(), new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                Log.d(TAG, "onSuccess: deleteMessage "+baseMessage.toString());
                getBaseView().setDeletedMessage(baseMessage);
            }
            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: deleteMessage");
            }
        });
    }

    @Override
    public void editMessage(BaseMessage baseMessage, String newMessage) {
        TextMessage textMessage=new TextMessage(baseMessage.getReceiverUid(),newMessage, baseMessage.getType(),baseMessage.getReceiverType());
       textMessage.setId(baseMessage.getId());
      CometChat.editMessage(textMessage, new CometChat.CallbackListener<BaseMessage>() {
          @Override
          public void onSuccess(BaseMessage baseMessage) {
              Log.d(TAG, "editMessage onSuccess: "+baseMessage.toString());
              getBaseView().setEditedMessage(baseMessage);
          }

          @Override
          public void onError(CometChatException e) {
              Log.d(TAG, "editMessage onError: "+e.getMessage());
          }
      });
    }

    @Override
    public void searchMessage(String s,String UID) {
        List<BaseMessage> list=new ArrayList<>();

        messagesRequest=null;
        MessagesRequest searchMessageRequest=new MessagesRequest.MessagesRequestBuilder()
                .setUID(UID).setSearchKeyword(s).setLimit(30).build();

        searchMessageRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> baseMessages) {
                    if (isViewAttached()) {
                        for (BaseMessage baseMessage : baseMessages) {
                            Log.d(TAG, "onSuccess: delete "+baseMessage.getDeletedAt());
                            if (!baseMessage.getCategory().equals(CometChatConstants.CATEGORY_ACTION)&&baseMessage.getDeletedAt()==0) {
                                list.add(baseMessage);
                            }
                        }
                        getBaseView().setFilterList(list);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, " onError: "+e.getMessage());
                }
            });

    }

    @Override
    public void removeMessageLisenter(String listenerId) {
        CometChat.removeMessageListener(listenerId);
    }

    @Override
    public void setContactPic(OneToOneChatActivity oneToOneChatActivity, String avatar, CircleImageView circleImageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(oneToOneChatActivity.getResources().getDrawable(R.drawable.ic_broken_image));
        Glide.with(oneToOneChatActivity).load(avatar).apply(requestOptions).into(circleImageView);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
