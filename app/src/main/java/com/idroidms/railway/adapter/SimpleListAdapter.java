package com.idroidms.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.model.QuotaCodes;

import java.util.List;

/**
 * Created by Prabhat on 27/03/16.
 */
public class SimpleListAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<QuotaCodes> movieItems;


    public SimpleListAdapter(Context activity, List<QuotaCodes> movieItems) {
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
            convertView = inflater.inflate(R.layout.item_list, null);
        final QuotaCodes m = movieItems.get(position);

        TextView itemName = (TextView) convertView.findViewById(R.id.txtListText);
        itemName.setText(movieItems.get(position).getQuotaName());
        return convertView;
    }



}
