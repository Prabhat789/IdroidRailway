package com.pktworld.railway.parseutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;
import com.parse.SignUpCallback;
import com.pktworld.railway.R;
import com.pktworld.railway.activity.LoginActivity;
import com.pktworld.railway.activity.SlideMenuActivity;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.UserSessionManager;
import com.pktworld.railway.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ubuntu1 on 14/5/16.
 */
public class ParseSetUp {
    private static final String TAG = ParseSetUp.class.getSimpleName();

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public ParseSetUp(Context context) {
        this.mContext= context;

    }

    public void login(String username, String password) {

        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getResources().getString(R.string.processing), true);

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                List<String> list = new ArrayList<String>();
                mProgressDialog.dismiss();
                if (user != null) {
                    UserSessionManager sessionManager = new UserSessionManager(mContext);
                    sessionManager.createUserLoginSession();
                    Intent intent = new Intent(mContext, SlideMenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intent);
                } else {
                    Utils.showToastMessage(mContext, mContext.getResources().getString(R.string.invalid_user));
                }
            }
        });
    }

    public void register(String fName, String lName, String email, String password,String mobile){
        mProgressDialog = ProgressDialog.show(mContext, "", mContext.getResources().getString(R.string.processing), true);
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", fName);
        user.put("lastName",lName);
        user.put("mobile",mobile);
        user.put("name",fName+" "+lName);
        user.put("profileImage", ApplicationConstants.PROFILE_IMAGE);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    Intent i = new Intent(mContext, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(i);

                } else {
                    Utils.showToastMessage(mContext, mContext.getString(R.string.unable_to_process_request));
                }
            }
        });
    }

    /*send push notification*/
    public static void sendPush(String msg, Set<String> objectId) {
        Log.e(TAG,objectId.toString());
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereContainedIn("objectId", objectId);
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(msg);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Notification Send");
                }
            }
        });

    }

}
