package com.mobile.cometchat.Contracts;

import android.content.Context;
import android.content.Intent;

import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.User;
import com.mobile.cometchat.Activity.OneToOneChatActivity;
import com.mobile.cometchat.Base.BasePresenter;
import com.mobile.cometchat.Base.BaseView;
import com.mobile.cometchat.CustomView.CircleImageView;

import java.io.File;
import java.util.List;

public interface OneToOneActivityContract {



    interface OneToOneView extends BaseView {

        void setAdapter(List<BaseMessage> messageArrayList);

        void addMessage(BaseMessage newMessage);

        void setOwnerDetail(User user);

        void setTitle(String name);

        void addSendMessage(BaseMessage baseMessage);

        void setContactUid(String stringExtra);

        void setAvatar(String stringExtra);

        void setPresence(User user);

        void setTyping();

        void endTyping();

        void setMessageDelivered(MessageReceipt messageReceipt);

        void onMessageRead(MessageReceipt messageReceipt);

        void hideBanner();

        void setDeletedMessage(BaseMessage baseMessage);

        void setEditedMessage(BaseMessage baseMessage);

        void setFilterList(List<BaseMessage> list);
    }

    interface OneToOnePresenter extends BasePresenter<OneToOneView> {

        void sendMessage(String message, String uId);

        void setContext(Context context);

        void handleIntent(Intent intent);

        void addMessageReceiveListener(String contactUid);

        void sendMediaMessage(File filepath, String receiverUid, String type);

        void fetchPreviousMessage(String contactUid, int limit);

        void getOwnerDetail();

        void removeMessageLisenter(String listenerId);

        void setContactPic(OneToOneChatActivity oneToOneChatActivity, String avatar, CircleImageView circleImageView);

        void addPresenceListener(String presenceListener);

        void sendCallRequest(Context context, String contactUid, String receiverTypeUser, String callType);

        void addCallEventListener(String callEventListener);

        void removePresenceListener(String listenerId);

        void removeCallListener(String listenerId);

        void sendTypingIndicator(String receiverId);

        void endTypingIndicator(String receiverId);

        void blockUser(String contactId);

        void unBlockUser(String uid, Context context);

        void deleteMessage(BaseMessage baseMessage);

        void editMessage(BaseMessage baseMessage, String message);

        void searchMessage(String s, String UID);
    }
}
