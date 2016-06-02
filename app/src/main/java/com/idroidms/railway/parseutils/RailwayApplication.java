package com.idroidms.railway.parseutils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import io.fabric.sdk.android.Fabric;

/**
 * Created by ubuntu1 on 14/5/16.
 */
public class RailwayApplication extends Application {


    private static RailwayApplication mInstance;
    private static Context mAppContext;

    public static RailwayApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public void setAppContext(Context mAppContext) {
        RailwayApplication.mAppContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        this.setAppContext(getApplicationContext());
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(Group.class);
        Parse.enableLocalDatastore(getAppContext());
        Parse.initialize(getAppContext());
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        subscribeforpushnotification();

    }
    private void subscribeforpushnotification() {
        // TODO Auto-generated method stub
        ParsePush.subscribeInBackground("Railway", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

    }
}
