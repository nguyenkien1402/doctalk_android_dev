package com.mobile.cometchat.Contracts;

import android.content.Context;

import com.cometchat.pro.models.Group;
import com.mobile.cometchat.Base.BasePresenter;
import com.mobile.cometchat.Base.BaseView;


public interface CreateGroupActivityContract {

    interface CreateGroupView extends BaseView {

    }

    interface CreateGroupPresenter extends BasePresenter<CreateGroupView> {

        void createGroup(Context context, Group group);

    }
}
