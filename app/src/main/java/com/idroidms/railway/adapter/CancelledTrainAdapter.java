package com.idroidms.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.model.Trains;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prabhat on 16/04/16.
 */
public class CancelledTrainAdapter extends BaseAdapter {

    private static final String TAG = CancelledTrainAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private List<Trains> categoryList = null;
    public CancelledTrainAdapter(Context context,
                                 List<Trains> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
        this.categoryList = new ArrayList<Trains>();
        this.categoryList.addAll(categoryList);
    }

    public class ViewHolder {
        private TextView txtName,txtNumber,txtStartTime,txtType,
        txtSource,txtDestination;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Trains getItem(int position) {
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

            view = inflater.inflate(R.layout.item_cancelled_train, null);
            holder.txtName = (TextView)view.findViewById(R.id.txtName);
            holder.txtNumber = (TextView)view.findViewById(R.id.txtNumber);
            holder.txtType = (TextView)view.findViewById(R.id.txtType);
            holder.txtStartTime = (TextView)view.findViewById(R.id.txtStartTime);
            holder.txtSource = (TextView)view.findViewById(R.id.txtSource);
            holder.txtDestination = (TextView)view.findViewById(R.id.txtDestination);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //JSONObject json_data = getItem(position);

          /*  holder.txtName.setText("Train Name : " + json_data.getJSONObject("train").getString("name"));
            holder.txtNumber.setText("Train Number : " + json_data.getJSONObject("train").getString("number"));
            holder.txtType.setText("Type : "+json_data.getJSONObject("train").getString("type"));
            holder.txtStartTime.setText("Start Time : "+json_data.getJSONObject("train").getString("start_time"));
            holder.txtSource.setText("Source : "+json_data.getJSONObject("dest").getString("name"));
            holder.txtDestination.setText("Destination : "+json_data.getJSONObject("source").getString("name"));*/

            holder.txtName.setText("Train Name : " + categoryList.get(position).getTrain().getName());
            holder.txtNumber.setText("Train Number : " + categoryList.get(position).getTrain().getNumber());
            holder.txtType.setText("Type : "+categoryList.get(position).getTrain().getType());
            holder.txtStartTime.setText("Start Time : "+categoryList.get(position).getTrain().getStart_time());
            holder.txtSource.setText("Source : "+categoryList.get(position).getSource().getName());
            holder.txtDestination.setText("Destination : "+categoryList.get(position).getDest().getName());


        return view;
    }


}
