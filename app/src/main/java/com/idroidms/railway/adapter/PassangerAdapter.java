package com.idroidms.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.model.Passengers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 26/03/16.
 */
public class PassangerAdapter extends BaseAdapter {

    private static final String TAG = TrainRouteAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<Passengers> categoryList = null;

    public PassangerAdapter(Context context,
                             List<Passengers> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<Passengers>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView txtBookingStatus,txtCurrentStatus,txtCoach;
        ImageView IconFile;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Passengers getItem(int position) {
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
            view = inflater.inflate(R.layout.item_passangers, null);
            holder.txtBookingStatus = (TextView) view.findViewById(R.id.txtBookingStatus);
            holder.txtCurrentStatus = (TextView) view.findViewById(R.id.txtCurrentStatus);
            holder.txtCoach = (TextView) view.findViewById(R.id.txtCoachPosition);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtBookingStatus.setText("Booking Status : "+categoryList.get(position).getBooking_status());
        holder.txtCurrentStatus.setText("Current Status : "+categoryList.get(position).getCurrent_status());
        holder.txtCoach.setText("Coach Position : "+categoryList.get(position).getCoach_position());


        return view;
    }



}
