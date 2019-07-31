package com.mobile.docktalk;

import com.mobile.docktalk.models.RequestConsultDocument;
import com.mobile.docktalk.utilities.ConvertFunctions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestFunction {

    public static void main(String[] args){
        System.out.println("Haha");

        RequestConsultDocument requestConsultDocument = new RequestConsultDocument("image",
                ConvertFunctions.dateToString(Calendar.getInstance().getTime()),
                "http://something");
        List<RequestConsultDocument> requestConsultDocuments = new ArrayList<RequestConsultDocument>();
        requestConsultDocuments.add(requestConsultDocument);

//        RequestConsult requestConsult = new RequestConsult("Overview","This is content",1,1002,"NULL",requestConsultDocuments);
//        String object = new Gson().toJson(requestConsult);
//        System.out.println(object);
    }
}
