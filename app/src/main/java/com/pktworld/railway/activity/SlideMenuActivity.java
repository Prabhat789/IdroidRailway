package com.pktworld.railway.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.fragments.AlertsFragment;
import com.pktworld.railway.fragments.BookTicketFragment;
import com.pktworld.railway.fragments.FragmentDrawer;
import com.pktworld.railway.fragments.GotBoardFragment;
import com.pktworld.railway.fragments.HomeFragment;
import com.pktworld.railway.fragments.RatingsFragments;
import com.pktworld.railway.fragments.SettingsFragment;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.UserSessionManager;
import com.pktworld.railway.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class SlideMenuActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = SlideMenuActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private int i = 0;
    private TextView mTitle;
    private  UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidemenu);
        sessionManager = new UserSessionManager(SlideMenuActivity.this);
        sessionManager.checkLogin();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.txtToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (isLycenceExpire()){
            finish();
        }
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

        displayView(i);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            i = intent.getIntExtra("message",0);
            displayView(i);
            Log.d("receiver", "Got message: " + "" + i);
        }
    };




    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        FragmentDrawer.mDrawerLayout.closeDrawers();
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                // title = "";
                title = getString(R.string.home);
                break;
            case 1:
                fragment = new AlertsFragment();
                //title = "";
                title = getString(R.string.alerts);
                break;
            case 2:
                fragment = new BookTicketFragment();
                //title = "";
                title = getString(R.string.book_ticket);
                break;
            case 3:
                fragment = new GotBoardFragment();
                //title = "";
                title = getString(R.string.got_board);
                break;
            case 4:
                fragment = new SettingsFragment();
                //title = "";
                title = getString(R.string.settings);
                break;
            /*case 5:
                fragment = new ChatRoomFragment();
                // title = "";
                title = getString(R.string.chat_room);
                break;*/
            case 5:
                fragment = new RatingsFragments();
                title = getString(R.string.rating);
                //title = "";
                break;
            case 6:
                logoutUser();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle("");
            mTitle.setText(title);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


   private boolean isLycenceExpire(){
       String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
       int dayCount = Utils.get_count_of_days(currentDate, ApplicationConstants.EXPIRED_DATE);
       if (dayCount <= 0){
           Utils.showToastMessage(SlideMenuActivity.this,"Licence Expired !");
           return true;
       }else {
           return false;
       }
    }

    private void logoutUser(){
        /*SharedPreferences loginPreferences = getSharedPreferences(ApplicationConstants.LOGIN_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.clear();
        loginPrefsEditor.commit();
        ParseUser.logOut();
        Intent i = new Intent(SlideMenuActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();*/
        sessionManager.logoutUser();


    }
}

