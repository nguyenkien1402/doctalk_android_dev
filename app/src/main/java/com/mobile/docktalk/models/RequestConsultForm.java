package com.mobile.docktalk.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RequestConsultForm implements Parcelable {
    private String content;
    private List<Uri> imageUrls;
    private String specification;
    private String subject;
    private String additionalInfo;
    private int subjectId;

    public RequestConsultForm() {
    }

    public RequestConsultForm(String content, List<Uri> imageUrls, String specification, String subject, int subjectId, String additionalInfo) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.specification = specification;
        this.subject = subject;
        this.additionalInfo = additionalInfo;
        this.subjectId = subjectId;
    }

    protected RequestConsultForm(Parcel in) {
        content = in.readString();
        imageUrls = in.createTypedArrayList(Uri.CREATOR);
        specification = in.readString();
        subject = in.readString();
        additionalInfo = in.readString();
        subjectId = in.readInt();
    }


    public static final Creator<RequestConsultForm> CREATOR = new Creator<RequestConsultForm>() {
        @Override
        public RequestConsultForm createFromParcel(Parcel in) {
            return new RequestConsultForm(in);
        }

        @Override
        public RequestConsultForm[] newArray(int size) {
            return new RequestConsultForm[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeTypedList(imageUrls);
        dest.writeString(specification);
        dest.writeString(subject);
        dest.writeString(additionalInfo);
        dest.writeInt(subjectId);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Uri> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<Uri> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
