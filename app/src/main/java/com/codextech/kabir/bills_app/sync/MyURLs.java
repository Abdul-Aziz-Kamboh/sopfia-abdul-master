package com.codextech.kabir.bills_app.sync;

public class MyURLs {

//    private static final String server = "http://192.168.100.22:3000"; //local
    private static final String server = "https://sopfia.herokuapp.com/api"; //server

    public static final String LOGIN_URL = server + "/users/signin";
    public static final String SIGNUP_URL = server + "/users/signup";
    public static final String SEND_TOKEN = server + "/sendToken";
    public static final String SEND_ETH = server + "/sendEth";
    public static final String SEND_CREDITS = server + "/users/sendCredits";
    public static final String GET_ETHEREUM_BALANCE = server + "/accountDetail";
    public static final String GET_ETHMNY_BALANCE = "https://api.etherscan.io/api";
    public static final String UPDATE_DETAILS = server + "/users/";
    public static final String GET_PRODUCTS = server + "/products/";
    public static final String GET_PROPERTY_MANAGERS = server + "/supercontractor/assignment-props?api_token=";
    public static final String GET_ASSIGNMENT_REPORT = server + "/supercontractor/assignment-report?api_token=";
    public static final String GET_USER = server + "/api/v1/users/me";
    public static final String GET_USER_BY_ID = server + "/users/";
    public static final String GET_TRANSACTIONS = server + "/api/v1/users/transections";
    public static final String GET_SUBSCRIBERS = server + "/api/v1/subscriber";
    public static final String GET_SUBSCRIBERS_SINGLE = server + "/api/v1/subscriber";
    public static final String PAY_SUBSCRIBER = server + "/api/v1/subscriber";
    public static final String ADD_SUBSCRIBER = server + "/api/v1/subscriber";
    public static final String DELETE_SUBSCRIBERS = server + "/api/v1/subscriber";
    public static final String PRIVACY_POLICY = "https://bills.pk/privacy.html";
    public static final String FORGOT_PASSWORD = "https://app.bills.pk/#/access/forgotpwd";
}
