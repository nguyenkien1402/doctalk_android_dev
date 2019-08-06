package com.mobile.cometchat.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.mobile.R;
import com.mobile.cometchat.Adapter.ViewPagerAdapter;
import com.mobile.cometchat.Contracts.CometChatActivityContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.Fragments.ContactsFragment;
import com.mobile.cometchat.Fragments.GroupListFragment;
import com.mobile.cometchat.Helper.FabIconAnimator;
import com.mobile.cometchat.Helper.ScrollHelper;
import com.mobile.cometchat.Presenters.CometChatActivityPresenter;
import com.mobile.cometchat.Utils.FontUtils;

import java.util.HashMap;


public class CometChatActivity extends AppCompatActivity implements ScrollHelper, CometChatActivityContract.CometChatActivityView {

    private ViewPager mViewPager; //view pager

    private Toolbar toolbar;

    private AppBarLayout appBarLayout;

    private TabLayout tabs;

    private Context context;

    private FabIconAnimator fabIconAnimator;

    private ConstraintLayout container;

    private CoordinatorLayout mainContent;

    private CometChatActivityContract.CometChatActivityPresenter cometChatActivityPresenter;

    private ViewPagerAdapter adapter;

    private static final String TAG = "CometChatActivity";


    public static HashMap<String ,Integer> countMap;

    private MenuItem searchItem;

    private SearchView searchView;

    private int pageNumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comet_chat);
        context = this;
        cometChatActivityPresenter = new CometChatActivityPresenter();
        cometChatActivityPresenter.attach(this);
        initViewComponents();
    }

    private void initViewComponents() {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        new FontUtils(this);

        appBarLayout = findViewById(R.id.appbar);

        getSupportActionBar().setTitle("");

        Drawable groupDrawable = getResources().getDrawable(R.drawable.ic_group_add_white_24dp);

        container = findViewById(R.id.constraint_container);
        mainContent = findViewById(R.id.main_content);

        fabIconAnimator = new FabIconAnimator(container);
        fabIconAnimator.update(groupDrawable, R.string.group);
        fabIconAnimator.setExtended(true);

        new Thread(() -> cometChatActivityPresenter.getBlockedUser(CometChatActivity.this)).start();

        tabs = findViewById(R.id.tabs);

        fabIconAnimator.setOnClickListener(view -> startActivity(new Intent(context, CreateGroupActivity.class)));

        mViewPager = findViewById(R.id.container);

        mViewPager.setOffscreenPageLimit(2);

        setViewPager();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                adapter.notifyDataSetChanged();
                pageNumber=i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cometChatActivityPresenter.addCallEventListener(context, TAG);
        Log.d(TAG, "onResume: ");
        cometChatActivityPresenter.addMessageListener(CometChatActivity.this,TAG);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        cometChatActivityPresenter.removeMessageListener(TAG);
        cometChatActivityPresenter.removeCallEventListener(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new GroupListFragment().onActivityResult(requestCode, resultCode, data);
        mViewPager.setCurrentItem(2);
    }

    private void setViewPager() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactsFragment(), getString(R.string.contacts));
        adapter.addFragment(new GroupListFragment(), getString(R.string.group));
        mViewPager.setAdapter(adapter);
        tabs.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

       searchItem=menu.findItem(R.id.app_bar_search);

       SearchManager searchManager=((SearchManager)getSystemService(Context.SEARCH_SERVICE));

         if (searchItem!=null){

             searchView=((SearchView)searchItem.getActionView());

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                     searchUser(s);
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                     searchUser(null);
                    return false;
                }
            });
         }

         if (searchView!=null){
             searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
         }

        return true;
    }

    private void searchUser(String s) {

        switch (pageNumber){

            case 0:
                ContactsFragment contactsFragment = (ContactsFragment) adapter.getItem(0);
                contactsFragment.search(s);
                break;

            case 1:
                GroupListFragment groupListFragment= (GroupListFragment) adapter.getItem(1);
                groupListFragment.search(s);
                break;

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                User user = CometChat.getLoggedInUser();
                Intent intent = new Intent(context, UsersProfileViewActivity.class);
                intent.putExtra(StringContract.IntentStrings.PROFILE_VIEW, true);
                intent.putExtra(StringContract.IntentStrings.USER_ID, user.getUid());
                intent.putExtra(StringContract.IntentStrings.USER_AVATAR, user.getAvatar());
                intent.putExtra(StringContract.IntentStrings.USER_NAME, user.getName());
                startActivity(intent);
                break;

            case R.id.menu_log_out:
                cometChatActivityPresenter.logOut(this);
                break;

            case R.id.menu_blocked_user:
                startActivity(new Intent(this,BlockedUserListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void setFab(boolean isExtended) {
        fabIconAnimator.setExtended(isExtended);
    }

}



