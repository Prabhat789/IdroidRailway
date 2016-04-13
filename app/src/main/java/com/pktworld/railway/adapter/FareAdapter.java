package com.pktworld.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pktworld.railway.R;
import com.pktworld.railway.model.Fare;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 14/04/16.
 */
public class FareAdapter extends BaseAdapter {

    private static final String TAG = FareAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<Fare> categoryList = null;

    public FareAdapter(Context context,
                               List<Fare> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<Fare>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        TextView txtClass,txtFare;

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Fare getItem(int position) {
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
            holder.txtClass = (TextView) view.findViewById(R.id.txtDate);
            holder.txtFare = (TextView) view.findViewById(R.id.txtStatus);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtClass.setText("Class : "+categoryList.get(position).getName());
        holder.txtFare.setText("Fare :  Rs. "+categoryList.get(position).getFare()+" /- ");


        return view;
    }


}
