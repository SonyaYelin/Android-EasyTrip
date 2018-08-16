package com.easytrip.easytrip.api;

public interface IFoursquareConstants {

    //request url related

    public static final String FOURSQUARE = "https://api.foursquare.com/v2/venues/";
    public static final String CLIENT_ID = "client_id=ANR2NBHXQNHN01QPJ32ABYUFGSQFPN1LBICTSZGBWDH30TKH";
    public static final String CLIENT_SECRET = "&client_secret=JRJ0R3W5PU5GQBNEPBV0J40E5NK4JWSWUWE1DNQRHT2GRSJZ";

    public static final String EXPLORE = "explore?";
    //explore arguments:
    public static final String NEAR = "&near=";
    public static final String DATE = "&v="+ "20180810"; //temp - change to current date

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



    //categories
//    //food
//    public static final String FOOD = "4d4b7105d754a06374d81259";
//
//    //hotels
//    public static final String HOTEL = "4bf58dd8d48988d1fa931735";
//    public static final String BED_AND_BREAKFAST = "4bf58dd8d48988d1f8931735";
//    public static final String HOSTEL = "4bf58dd8d48988d1ee931735";
//    public static final String POOL = "4bf58dd8d48988d132951735";
//    public static final String MOTEL = "4bf58dd8d48988d1fb931735";
//    public static final String RESORT = "4bf58dd8d48988d12f951735";
//    public static final String VACATION_RENTAL = "4bf58dd8d48988d12f951735";
//
//    //attractions
//    public static final String ARTS_AND_ENTERTAINMENT = "4d4b7104d754a06370d81259";
//    public static final String AMPHITHEATER = "56aa371be4b08b9a8d5734db";
//    public static final String AQUARIUM = "4fceea171983d5d06c3e9823";
//    public static final String ARCADE = "4bf58dd8d48988d1e1931735";
//    public static final String ART_GALLERY = "4bf58dd8d48988d1e2931735";
//    public static final String BOWLING_ALLEY = "4bf58dd8d48988d1e4931735";
//    public static final String CASINO = "4bf58dd8d48988d17c941735";
//    public static final String COMEDY_CLUB = "4bf58dd8d48988d18e941735";
//    public static final String CONCERT_HALL = "5032792091d4c4b30a586d5c";
//    public static final String COUNTRY_DANCE_CLUB = "52e81612bcbc57f1066b79ef";
//    public static final String EXHIBIT = "56aa371be4b08b9a8d573532";
//    public static final String ENTERTAINMENT = "4bf58dd8d48988d1f1931735";
//    public static final String HISTORIC_SITE = "4deefb944765f83613cdba6e";
//    public static final String KARAOKE_BOX = "5744ccdfe4b0c0459246b4bb";
//    public static final String LASER_TAG = "52e81612bcbc57f1066b79e6";
//    public static final String MEMORIAL_SITE = "5642206c498e4bfca532186c";
//    public static final String MINI_GOLF = "52e81612bcbc57f1066b79eb";
//    public static final String MOVIE_THEATER = "4bf58dd8d48988d17f941735";
//
//    //museum
//    public static final String MUSEUM = "4bf58dd8d48988d181941735";
//    public static final String ART_MUSEUM = "4bf58dd8d48988d18f941735";
//    public static final String EROTIC_MUSEUM = "559acbe0498e472f1a53fa23";
//    public static final String HISTORY_MUSEUM = "4bf58dd8d48988d190941735";
//    public static final String PLANETARIUM = "4bf58dd8d48988d192941735";
//    public static final String SCIENCE_MUSEUM = "4bf58dd8d48988d191941735";
//
//    //music
//    public static final String MUSIC_VENUE = "4bf58dd8d48988d1e5931735";
//    public static final String JAZZ_CLUB = "4bf58dd8d48988d1e7931735";
//    public static final String PIANO_BAR = "4bf58dd8d48988d1e8931735";
//    public static final String ROCK_CLUB = "4bf58dd8d48988d1e9931735";
//
//    //art performance
//    public static final String PERFORMING_ART_VENUE = "4bf58dd8d48988d1f2931735";
//    public static final String DANCE_STUDIO = "4bf58dd8d48988d134941735";
//    public static final String INDIE_THEATER = "4bf58dd8d48988d135941735";
//    public static final String OPERA_HOUSE = "4bf58dd8d48988d136941735";
//    public static final String THEATER = "4bf58dd8d48988d137941735";
//
//    //art
//    public static final String PUBLIC_ART = "507c8c4091d498d9fc8c67a9";
//    public static final String OUTDOOR_SCULPTURE = "52e81612bcbc57f1066b79ed";
//    public static final String STREET_ART = "52e81612bcbc57f1066b79ee";
//
//    //race
//    public static final String RACE_COURSE = "56aa371be4b08b9a8d573514";
//    public static final String RACE_TRACK = "4bf58dd8d48988d1f4931735";
//
//    //dance
//    public static final String SALSA_CLUB = "52e81612bcbc57f1066b79ec";
//    public static final String SAMBA_SCHOOL = "56aa371be4b08b9a8d5734f9";
//
//    //sports
//    public static final String STADIUM = "4bf58dd8d48988d184941735";
//    public static final String BASEBALL_STADIUM = "4bf58dd8d48988d18c941735";
//    public static final String BASKETBALL_STADIUM = "4bf58dd8d48988d18b941735";
//    public static final String FOOTBALL_STADIUM = "4bf58dd8d48988d189941735";
//    public static final String RUGBY_STADIUM = "56aa371be4b08b9a8d573556";
//    public static final String SOCCER_STADIUM = "4bf58dd8d48988d188941735";
//    public static final String TENNIS_STADIUM = "4e39a891bd410d7aed40cbc2";
//    public static final String TRACK_STADIUM = "4bf58dd8d48988d187941735";
//    public static final String HOCKEY_ARENA = "4bf58dd8d48988d185941735";
//
//    //children
//    public static final String THEME_PARK = "4bf58dd8d48988d182941735";
//    public static final String THEME_PARK_RIDE = "5109983191d435c0d71c2bb1";
//    public static final String WATER_PARK = "4bf58dd8d48988d193941735";
//    public static final String ZOO = "4bf58dd8d48988d17b941735";
//    public static final String CIRCUS = "52e81612bcbc57f1066b79e7";
//
//    //games
//    public static final String POOL_HALL = "4bf58dd8d48988d1e3931735";
//
//    //tours
//    public static final String TOUR_PROVIDER = "56aa371be4b08b9a8d573520";


}
