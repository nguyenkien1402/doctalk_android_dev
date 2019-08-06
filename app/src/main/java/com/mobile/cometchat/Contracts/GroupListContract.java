package com.mobile.cometchat.Contracts;

import android.app.ProgressDialog;
import android.content.Context;

import com.cometchat.pro.models.Group;
import com.mobile.cometchat.Adapter.GroupListAdapter;
import com.mobile.cometchat.Base.BasePresenter;
import com.mobile.cometchat.Base.BaseView;

import java.util.List;

public interface GroupListContract {


    interface GroupView extends BaseView {

        void setGroupAdapter(List<Group> groupList);

        void groupjoinCallback(Group group);

        void setFilterGroup(List<Group> groups);
    }

    interface GroupPresenter extends BasePresenter<GroupView>
    {
        void initGroupView();

        void joinGroup(Context context, Group group, ProgressDialog progressDialog, GroupListAdapter groupListAdapter);

        void refresh();

        void searchGroup(String s);
    }
}
