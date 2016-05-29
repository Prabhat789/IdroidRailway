package com.pktworld.railway.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.RingtoneList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu1 on 29/5/16.
 */
public class RingtoneListAdapter extends BaseAdapter {

    private static final String TAG = TrainRouteAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<RingtoneList> categoryList = null;

    public RingtoneListAdapter(Context context,
                               List<RingtoneList> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<RingtoneList>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView txtBookingStatus;
        ImageView imagePlay;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public RingtoneList getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_ringtone_list, null);
            holder.txtBookingStatus = (TextView) view.findViewById(R.id.txtListText);
            holder.imagePlay = (ImageView)view.findViewById(R.id.imageView);




            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtBookingStatus.setText(categoryList.get(position).getNotificationTitle());
        holder.txtBookingStatus.setTextColor(mContext.getResources().getColor(R.color.white));

        holder.imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRingtone(mContext,categoryList.get(position).getNotificationUri());
            }
        });


        return view;
    }


    private void playRingtone(Context context,String uri){
        Log.e(TAG,uri);
        Uri notification = Uri.parse(uri);
        /*MediaPlayer mp = MediaPlayer.create(context, notification);
        mp.start();*/
        /*Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();*/

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(notification, "audio/*");
        context.startActivity(intent);
    }

}
