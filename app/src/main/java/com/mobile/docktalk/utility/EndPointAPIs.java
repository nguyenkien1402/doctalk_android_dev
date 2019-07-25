package com.mobile.docktalk.utility;

public class EndPointAPIs {
    public static String account_url = "http://192.168.132.1:5000/";
    public static String application_url = "http://192.168.132.1:5001/";
    public static String signup_url = account_url + "authentication/api/register";
    public static String register_patient = application_url + "api/account/register/patient";
    public static String register_doctor = application_url + "api/account/register/doctor";
    public static String get_token_mobile = application_url + "api/account/token/";
    public static String get_user_token_info = account_url + "connect/userinfo";
    public static String get_patient_info = application_url +"api/patients/userid/";
    public static String get_doctor_info = application_url + "api/doctors/userid/";
    public static String professional_list = application_url + "api/professionals";
    public static String adding_doctor_professional = application_url + "api/professionals/doctor/addpro";
    public static String posting_question_consult = application_url + "api/requestconsult";
}
