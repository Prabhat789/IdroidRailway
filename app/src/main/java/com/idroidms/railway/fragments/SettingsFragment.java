package com.idroidms.railway.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.model.RingtoneList;
import com.idroidms.railway.util.UserSessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    /*private Button btnGetRingtoneList;
    private ListView listRingtone;*/
    private Ringtone ringtone = null;
    private Switch mSwitch;
    private TextView txtRingtone;
    private View view;
    private UserSessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        mSwitch = (Switch)rootView.findViewById(R.id.switch1);
        txtRingtone = (TextView)rootView.findViewById(R.id.txtRingtone);
        view = (View)rootView.findViewById(R.id.view);
        session = new UserSessionManager(getActivity());
        txtRingtone.setOnClickListener(this);

        String ringTone = session.getRingtone();
        if (ringTone.equals("Unknown ringtone")){
            view.setVisibility(View.INVISIBLE);
            mSwitch.setChecked(false);
            mSwitch.setClickable(true);
            txtRingtone.setVisibility(View.INVISIBLE);
        }else {
            mSwitch.setClickable(false);
            view.setVisibility(View.VISIBLE);
            mSwitch.setChecked(true);
            txtRingtone.setVisibility(View.VISIBLE);
            txtRingtone.setText("Ringtone : "+ringTone);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   getRingtonePicker();
                }
            }
        });

       /* listRingtone =(ListView)rootView.findViewById(R.id.listRingtone);
        btnGetRingtoneList = (Button)rootView.findViewById(R.id.btnGetlistRingtone);

        btnGetRingtoneList.setOnClickListener(this);*/

        return rootView;

    }

   /* @Override
    public void onClick(View v) {
        if (v == btnGetRingtoneList){

            *//*btnGetRingtoneList.setVisibility(View.GONE);
            RingtoneListAdapter adapter = new RingtoneListAdapter(getActivity(),getNotifications());
            listRingtone.setAdapter(adapter);*//*
        }
    }*/

    void getRingtonePicker(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(intent, 1);
    }

    public List<RingtoneList> getNotifications() {
        RingtoneManager manager = new RingtoneManager(getActivity());
        manager.setType(RingtoneManager.TYPE_ALL);
        Cursor cursor = manager.getCursor();

        List<RingtoneList> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            list.add(new RingtoneList(notificationTitle,notificationUri));
        }

        return list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Log.e("TAG",uri.toString());
                    ringtone = RingtoneManager.getRingtone(getActivity(), uri);
                    String ringtoneTitle = ringtone.getTitle(getActivity());
                    session.setRingtone(ringtoneTitle);
                    /*if (ringtoneTitle.equals("Unknown ringtone")){
                        txtRingtone.setVisibility(View.INVISIBLE);
                        mSwitch.setChecked(false);
                    }else {
                        txtRingtone.setVisibility(View.VISIBLE);
                        txtRingtone.setText("Ringtone : "+ ringtoneTitle);
                    }*/

                    String ringTone = session.getRingtone();
                    if (ringTone.equals("Unknown ringtone")){
                        view.setVisibility(View.INVISIBLE);
                        mSwitch.setChecked(false);
                        mSwitch.setClickable(true);
                        txtRingtone.setVisibility(View.INVISIBLE);
                    }else {
                        view.setVisibility(View.VISIBLE);
                        txtRingtone.setVisibility(View.VISIBLE);
                        mSwitch.setChecked(true);
                        mSwitch.setClickable(false);
                        txtRingtone.setText("Ringtone : "+ringTone);
                    }
                    Log.e("SettingsFragment", ringtone.getTitle(getActivity()));

                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == txtRingtone){
            getRingtonePicker();
        }
    }
}
