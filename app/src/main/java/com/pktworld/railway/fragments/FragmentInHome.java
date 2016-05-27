package com.pktworld.railway.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pktworld.railway.R;
import com.pktworld.railway.activity.BookTicketActivity;
import com.pktworld.railway.activity.TrainInfoActivity;
import com.pktworld.railway.parseutils.GotBoardActivity;

/**
 * Created by Prabhat on 25/03/16.
 */
public class FragmentInHome extends Fragment implements View.OnClickListener{

    private static final String TAG = FragmentInHome.class.getSimpleName();
    private LinearLayout llTrainInfo,llAlerts,llBookTickets,llGotBoard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_home, container, false);

        llTrainInfo = (LinearLayout)rootView.findViewById(R.id.llTrainInfo);
        llBookTickets = (LinearLayout)rootView.findViewById(R.id.llBookTicket);
       // llSettings = (LinearLayout)rootView.findViewById(R.id.llSettings);
        llGotBoard = (LinearLayout)rootView.findViewById(R.id.llGotBoard);
        llBookTickets.setOnClickListener(this);
        llTrainInfo.setOnClickListener(this);
       // llSettings.setOnClickListener(this);
        llGotBoard.setOnClickListener(this);

        return rootView;

    }


    @Override
    public void onClick(View v) {
        if (v == llTrainInfo){
            Intent i = new Intent(getActivity(), TrainInfoActivity.class);
            startActivity(i);
        }else if (v == llBookTickets){
            Intent i = new Intent(getActivity(), BookTicketActivity.class);
            startActivity(i);
        }/*else if (v == llSettings){
            Intent i = new Intent(getContext(), SettingActivity.class);
            startActivity(i);
        }*/else if (v == llGotBoard){
            Intent i = new Intent(getContext(), GotBoardActivity.class);
            startActivity(i);
        }
    }
}
