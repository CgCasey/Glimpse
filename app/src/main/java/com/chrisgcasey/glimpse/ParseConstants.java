package com.chrisgcasey.glimpse;

import com.parse.ParseUser;

/**
 * Created by Chris on 1/13/2015.
 */
public class ParseConstants {

    //Parse classes
    public static final String CLASS_MESSAGES = "Messages";

     //Parse fields
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FRIENDS_RELATION = "friendsRelation";
    public static final String KEY_RECIPIENTS_IDS = "recipientsIds";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_FILE_TYPE = "fileType";
    public static final String KEY_FILE = "file";
    public static final String KEY_CREATED_AT = "createdAt";

    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_IMAGE = "image";

    //parse methods
    public static void logout(){
        ParseUser.logOut();
    }
}
