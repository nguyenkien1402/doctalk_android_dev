package com.mobile.docktalk.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertFunctions {

    public static String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd_hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}
