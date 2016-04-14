package com.pktworld.railway.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pktworld.railway.R;
import com.pktworld.railway.adapter.TrainBetweenStationsAdapter;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.GsonRequestResponseHelper;
import com.pktworld.railway.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ubuntu1 on 14/4/16.
 */
public class TrainBetweenStationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TrainBetweenStationActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;
    private ListView listTrains;
    private EditText editSourcestation, editDestinationStation;
    private static Button btnSearch,btnDate;

    private static String date1 = null;
    private TrainBetweenStationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_bw_station);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("Train Between Station");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        btnSearch = (Button)findViewById(R.id.btnGo);
        btnDate = (Button)findViewById(R.id.btnDate);
        editSourcestation = (EditText)findViewById(R.id.editSourceStation);
        editSourcestation.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editDestinationStation = (EditText)findViewById(R.id.editDestinationStation);
        editDestinationStation.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        listTrains = (ListView)findViewById(R.id.listTrains);

        btnSearch.setOnClickListener(this);
        btnDate.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSearch){
            if (validate()){
                if (Utils.isConnected(TrainBetweenStationActivity.this)){
                    getTrains(TrainBetweenStationActivity.this, date1);
                }
            }
        }else if (v == btnDate){
            pickDate();
        }
    }

    private void pickDate() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            String monthS = null;
            String dayS = null;
            if (month < 10){
                monthS = "0"+month;
            }else {
                monthS = ""+month;
            }
            if (day < 10){
                dayS = "0"+day;
            }else {
                dayS = ""+day;
            }
            String date =  dayS + "-" + monthS;
            date1 = date;
            btnDate.setText(date);

        }

    }

    private boolean validate() {
        if (editSourcestation.getText().toString().trim().length() == 0 || editSourcestation.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(TrainBetweenStationActivity.this, "Please enter Source Station Code");
            return false;
        }else if (editDestinationStation.getText().toString().trim().length() == 0 || editDestinationStation.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(TrainBetweenStationActivity.this, "Please enter Destination Station Code");
            return false;
        }else if (btnDate.getText().toString().equalsIgnoreCase("Date")){
            Utils.showToastMessage(TrainBetweenStationActivity.this,"Please select Date");
            return false;
        }else {
            return true;
        }
    }

    private void getTrains(Context mContext, String date) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"between/source/"+editSourcestation.getText().toString()+
                "/dest/"+editDestinationStation.getText().toString()+"/date/"+date+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        Log.e(TAG, REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<String> myReq = new GsonRequestResponseHelper<String>(
                Request.Method.GET,
                REQUEST_URL,
                String.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);
    }

    private Response.Listener<String> createMyReqSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Log.e(TAG,response.toString());
                    JSONObject response1 = new JSONObject(response);
                    if (response1.getString("response_code").equals("200")){
                        //int trainCount = response.getInt("total");
                        JSONArray train = response1.getJSONArray("train");

                        mAdapter = new TrainBetweenStationsAdapter(TrainBetweenStationActivity.this,train);
                        listTrains.setAdapter(mAdapter);
                        Utils.setListViewHeightBasedOnChildren(listTrains);
                    }else{
                        Utils.showToastMessage(TrainBetweenStationActivity.this,getString(R.string.empty_response));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Log.e(TAG, "TryCatch");
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e(TAG, error.toString());
            }
        };
    }

}
