package com.pktworld.railway.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.parse.ParseUser;
import com.pktworld.railway.activity.LoginActivity;

import java.util.HashMap;

public class UserSessionManager {

    private static final String TAG = UserSessionManager.class.getSimpleName();
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	private static final String PREFER_NAME = ApplicationConstants.LOGIN_PREFERENCE;
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_USER_NAME = "userName";
	public static final String KEY_FIRST_NAME = "firstName";
	public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_USER_ID = "userId";
	public static final String KEY_MAIL_ID = "mailId";
	public static final String RINGTONE= "ringtone";


	// Constructor
	public UserSessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	//Create login session
	public void createUserLoginSession(){
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		// Storing name in pref
		/*editor.putString(KEY_USER_NAME, username);
		editor.putString(KEY_MAIL_ID, mailid);
		editor.putString(KEY_USER_ID,userid);
        editor.putString(KEY_FIRST_NAME,firstName);
        editor.putString(KEY_LAST_NAME,lastName);*/
		// commit changes
		editor.commit();
	}
    /**
	 * Check login method will check user login status
	 * If false it will redirect user to login page
	 * Else do anything
	 * */
	public boolean checkLogin(){
		// Check login status
		if(!this.isUserLoggedIn()){
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);

			return true;
		}
		return false;
	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
		user.put(KEY_MAIL_ID, pref.getString(KEY_MAIL_ID, null));
		user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
		// return user
		return user;
	}
	public void createLoginSession(String mailid, String userid ){
		// Storing login value as TRUE
		editor.putBoolean(IS_USER_LOGIN, true);
		editor.putString(KEY_MAIL_ID, mailid);
		editor.putString(KEY_USER_ID, userid);
		Log.e(TAG, editor.toString());
		// commit changes
		editor.commit();
	}

	public void setRingtone(String ringtone){

		editor.putString(RINGTONE,ringtone);
		editor.commit();

	}

	public String getRingtone(){
		return pref.getString(RINGTONE,"Unknown ringtone");
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all user data from Shared Preferences
		editor.clear();
		editor.commit();
        ParseUser.logOut();
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		_context.startActivity(i);
	}
	
	// Check for login
	public boolean isUserLoggedIn(){
		return pref.getBoolean(IS_USER_LOGIN, false);
	}

	
}
