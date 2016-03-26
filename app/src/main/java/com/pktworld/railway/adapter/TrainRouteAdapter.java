package com.pktworld.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.RouteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 26/03/16.
 */
public class TrainRouteAdapter extends BaseAdapter {

    private static final String TAG = TrainRouteAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<RouteList> categoryList = null;

    public TrainRouteAdapter(Context context,
                           List<RouteList> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<RouteList>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView stationName, number,acArrival,
                acDeparture,scArrival,scDeparture,day,distance,lateMinute;
        ImageView IconFile;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public RouteList getItem(int position) {
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
            view = inflater.inflate(R.layout.item_route, null);
            holder.stationName = (TextView) view.findViewById(R.id.txtStationName);
            holder.number = (TextView) view.findViewById(R.id.txtNumber);
            holder.acArrival = (TextView) view.findViewById(R.id.txtAcArrival);
            holder.acDeparture = (TextView) view.findViewById(R.id.txtAcDeparture);
            holder.scArrival = (TextView) view.findViewById(R.id.txtScArrival);
            holder.scDeparture = (TextView) view.findViewById(R.id.txtScDeparture);
            holder.day = (TextView) view.findViewById(R.id.txtDay);
            holder.distance = (TextView) view.findViewById(R.id.txtDistance);
            holder.lateMinute = (TextView) view.findViewById(R.id.txtLateMinute);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.stationName.setText("Station Name : "+categoryList.get(position).getStation_().getName());
        holder.number.setText("No : "+categoryList.get(position).getNo());
        holder.acArrival.setText("Actual Arrival : "+categoryList.get(position).getActarr()+", "+categoryList.get(position).getActarr_date());
        holder.acDeparture.setText("Actual Departure : "+categoryList.get(position).getActdep());
        holder.scArrival.setText("Scheduled Arrival : "+categoryList.get(position).getScharr()+", "+categoryList.get(position).getScharr_date());
        holder.scDeparture.setText("Scheduled Departure : "+categoryList.get(position).getSchdep());
        holder.day.setText("Day : "+categoryList.get(position).getDay());
        holder.distance.setText("Diatance : "+categoryList.get(position).getDistance());
        holder.lateMinute.setText("Late : "+categoryList.get(position).getLatemin());

        return view;
    }



}