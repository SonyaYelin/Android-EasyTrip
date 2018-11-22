package com.easytrip.easytrip.utils;

public interface IConstants {

    public static enum GENDER {male, female};

    public static final int MAP = 1;
    public static final int LIST = 0;
    public static final int CURRENT = 0;
    public static final int SCHEDULED = 1;
    public static final int PAST = 2;

    public static final String AUTH_FAILED = "Authentication failed.";
    public static final String REGISTRATION_FAILED = "Registration failed";
    public static final String REGISTRATION_SUCCEEDED = "User registered successfully";
    public static final String VALID_EMAIL = "Please enter a valid email";
    public static final String EMAIL_REQ = "Email is required";
    public static final String PASSWORD_REQ = "Password is required";
    public static final String PASSWORD_MIN = "Minimum length of password should be 6";
    public static final String VERIFY_REQ = "Verified password is required";
    public static final String VERIFY_FAILED = "Verified password does'nt matches";
    public static final String BUILDING_TRIP = "Building your trip...";
    public static final String SELECT_MORE = "Please Select More Venues";
    public static final String SELECT_LESS = "Please Select Less Venues";
    public static final String TRIP_TO = "Trip to: ";
    public static final String DAYS = "Days ";

    public static final String NO_INTERNET = "There is no Internet connection";
    public static final String LOADING_TRIPS = "Loading trips...";
    public static final String DESTINATION_REQ = "Destination is required!";
    public static final String CATEGORIES = "Categories";
    public static final String BEST_MATCHES = "Best Matches";
    public static final String USER = "user";
    public static final String TRIP = "trip";

    public static final int MAX_DIALOG_TIME_IN_SEC = 60000;

    public static final int REQUEST_CODE_LOAD_IMAGE = 114;

    public static final float  ZOOM = 14.6f;

}
