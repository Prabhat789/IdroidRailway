package com.idroidms.railway.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idroidms.railway.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ubuntu1 on 14/4/16.
 */
public class TrainBetweenStationsAdapter extends BaseAdapter {

    private static final String TAG = TrainBetweenStationsAdapter.class.getSimpleName();
    Context mContext;
    LayoutInflater inflater;
    private JSONArray categoryList = null;

    public TrainBetweenStationsAdapter(Context context,
                               JSONArray categoryList) {
        mContext = context;
        this.categoryList = categoryList;
        inflater = LayoutInflater.from(mContext);
    }

    public class ViewHolder {
        private TextView txtName,txtNumber,txtDepTime,txtAarTime,
                txtTravlTime,txtFrom,txtTo,txtSun,txtMon,txtTue,txtWed,
                txtThu,txtFri,txtSat,txtFc,txt3E,txtCc,txtSl,txt2s,txt2a,txt3a,txt1a;
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

            view = inflater.inflate(R.layout.item_train_between_station, null);
            holder.txtName = (TextView)view.findViewById(R.id.txtName);
            holder.txtNumber = (TextView)view.findViewById(R.id.txtNumber);
            holder.txtDepTime = (TextView)view.findViewById(R.id.txtDepTime);
            holder.txtAarTime = (TextView)view.findViewById(R.id.txtArrTime);
            holder.txtTravlTime = (TextView)view.findViewById(R.id.txtTravlTime);
            holder.txtFrom = (TextView)view.findViewById(R.id.txtFrom);
            holder.txtTo = (TextView)view.findViewById(R.id.txtTo);
            holder.txtSun = (TextView)view.findViewById(R.id.txtSun);
            holder.txtMon = (TextView)view.findViewById(R.id.txtMon);
            holder.txtTue = (TextView)view.findViewById(R.id.txtTue);
            holder.txtWed = (TextView)view.findViewById(R.id.txtWed);
            holder.txtThu = (TextView)view.findViewById(R.id.txtThu);
            holder.txtFri = (TextView)view.findViewById(R.id.txtFri);
            holder.txtSat = (TextView)view.findViewById(R.id.txtSat);
            holder.txtFc = (TextView)view.findViewById(R.id.txtFc);
            holder.txt3E = (TextView)view.findViewById(R.id.txt3E);
            holder.txtCc = (TextView)view.findViewById(R.id.txtCc);
            holder.txtSl = (TextView)view.findViewById(R.id.txtSl);
            holder.txt2s = (TextView)view.findViewById(R.id.txt2S);
            holder.txt2a = (TextView)view.findViewById(R.id.txt2A);
            holder.txt3a = (TextView)view.findViewById(R.id.txt3A);
            holder.txt1a = (TextView)view.findViewById(R.id.txt1A);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        JSONObject json_data = getItem(position);
        try {
            holder.txtName.setText("Train Name : " + json_data.getString("name"));
            holder.txtNumber.setText("Train Number : " + json_data.getString("number"));
            holder.txtDepTime.setText("Source Departure Time : "+json_data.getString("src_departure_time"));
            holder.txtAarTime.setText("Destination Arrival Time : "+json_data.getString("dest_arrival_time"));
            holder.txtTravlTime.setText("Traval Time : "+json_data.getString("travel_time"));
            holder.txtFrom.setText("From : "+json_data.getJSONObject("from").getString("name")
                    +" ("+json_data.getJSONObject("from").getString("code")+")");
            holder.txtTo.setText("To : "+json_data.getJSONObject("to").getString("name")
                    +" ("+json_data.getJSONObject("to").getString("code")+")");
            holder.txtSun.setText(json_data.getJSONArray("days").getJSONObject(0).getString("runs"));
            holder.txtMon.setText(json_data.getJSONArray("days").getJSONObject(1).getString("runs"));
            holder.txtTue.setText(json_data.getJSONArray("days").getJSONObject(2).getString("runs"));
            holder.txtWed.setText(json_data.getJSONArray("days").getJSONObject(3).getString("runs"));
            holder.txtThu.setText(json_data.getJSONArray("days").getJSONObject(4).getString("runs"));
            holder.txtFri.setText(json_data.getJSONArray("days").getJSONObject(5).getString("runs"));
            holder.txtSat.setText(json_data.getJSONArray("days").getJSONObject(6).getString("runs"));
            holder.txtFc.setText(json_data.getJSONArray("classes").getJSONObject(0).getString("available"));
            holder.txt3E.setText(json_data.getJSONArray("classes").getJSONObject(1).getString("available"));
            holder.txtCc.setText(json_data.getJSONArray("classes").getJSONObject(2).getString("available"));
            holder.txtSl.setText(json_data.getJSONArray("classes").getJSONObject(3).getString("available"));
            holder.txt2s.setText(json_data.getJSONArray("classes").getJSONObject(4).getString("available"));
            holder.txt2a.setText(json_data.getJSONArray("classes").getJSONObject(5).getString("available"));
            holder.txt3a.setText(json_data.getJSONArray("classes").getJSONObject(6).getString("available"));
            holder.txt1a.setText(json_data.getJSONArray("classes").getJSONObject(7).getString("available"));
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return view;
    }




}
