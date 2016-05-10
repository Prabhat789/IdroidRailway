package com.pktworld.railway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pktworld.railway.R;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private Button btnHome, btnCharRoom, btnBookTicket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnCharRoom = (Button)rootView.findViewById(R.id.btnChatRoom);
        btnHome = (Button)rootView.findViewById(R.id.btnHome);

        btnHome.setOnClickListener(this);
        btnCharRoom.setOnClickListener(this);

        changeButtonState(1);


        return rootView;

    }

    @Override
    public void onClick(View v) {
        if (v == btnHome){
            changeButtonState(1);
        }else if (v == btnCharRoom){
            changeButtonState(2);
        }
    }

     void changeButtonState(int id){
        switch (id){
            case 1:
                btnHome.setBackgroundColor(getResources().getColor(R.color.yellow));
                btnCharRoom.setBackgroundColor(getResources().getColor(R.color.smoke_white));
                FragmentInHome fragment = new FragmentInHome();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment).commit();
                break;
            case 2:
                btnHome.setBackgroundColor(getResources().getColor(R.color.smoke_white));
                btnCharRoom.setBackgroundColor(getResources().getColor(R.color.yellow));
                ChatRoomFragment fragment1 = new ChatRoomFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment1).commit();
                break;
            default:
                break;
        }

    }



}
