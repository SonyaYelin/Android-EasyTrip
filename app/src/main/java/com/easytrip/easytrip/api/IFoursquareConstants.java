package com.easytrip.easytrip.api;

public interface IFoursquareConstants {

    //request url related
    public static final String FOURSQUARE = "https://api.foursquare.com/v2/venues/";
    public static final String CLIENT_ID = "client_id=ANR2NBHXQNHN01QPJ32ABYUFGSQFPN1LBICTSZGBWDH30TKH";
    public static final String CLIENT_SECRET = "&client_secret=JRJ0R3W5PU5GQBNEPBV0J40E5NK4JWSWUWE1DNQRHT2GRSJZ";

    public static final String EXPLORE = "explore?";
    //explore arguments:
    public static final String NEAR = "&near=";
    public static final String DATE = "&v="+ "20180810";

    public static final String SECTION = "&section=";
    //sections arguments
    public static final String FOOD = "food";
    public static final String DRINKS = "drinks";
    public static final String ARTS = "arts";
    public static final String OUTDOORS = "outdoors";
    public static final String SIGHTS = "sights";
    public static final String TRENDING = "trending";

    public static final String FOURSQUARE_EXPLORE_URL = FOURSQUARE + EXPLORE + CLIENT_ID + CLIENT_SECRET + DATE +  NEAR;

    //response json related

    public static final String RESPONSE = "response";
    public static final String ITEMS = "items";
    public static final String GROUPS = "groups";
    public static final String VENUE = "venue";
    public static final String CANONICAL_URL = "canonicalUrl";
    public static final String HOURS = "hours";
    public static final String STATUS = "status";

    public static final String UTF8 = "UTF-8";
    public static final String PREFIX = "prefix";
    public static final String SUFFIX = "suffix";

}
