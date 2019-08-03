package com.mobile.docktalk.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

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
