package com.pktworld.railway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pktworld.railway.R;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class ChatRoomFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_char_room, container, false);

        return rootView;

    }
}
