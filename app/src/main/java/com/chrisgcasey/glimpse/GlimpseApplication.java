package com.chrisgcasey.glimpse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Chris on 12/11/2014.
 */
public class GlimpseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "1NoZjhkrWSTXywuYAja5cGDDrYYbKWFlRFtJmIYO", "23IRYVJiAyLeOxsk6mJct0OptwTA6gZT5A7iWbEV");


        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
    }
}
