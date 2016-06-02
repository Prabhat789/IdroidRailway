package com.idroidms.railway.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.fragments.AlertsFragment;
import com.idroidms.railway.fragments.BookTicketFragment;
import com.idroidms.railway.fragments.FragmentDrawer;
import com.idroidms.railway.fragments.GotBoardFragment;
import com.idroidms.railway.fragments.HomeFragment;
import com.idroidms.railway.fragments.RatingsFragments;
import com.idroidms.railway.fragments.SettingsFragment;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.UserSessionManager;

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
    private UserSessionManager sessionManager;
    private long lastPressedTime;
    private static final int PERIOD = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidemenu);
        sessionManager = new UserSessionManager(SlideMenuActivity.this);
        sessionManager.checkLogin();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.e(TAG,soundUri.toString());


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
                dialogLogout(SlideMenuActivity.this);
                //logoutUser();
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
       int dayCount = AppUtils.get_count_of_days(currentDate, ApplicationConstants.EXPIRED_DATE);
       if (dayCount <= 0){
           AppUtils.showToastMessage(SlideMenuActivity.this,"Licence Expired !");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        AppUtils.showToastMessage(this,"Press again to exit.");
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }


    private void dialogLogout(final Context mContext){
        final Dialog gameOver = new Dialog(mContext);
        gameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOver.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOver.setCancelable(false);
        gameOver.setContentView(R.layout.dialog_logout);
        Button btnCancel = (Button)gameOver.findViewById(R.id.btnCancel);
        Button btnLogout = (Button)gameOver.findViewById(R.id.btnLogout);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver.dismiss();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver.dismiss();
                logoutUser();
                AppUtils.showToastMessage(mContext,"Logout is Successful");

            }
        });

        gameOver.show();
    }
}

