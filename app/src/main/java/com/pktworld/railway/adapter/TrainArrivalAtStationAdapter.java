package com.pktworld.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pktworld.railway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prabhat on 16/04/16.
 */
public class TrainArrivalAtStationAdapter extends BaseAdapter {
    private static final String TAG = TrainArrivalAtStationAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private JSONArray categoryList = null;

    public TrainArrivalAtStationAdapter(Context context,
                                 JSONArray categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        private TextView txtName,txtNumber,txtScArr,txtScDep,
                txtAcArr,txtAcDep,txtDeArr,txtDeDep;
    }

    @Override
    public int getCount() {
        if(null==categoryList)
            return 0;
        else
            return categoryList.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if(null==categoryList) return null;
        else
            return categoryList.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();

            view = inflater.inflate(R.layout.item_train_arrival_at_station, null);
            holder.txtName = (TextView)view.findViewById(R.id.txtName);
            holder.txtNumber = (TextView)view.findViewById(R.id.txtNumber);
            holder.txtAcArr = (TextView)view.findViewById(R.id.txtActArriv);
            holder.txtAcDep = (TextView)view.findViewById(R.id.txtActDep);
            holder.txtScArr = (TextView)view.findViewById(R.id.txtSchArr);
            holder.txtScDep = (TextView)view.findViewById(R.id.txtSchDep);
            holder.txtDeArr= (TextView)view.findViewById(R.id.txtDelayArr);
            holder.txtDeDep = (TextView)view.findViewById(R.id.txtDelayDep);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject json_data = getItem(position);
        try {
            holder.txtName.setText("Train Name : " + json_data.getString("name"));
            holder.txtNumber.setText("Train Number : " + json_data.getString("number"));
            holder.txtScArr.setText("Schedule Arrival : "+json_data.getString("scharr"));
            holder.txtScDep.setText("Schedule Departure : "+json_data.getString("schdep"));
            holder.txtAcArr.setText("Actual Arrival : "+json_data.getString("actarr"));
            holder.txtAcDep.setText("Actual Departure : "+json_data.getString("actdep"));
            holder.txtDeArr.setText("Delay Arrival : "+json_data.getString("delayarr"));
            holder.txtDeDep.setText("Delay Departure : "+json_data.getString("delaydep"));

        } catch (JSONException e) {
            e.printStackTrace();
        }




        return view;
    }

}
