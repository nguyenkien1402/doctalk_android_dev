package com.mobile.cometchat.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.mobile.R;
import com.mobile.cometchat.Activity.OneToOneChatActivity;
import com.mobile.cometchat.Adapter.GroupMemberListAdapter;
import com.mobile.cometchat.Contracts.MemberListFragmentContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.Helper.RecyclerTouchListener;
import com.mobile.cometchat.Presenters.MemberListFragmentPresenter;
import com.mobile.cometchat.Utils.CommonUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberListFragment extends Fragment implements MemberListFragmentContract.MemberListFragmentView {

    private static final int LIMIT = 30;

    private RecyclerView rvMembers;

    private LinearLayoutManager linearLayoutManager;

    private User user;

    private MemberListFragmentContract.MemberListFragmentPresenter memberListFragmentPresenter;

    private String groupId;

    private String ownerUid;

    private GroupMemberListAdapter groupMemberListAdapter;

    public MemberListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_list, container, false);

        memberListFragmentPresenter = new MemberListFragmentPresenter();
        memberListFragmentPresenter.attach(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvMembers = view.findViewById(R.id.rv_member);
        rvMembers.setLayoutManager(linearLayoutManager);



        if (getArguments().containsKey(StringContract.IntentStrings.INTENT_GROUP_ID)) {
            groupId = getArguments().getString(StringContract.IntentStrings.INTENT_GROUP_ID);
            memberListFragmentPresenter.initMemberList(groupId, LIMIT, getContext());
        }
        if (getArguments().containsKey(StringContract.IntentStrings.USER_ID))
        {
            ownerUid=getArguments().getString(StringContract.IntentStrings.USER_ID);
        }

        rvMembers.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvMembers, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int var2) {

                registerForContextMenu(rvMembers);

                user = (User) var1.getTag(R.string.user);

                if (!ownerUid.equals(user.getUid())) {
                    getActivity().openContextMenu(var1);
                }

            }

            @Override
            public void onLongClick(View var1, int var2) {


            }
        }));


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        memberListFragmentPresenter.detach();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_group_action, menu);

        MenuItem menuItem=menu.findItem(R.id.menu_item_Reinstate);

        menuItem.setVisible(false);
        menu.setHeaderTitle(CommonUtils.setTitle("Select Action",getContext()));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_outcast:
                showToast("outcast");
                memberListFragmentPresenter.outCastUser(user.getUid(), groupId,groupMemberListAdapter);
                break;
            case R.id.menu_item_kick:
                memberListFragmentPresenter.kickUser(user.getUid(), groupId,groupMemberListAdapter);
                showToast("kick");
                break;
            case R.id.menu_view_profile:
                Intent intent = new Intent(getContext(), OneToOneChatActivity.class);
                intent.putExtra(StringContract.IntentStrings.USER_ID, user.getUid());
                intent.putExtra(StringContract.IntentStrings.USER_NAME, user.getName());
                startActivity(intent);
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setAdapter(List<GroupMember> groupMemberList) {

        if (groupMemberListAdapter == null) {
            groupMemberListAdapter = new GroupMemberListAdapter(groupMemberList, getContext(),ownerUid);
            rvMembers.setAdapter(groupMemberListAdapter);
        } else {
            groupMemberListAdapter.notifyDataSetChanged();
        }
    }


}
