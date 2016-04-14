package com.pktworld.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.Avilability;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu1 on 14/4/16.
 */
public class TrainBetweenStationsAdapter extends BaseAdapter {

    private static final String TAG = TrainRouteAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<Avilability> categoryList = null;

    public TrainBetweenStationsAdapter(Context context,
                               List<Avilability> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<Avilability>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView txtBookingStatus,txtCurrentStatus;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Avilability getItem(int position) {
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
            view = inflater.inflate(R.layout.item_availability, null);
            holder.txtBookingStatus = (TextView) view.findViewById(R.id.txtDate);
            holder.txtCurrentStatus = (TextView) view.findViewById(R.id.txtStatus);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtBookingStatus.setText("Date : "+categoryList.get(position).getDate());
        holder.txtCurrentStatus.setText("Status : "+categoryList.get(position).getStatus());


        return view;
    }




}
