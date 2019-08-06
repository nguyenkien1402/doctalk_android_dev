package com.mobile.cometchat.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.mobile.R;
import com.mobile.cometchat.Adapter.ViewPagerAdapter;
import com.mobile.cometchat.Contracts.GroupDetailActivityContract;
import com.mobile.cometchat.Contracts.StringContract;
import com.mobile.cometchat.Fragments.MemberListFragment;
import com.mobile.cometchat.Fragments.OutcastMemberListFragment;
import com.mobile.cometchat.Presenters.GroupDetailActivityPresenter;
import com.mobile.cometchat.Utils.FontUtils;
import com.mobile.cometchat.Utils.MediaUtils;

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener,
        GroupDetailActivityContract.GroupDetailView {

    private TextView toolbarSubtitle;

    private ImageView groupImage;

    private GroupDetailActivityContract.GroupDetailPresenter groupDetailPresenter;

    private CollapsingToolbarLayout collapsingToolbar;

    private String groupId;

    private String groupName;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private Bundle bundle;

    private String ownerUid;

    private TextView tvGroupDescriptionLabel;

    private TextView tvGroupDescription;

    private TextView tvAddMembers;

    private ViewPagerAdapter adapter;

    private String scope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        new FontUtils(this);
        groupDetailPresenter = new GroupDetailActivityPresenter();
        groupDetailPresenter.attach(this);
        initViewComponent();
    }

    private void initViewComponent() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = new Bundle();
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.primaryLightColor));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.primaryLightColor));
        collapsingToolbar.setCollapsedTitleTypeface(FontUtils.robotoMedium);
        collapsingToolbar.setExpandedTitleTypeface(FontUtils.robotoRegular);
        collapsingToolbar.setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);

        tvGroupDescription = findViewById(R.id.tv_group_description);
        tvAddMembers=findViewById(R.id.addMember);
        tvAddMembers.setVisibility(View.GONE);
        tvAddMembers.setOnClickListener(this);
        tvGroupDescription.setTypeface(FontUtils.robotoRegular);

        tvGroupDescriptionLabel = findViewById(R.id.group_description_labal);
        tvGroupDescriptionLabel.setTypeface(FontUtils.robotoMedium);

        toolbarSubtitle = findViewById(R.id.toolbarSubTitle);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.container);
        groupImage = findViewById(R.id.ivGroupImage);
        groupDetailPresenter.handleIntent(getIntent(), this);


    }

    private void setViewPager() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        MemberListFragment memberListFragment = new MemberListFragment();
        OutcastMemberListFragment outcastMemberListFragment = new OutcastMemberListFragment();

        bundle.putString(StringContract.IntentStrings.INTENT_GROUP_ID, groupId);
        bundle.putString(StringContract.IntentStrings.USER_ID, ownerUid);

        memberListFragment.setArguments(bundle);
        outcastMemberListFragment.setArguments(bundle);

        adapter.addFragment(memberListFragment, getString(R.string.member));
        adapter.addFragment(outcastMemberListFragment, getString(R.string.ban_member));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupDetailPresenter.detach();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.addMember:
                Intent addMemberIntent=new Intent(this,SelectUserActivity.class);
                addMemberIntent.putExtra(StringContract.IntentStrings.INTENT_GROUP_ID,groupId);
                addMemberIntent.putExtra(StringContract.IntentStrings.INTENT_SCOPE,scope);
                startActivity(addMemberIntent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
//        tvGroupName.setText(this.groupName);
        collapsingToolbar.setTitle(this.groupName);

    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
        setViewPager();
    }

    @Override
    public void setOwnerDetail(User user) {
        ownerUid = user.getUid();
    }

    @Override
    public void setUserScope(String scope) {
        this.scope=scope;
        if (scope.equals(CometChatConstants.SCOPE_ADMIN)||scope.equals(CometChatConstants.SCOPE_MODERATOR)) {
            tvAddMembers.setVisibility(View.VISIBLE);
        }
        else {
            tvAddMembers.setVisibility(View.GONE);
        }
    }

    @Override
    public void setGroupOwnerName(String owner) {

    }

    @Override
    public void setGroupIcon(String icon) {

        if (icon == null || icon.isEmpty()) {
            Drawable drawable = getResources().getDrawable(R.drawable.cc_ic_group);
            Bitmap bitmap = null;
            try {
                bitmap = MediaUtils.getPlaceholderImage(this, drawable);
                groupImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            groupDetailPresenter.setIcon(this, icon, groupImage);
        }
    }

    @Override
    public void setGroupDescription(String description) {
        if (description != null) {
            tvGroupDescriptionLabel.setVisibility(View.VISIBLE);
            tvGroupDescription.setVisibility(View.VISIBLE);
            tvGroupDescription.setText(description);
        }
    }
}