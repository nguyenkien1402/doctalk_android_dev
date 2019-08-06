package com.mobile.docktalk.consult_activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobile.R;
import com.mobile.docktalk.models.RequestConsultForm;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;

public class PostingRequestConsultActivity extends AppCompatActivity {

    EditText postingContent;
    LinearLayout layoutBottomSheet, bottomSheetTitle;
    BottomSheetBehavior sheetBehavior;
    TextView photoRow;

    private List<Uri> selectedUriList;
    private ViewGroup mSelectedImagesContainer;
    private RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        buttonSelectPhotoActioner();
        bottomSheetActioner();
    }

    /*
     * Initialize all the widget of the view
     */
    private void initView(){
        setContentView(R.layout.activity_posting_request_consult);
        layoutBottomSheet = (LinearLayout) findViewById(R.id.posting_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetTitle = (LinearLayout) findViewById(R.id.posting_bottom_title);
        postingContent = (EditText) findViewById(R.id.posting_content);
        photoRow = (TextView) findViewById(R.id.posting_photo);
        mSelectedImagesContainer = findViewById(R.id.posting_selected_photos_container);
        requestManager = Glide.with(this);
    }

    /*
     * Selected Photo button listener
     */
    private void buttonSelectPhotoActioner(){
        photoRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Now open the photo fragment.
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker.with(PostingRequestConsultActivity.this)
                                .setPeekHeight(getResources().getDisplayMetrics().heightPixels)
                                .showTitle(false)
                                .setCompleteButtonText("Done")
                                .setEmptySelectionText("No Select")
                                .setSelectedUriList(selectedUriList)
                                .showMultiImage(uriList -> {
                                    selectedUriList = uriList;
                                    showUriList(uriList);
                                });
                    }


                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(),"Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_LONG).show();
                    }
                };

                checkPermission(permissionListener);
            }
        });
    }

    /*
     * Bottom Sheet Action
     */
    private void bottomSheetActioner(){
        bottomSheetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        break;
                    }
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }


    private void showUriList(List<Uri> uriList) {
        mSelectedImagesContainer.removeAllViews();
        mSelectedImagesContainer.setVisibility(View.VISIBLE);
        for(Uri uri : uriList){
            ImageView iv =(ImageView) LayoutInflater.from(this).inflate(R.layout.posting_image_item, mSelectedImagesContainer,false);
            requestManager.load(uri.toString())
                    .apply(new RequestOptions().fitCenter())
                    .into(iv);
            mSelectedImagesContainer.addView(iv);
        }
    }

    private void checkPermission(PermissionListener permissionListener){
        TedPermission.with(PostingRequestConsultActivity.this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission, you can not use this srevice\n\nPlease turn on permission at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posting_request_consult_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.posting_consult_send){
            String content = postingContent.getText().toString();
            RequestConsultForm requestConsultForm = new RequestConsultForm();
            requestConsultForm.setContent(content);
            requestConsultForm.setImageUrls(selectedUriList);
            Bundle bundle = new Bundle();
            bundle.putParcelable("RequestConsultForm",requestConsultForm);
            Intent intent = new Intent(getApplicationContext(),FirstQuestionRequestConsultActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
