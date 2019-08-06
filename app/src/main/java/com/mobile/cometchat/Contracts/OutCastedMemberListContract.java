package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.cometchat.pro.models.GroupMember;
import com.mobile.cometchat.Adapter.GroupMemberListAdapter;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.List;

public interface OutCastedMemberListContract {

    interface OutCastedMemberListView{

        void setAdapter(List<GroupMember> list);
    }

    interface OutCastedMemberListPresenter extends BasePresenter<OutCastedMemberListView>
    {

        void initMemberList(String groupId, int limit, Context context);

        void reinstateUser(String uid, String groupId, GroupMemberListAdapter groupMemberListAdapter);
    }


}
