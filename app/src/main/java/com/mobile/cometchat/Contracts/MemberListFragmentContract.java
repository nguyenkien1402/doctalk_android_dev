package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.cometchat.pro.models.GroupMember;
import com.mobile.cometchat.Adapter.GroupMemberListAdapter;
import com.mobile.cometchat.Base.BasePresenter;

import java.util.List;

public interface MemberListFragmentContract {


    interface MemberListFragmentView{

        void setAdapter(List<GroupMember> groupMemberList);


    }

    interface MemberListFragmentPresenter extends BasePresenter<MemberListFragmentView> {

        void initMemberList(String guid, int LIMIT, Context context);

        void outCastUser(String uid, String groupGuid, GroupMemberListAdapter groupMemberListAdapter);

        void kickUser(String uid, String groupId, GroupMemberListAdapter groupMemberListAdapter);
    }
}
