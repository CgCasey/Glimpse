package com.chrisgcasey.glimpse;

import com.parse.ParseUser;

/**
 * Created by Chris on 1/13/2015.
 */
public class ParseConstants {

    //Parse classes

    //Parse fields
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FRIENDS_RELATION = "friendsRelation";

    public static void logout(){
        ParseUser.logOut();
    }
}
