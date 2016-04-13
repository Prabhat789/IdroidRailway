package com.pktworld.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 14/04/16.
 */
public class RouteAdapter extends BaseAdapter {

    private static final String TAG = RouteAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<Route> categoryList = null;

    public RouteAdapter(Context context,
                             List<Route> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<Route>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView stationName, number,scArrival,scDeparture,day,distance,state;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Route getItem(int position) {
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
            view = inflater.inflate(R.layout.item_train_route, null);
            holder.stationName = (TextView) view.findViewById(R.id.txtStationName);
            holder.number = (TextView) view.findViewById(R.id.txtNumber);
            holder.scArrival = (TextView) view.findViewById(R.id.txtScArrival);
            holder.scDeparture = (TextView) view.findViewById(R.id.txtScDeparture);
            holder.day = (TextView) view.findViewById(R.id.txtDay);
            holder.distance = (TextView) view.findViewById(R.id.txtDistance);
            holder.state = (TextView) view.findViewById(R.id.txtState);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.stationName.setText("Station Name : "+categoryList.get(position).getFullname());
        holder.number.setText("No : "+categoryList.get(position).getNo());
        holder.scArrival.setText("Scheduled Arrival : "+categoryList.get(position).getScharr());
        holder.scDeparture.setText("Scheduled Departure : "+categoryList.get(position).getSchdep());
        holder.day.setText("Day : "+categoryList.get(position).getDay());
        holder.distance.setText("Diatance : "+categoryList.get(position).getDistance());
        holder.state.setText("State : "+categoryList.get(position).getState());

        return view;
    }





}
