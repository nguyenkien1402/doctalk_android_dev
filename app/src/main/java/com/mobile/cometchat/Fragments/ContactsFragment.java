package com.mobile.cometchat.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mobile.R;
import com.mobile.cometchat.Activity.OneToOneChatActivity;
import com.mobile.cometchat.Adapter.ContactListAdapter;
import com.mobile.cometchat.Contracts.ContactsContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.Helper.RecyclerTouchListener;
import com.mobile.cometchat.Helper.ScrollHelper;
import com.mobile.cometchat.Presenters.ContactsListPresenter;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */

public class ContactsFragment extends Fragment implements ContactsContract.ContactView{

    private ContactListAdapter contactListAdapter;

    private RecyclerView contactRecyclerView;

    private ImageView ivNoContacts;

    private TextView tvNoContacts;

    private ContactsContract.ContactPresenter contactPresenter;

    private ScrollHelper scrollHelper;

    private User user;

    private ShimmerFrameLayout contactShimmer;

    private LinearLayoutManager linearLayoutManager;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        ivNoContacts = view.findViewById(R.id.ivNoContacts);
        tvNoContacts=view.findViewById(R.id.tvNoContacts);
        contactRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactShimmer=view.findViewById(R.id.contact_shimmer);

        linearLayoutManager=new LinearLayoutManager(getContext());
        contactRecyclerView.setLayoutManager(linearLayoutManager);
        contactRecyclerView.setItemAnimator(new DefaultItemAnimator());

        contactPresenter = new ContactsListPresenter();

        contactPresenter.attach(this);

        contactPresenter.getLoggedInUser();

        new Thread(() -> contactPresenter.fetchUsers()).start();



        contactRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                contactRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int position) {

                String contactID = (String) var1.getTag(R.string.contact_id);
                String contactName = (String) var1.getTag(R.string.contact_name);
                String userAvatar = (String) var1.getTag(R.string.user_avatar);
                User user= (User) var1.getTag(R.string.user);
                ContactListAdapter.ContactViewHolder contactViewHolder = (ContactListAdapter.ContactViewHolder) var1.getTag(R.string.userHolder);

                Pair<View,String > p1= Pair.create(((View)contactViewHolder.avatar),"profilePic");
                Pair<View,String > p2= Pair.create(((View)contactViewHolder.userName),"Name");
                Pair<View,String > p3= Pair.create(((View)contactViewHolder.userStatus),"status");
                Intent intent=new Intent(getContext(), OneToOneChatActivity.class);
                intent.putExtra(StringContract.IntentStrings.USER_ID,contactID);
                intent.putExtra(StringContract.IntentStrings.USER_AVATAR,userAvatar);
                intent.putExtra(StringContract.IntentStrings.USER_NAME,contactName);
                ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2,p3);
                startActivity(intent,optionsCompat.toBundle());

            }

            @Override
            public void onLongClick(View var1, int position) {
            }
        }));

             contactRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                 @Override
                 public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                     if (!recyclerView.canScrollVertically(1)) {
                         contactPresenter.fetchUsers();
                     }

                 }

                 @Override
                 public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                     super.onScrolled(recyclerView, dx, dy);
                     if (dy < 0) {
                         scrollHelper.setFab(true);
                     } else
                         scrollHelper.setFab(false);
                 }
             });


        return view;
    }

    @Override
    public void updatePresence(User user) {
        if (contactListAdapter != null) {
            contactListAdapter.updateUserPresence(user);
        }
    }

    @Override
    public void setLoggedInUser(User user) {
        this.user=user;
    }

    @Override
    public void setUnreadMap(HashMap<String, Integer> stringIntegerHashMap) {
         if (contactListAdapter!=null) {
             contactListAdapter.setUnreadCount(stringIntegerHashMap);
         }
    }

    @Override
    public void setFilterList(HashMap<String, User> hashMap) {
        contactListAdapter.setFilterList(hashMap);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contactPresenter.removePresenceListener(getString(R.string.presenceListener));
    }

    @Override
    public void onStart() {
        super.onStart();
        contactPresenter.addPresenceListener(getString(R.string.presenceListener));
        contactPresenter.fetchCount();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollHelper = (ScrollHelper) context;

    }

    @Override
    public void setContactAdapter(HashMap<String, User> userHashMap) {

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(userHashMap, getContext(), R.layout.contact_list_item,false);
            contactRecyclerView.setAdapter(contactListAdapter);
            contactShimmer.stopShimmer();
            contactShimmer.setVisibility(View.GONE);
        } else {
            if (userHashMap != null) {
                contactListAdapter.refreshData(userHashMap);
            }
        }

        if (userHashMap != null && userHashMap.size() == 0) {
            tvNoContacts.setVisibility(View.VISIBLE);
            ivNoContacts.setVisibility(View.VISIBLE);
        } else {
            tvNoContacts.setVisibility(View.GONE);
            ivNoContacts.setVisibility(View.GONE);
        }

    }

    public void search(String s) {
        contactPresenter.searchUser(s);
    }
}
