package com.pktworld.railway.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pktworld.railway.R;
import com.pktworld.railway.adapter.CancelledTrainAdapter;
import com.pktworld.railway.model.CancelTrainResponse;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.GsonRequestResponseHelper;
import com.pktworld.railway.util.Utils;

import java.util.Calendar;

/**
 * Created by Prabhat on 16/04/16.
 */
public class CancelTrainActivity extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = CancelTrainActivity.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;
    private static Button btnDate,btnSearch;
    private static String date1 = null;
    private ListView listCancelTrains;
    private CancelledTrainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_train);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("Cancelled Train");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        btnSearch = (Button)findViewById(R.id.btnGo);
        btnDate = (Button)findViewById(R.id.btnDate);
        listCancelTrains = (ListView)findViewById(R.id.listTrains);

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
                if (Utils.isConnected(CancelTrainActivity.this)){
                    getCancelledTrains(CancelTrainActivity.this, date1);
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
    private boolean validate(){
        if (btnDate.getText().toString().equalsIgnoreCase("Date")){
            Utils.showToastMessage(CancelTrainActivity.this,"Please select Date");
            return false;
        }else {
            return true;
        }

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
            String date =  dayS + "-" + monthS + "-" + year;
            date1 = date;
            btnDate.setText(date);

        }

    }

    private void getCancelledTrains(Context mContext, String date) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"cancelled/"+"/date/"+date+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        Log.e(TAG, REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<CancelTrainResponse> myReq = new GsonRequestResponseHelper<CancelTrainResponse>(
                Request.Method.GET,
                REQUEST_URL,
                CancelTrainResponse.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);


    }

    private Response.Listener<CancelTrainResponse> createMyReqSuccessListener() {
        return new Response.Listener<CancelTrainResponse>() {
            @Override
            public void onResponse(CancelTrainResponse response) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                try {

                    if (response.getResponse_code().equals("200")){
                        mAdapter = new CancelledTrainAdapter(CancelTrainActivity.this,response.getTrains());
                        listCancelTrains.setAdapter(mAdapter);
                        Utils.setListViewHeightBasedOnChildren(listCancelTrains);
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                    }else{
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }
                        Utils.showToastMessage(CancelTrainActivity.this,getString(R.string.empty_response));
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
