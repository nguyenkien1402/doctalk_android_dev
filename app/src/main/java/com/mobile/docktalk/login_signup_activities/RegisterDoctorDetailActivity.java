package com.mobile.docktalk.login_signup_activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.R;
import com.mobile.docktalk.utilities.Messages;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterDoctorDetailActivity extends AppCompatActivity {

    Button ChooseButton, UploadButton;
    EditText ImageName ;
    ImageView SelectImage;
    Uri FilePathUri;
    StorageReference storageReference;
    FirebaseFirestore db;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    private String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_upload_demo);
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        ChooseButton = (Button)findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button)findViewById(R.id.ButtonUploadImage);
        ImageName = (EditText)findViewById(R.id.ImageNameEditText);
        SelectImage = (ImageView)findViewById(R.id.ShowImageView);
        progressDialog = new ProgressDialog(RegisterDoctorDetailActivity.this);

        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId = pref.getString("UserId",null);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){

            ChooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    // Setting intent type as image to select image from phone storage.
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
                }
            });

            UploadButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    UploadImageFileToFirebaseStorage();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Haven't login to firebase yet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);
                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }


    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {
        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");
            // Showing progressDialog.
            progressDialog.show();
            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Messages.firebase_storage_avatar_path  + "/" + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            // Adding addOnSuccessListener to second StorageReference.
            UploadTask uploadTask = storageReference2nd.putFile(FilePathUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference2nd.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        final String TempImageName = ImageName.getText().toString().trim();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        Uri downloadUri = task.getResult();
                        ImageUploadInfo imageUploadInfo = new RegisterDoctorDetailActivity.ImageUploadInfo(TempImageName,downloadUri.toString());
                        Map<String, Object> imagePostValue = imageUploadInfo.toMap();
                        // Getting image upload ID.
//                        DatabaseReference doctorRef = databaseReference.child("Doctor_1");
//                        String key = doctorRef.push().getKey();
//                        doctorRef.child(key).setValue(imagePostValue);

                        db.collection(Messages.firebase_database_avatar_collection)
                                .document(Messages.firebase_database_document_doctor_avatar)
                                .collection(userId).add(imagePostValue)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }
            });
        }
        else {
            Toast.makeText(RegisterDoctorDetailActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }

    public class ImageUploadInfo {

        public String imageName;

        public String imageURL;

        public ImageUploadInfo() {

        }

        public ImageUploadInfo(String imageName, String imageURL) {

            this.imageName = imageName;
            this.imageURL= imageURL;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("imageName",imageName);
            result.put("url",imageURL);
            return result;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
