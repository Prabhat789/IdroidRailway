package com.pktworld.railway.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.NavDrawerModel;

import java.util.List;


/**
 * Created by ubuntu1 on 25/2/16.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<NavDrawerModel> movieItems;


    public NavigationDrawerAdapter(Context activity, List<NavDrawerModel> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_nav_drawer, null);
        // getting movie data for the row
       final NavDrawerModel m = movieItems.get(position);

        TextView itemName = (TextView) convertView.findViewById(R.id.txt);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.drawerItem);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("LOG","CLICL1100");
                sendMessage(m.getId());
            }
        });
        itemName.setText(m.getTitle());
        //itemQuantity.setText(.m.getQuantity());
        icon.setImageResource(movieItems.get(position).getIcon());




        return convertView;
    }



    private void sendMessage(int pos) {
        Intent intent = new Intent("custom-event-name");
        intent.putExtra("message", pos);
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
    }

}
