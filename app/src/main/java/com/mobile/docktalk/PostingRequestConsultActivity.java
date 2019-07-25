package com.mobile.docktalk;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
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
import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.docktalk.apiconsumption.RequestConsultController;
import com.mobile.docktalk.model.RequestConsult;
import com.mobile.docktalk.model.RequestConsultDocument;
import com.mobile.docktalk.utility.ConvertFunctions;
import com.mobile.docktalk.utility.Messages;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostingRequestConsultActivity extends AppCompatActivity {

    Button btnChooseImage;
    EditText postingContent;
    String content = "";
    String userId;
    String token;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog ;
    ImageView SelectImage;
    Uri FilePathUri;
    StorageReference storageReference;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_posting_request_consult);
        btnChooseImage = (Button) findViewById(R.id.btn_choose_image);
        postingContent = (EditText) findViewById(R.id.posting_content);
        SelectImage = (ImageView) findViewById(R.id.posting_image);
        progressDialog = new ProgressDialog(PostingRequestConsultActivity.this);
        //Get user Id and Token from SharePreferences
        SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
        userId = pref.getString("UserId",null);
        token = pref.getString("Token",null);

        // Init Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });
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
                btnChooseImage.setText("Image Selected");
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
            progressDialog.setTitle("Image is Uploading Yeah...");
            // Showing progressDialog.
            progressDialog.show();
            // Creating second StorageReference.
            final StorageReference storageReference2nd = storageReference.child(Messages.firebase_storage_request_consult_document_path
                    + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
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
                        String image_name = userId + "_" + ConvertFunctions.dateToString(Calendar.getInstance().getTime());
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        Uri downloadUri = task.getResult();
                        final String imageUrl = downloadUri.toString();
                        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(image_name,  downloadUri.toString());
                        Map<String, Object> imagePostValue = imageUploadInfo.toMap();

                        // Getting image upload ID.
//                        DatabaseReference doctorRef = databaseReference.child("Doctor_1");
//                        String key = doctorRef.push().getKey();
//                        doctorRef.child(key).setValue(imagePostValue);

                        db.collection(Messages.firebase_database_consult_document_collection).document(Messages.firebase_database_consult_patient_document).
                                collection(Messages.firebase_database_consult_patient_user_collection +userId).add(imagePostValue)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Save the question to the Database
                                        content = postingContent.getText().toString();
                                        saveRequestConsultToDatabase(content, imageUrl);
                                        // And then call the API to find the doctor
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
            Toast.makeText(PostingRequestConsultActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();
        }
    }

    public void saveRequestConsultToDatabase(String content, String imageUrl){
        // Get PatientId
        PostingQuestionAsync postingQuestionAsync = new PostingQuestionAsync();
        postingQuestionAsync.execute(new String[]{content,imageUrl});
    }

    private class PostingQuestionAsync extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... strings) {
            String content = strings[0];
            String imageUrl = strings[1];
            SharedPreferences pref = getSharedPreferences("UserInfo",MODE_PRIVATE);
            int patientId = pref.getInt("PatientId",0);

            RequestConsult requestConsult = createNewRequestConsult(content, imageUrl, patientId);
            JSONObject result = RequestConsultController.PostingQuestion(token,requestConsult);
            return result;
        }


        private RequestConsult createNewRequestConsult(String content, String imageUrl, int patientId) {
            RequestConsultDocument requestConsultDocument = new RequestConsultDocument("image",
                    ConvertFunctions.dateToString(Calendar.getInstance().getTime()),
                    imageUrl);
            List<RequestConsultDocument> requestConsultDocuments = new ArrayList<RequestConsultDocument>();
            requestConsultDocuments.add(requestConsultDocument);

            RequestConsult requestConsult = new RequestConsult("Overview",content,1,patientId,"NULL",requestConsultDocuments);
            return requestConsult;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            // When get the result, call the API to find the doctor.
            if(jsonObject != null){
                Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_SHORT).show();
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.posting_request_consult_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.posting_consult_send){
            Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_SHORT).show();
            UploadImageFileToFirebaseStorage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
