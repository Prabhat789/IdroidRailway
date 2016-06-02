package com.idroidms.railway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.adapter.NavigationDrawerAdapter;
import com.idroidms.railway.model.NavDrawerModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ubuntu1 on 25/2/16.
 */
public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;
    public static View containerView;
    private FragmentDrawerListener drawerListener;
    private ListView expandableListView;
    private List<NavDrawerModel> model;
    private NavigationDrawerAdapter adapter;
    private TextView txtProfileName;
    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        expandableListView = (ListView) layout.findViewById(R.id.mainList);
        //txtProfileName = (TextView)layout.findViewById(R.id.txtProfileName);
        fillDrawerList();
        adapter = new NavigationDrawerAdapter(getActivity(),model);
        expandableListView.setAdapter(adapter);

       /* txtProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Fragment fragment = null;
                fragment = new SettingsFragment();
                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                    // set the toolbar title
                    //getActivity().getSupportActionBar().setTitle(title);
                }
            }
        });*/



        return layout;
    }




    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    private void fillDrawerList(){
        model = new ArrayList<NavDrawerModel>();
        model.add(0,new NavDrawerModel(getString(R.string.home), R.drawable.ic_home,0));
        model.add(1,new NavDrawerModel(getString(R.string.alerts),R.drawable.ic_alerts,1));
        model.add(2,new NavDrawerModel(getString(R.string.book_ticket),R.drawable.ic_book_ticket,2));
        model.add(3,new NavDrawerModel(getString(R.string.got_board),R.drawable.ic_get_board,3));
        model.add(4,new NavDrawerModel(getString(R.string.settings),R.drawable.ic_setting_menu,4));
       // model.add(5,new NavDrawerModel(getString(R.string.chat_room),R.drawable.ic_home,5));
        model.add(5,new NavDrawerModel(getString(R.string.rating),R.drawable.ic_rating,5));
        model.add(6,new NavDrawerModel(getString(R.string.logout),R.drawable.ic_logout,6));

    }


}
