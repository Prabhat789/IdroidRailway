package com.pktworld.railway.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pktworld.railway.R;

/**
 * Created by Prabhat on 25/03/16.
 */
public class FragmentInHome extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_in_home, container, false);

        return rootView;

    }


}
