package com.mobile.docktalk.utility;

public class EndPointAPIs {
    public static String account_url = "http://192.168.132.1:5000/";
    public static String application_url = "http://192.168.132.1:5001/";
    public static String signup_url = account_url + "authentication/api/register";
    public static String get_token = account_url + "connect/token";
    public static String register_patient = application_url + "api/account/register/patient";
    public static String get_token_mobile = application_url + "api/account/token/";

}
