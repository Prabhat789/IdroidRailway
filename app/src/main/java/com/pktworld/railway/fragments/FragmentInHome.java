package com.pktworld.railway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pktworld.railway.R;
import com.pktworld.railway.activity.TrainInfoActivity;

/**
 * Created by Prabhat on 25/03/16.
 */
public class FragmentInHome extends Fragment implements View.OnClickListener{

    private static final String TAG = FragmentInHome.class.getSimpleName();
    private LinearLayout llTrainInfo,llAlerts,llBookTickets,llGotBoard,llSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_home, container, false);

        llTrainInfo = (LinearLayout)rootView.findViewById(R.id.llTrainInfo);
        llTrainInfo.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onClick(View v) {
        if (v == llTrainInfo){
            Intent i = new Intent(getActivity(), TrainInfoActivity.class);
            startActivity(i);
        }
    }
}
