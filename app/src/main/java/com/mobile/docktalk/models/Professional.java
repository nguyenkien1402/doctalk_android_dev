package com.mobile.docktalk.models;


import com.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class Professional {

    private int id;
    private String title;
    private String description;
    private String code;
    private int icon;

    public Professional() {
    }

    public Professional(int id, String title, String description, String code, int icon) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
        this.icon = icon;
    }

    public Professional(int id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static List<Professional> getListProfessional(){
        List<Professional> professionals = new ArrayList<Professional>();
        // Init professional manual.
        Professional p1 = new Professional(1,"Rang Ham Mat", R.drawable.doctor_32);
        Professional p2 = new Professional(2,"Da Khoa", R.drawable.medical_32);
        Professional p3 = new Professional(3,"San", R.drawable.medical_32);
        Professional p4 = new Professional(4,"Mat", R.drawable.exercise_32);
        Professional p5 = new Professional(5,"Tai Mui Hong", R.drawable.heart_32);
        Professional p6 = new Professional(6,"Noi Khoa", R.drawable.stethoscope_32);

        // add to the list
        professionals.add(p1);
        professionals.add(p2);
        professionals.add(p3);
        professionals.add(p4);
        professionals.add(p5);
        professionals.add(p6);

        return professionals;
    }
}
