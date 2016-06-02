package com.idroidms.railway.activity;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.idroidms.railway.R;
import com.idroidms.railway.adapter.TrainRouteAdapter;
import com.idroidms.railway.model.LiveStatus;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.GsonRequestResponseHelper;

import java.util.Calendar;

/**
 * Created by ubuntu1 on 25/3/16.
 */
public class TrainLiveStatusActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = TrainLiveStatusActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private EditText editSearch;
    private static Button btnDate,btnGetLiveStatus;
    private TextView txtTrainNumber, txtPosition,txtError;
    private LinearLayout llData;
    private static String date1 = null;
    private ListView listData;
    private TrainRouteAdapter mAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_status);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("Live Train");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        editSearch = (EditText)findViewById(R.id.editSearch);
        btnDate = (Button)findViewById(R.id.btnDate);
        btnGetLiveStatus = (Button)findViewById(R.id.btnGetLiveStatus);
        txtPosition = (TextView)findViewById(R.id.txtPosition);
        txtError = (TextView)findViewById(R.id.txtError);
        txtTrainNumber = (TextView)findViewById(R.id.txtTrainNumber);
        llData = (LinearLayout)findViewById(R.id.llData);
        listData = (ListView)findViewById(R.id.listData);
        llData.setVisibility(View.GONE);
        btnDate.setOnClickListener(this);
        btnGetLiveStatus.setOnClickListener(this);


        for (int i = 0; i<=1;i++){
            AppUtils.showToastMessage(TrainLiveStatusActivity.this,getString(R.string.live_status_info));
        }

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
        if (v == btnDate){
            pickDate();
        }else if (v == btnGetLiveStatus){
            if (validate()){
                if (AppUtils.isConnected(TrainLiveStatusActivity.this)){
                    getLiveStatus(TrainLiveStatusActivity.this,date1);
                }
            }
        }

    }
    private void pickDate() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    private boolean validate(){

        if (editSearch.getText().toString().trim().length() == 0 || editSearch.getText().toString().trim().isEmpty()){
            AppUtils.showToastMessage(TrainLiveStatusActivity.this, "Please enter Train Number");
            return false;
        }else if (btnDate.getText().toString().equalsIgnoreCase("date")){
            AppUtils.showToastMessage(TrainLiveStatusActivity.this,"Please select journey date");
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
            String date =  dayS + "/" + monthS + "/" + year;
            date1 = ""+year+monthS+dayS;
            btnDate.setText(date);

        }

    }

    private void getLiveStatus(Context mContext, String date) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"live/train/"+
                editSearch.getText().toString().trim()+"/doj/"+date+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<LiveStatus> myReq = new GsonRequestResponseHelper<LiveStatus>(
                Request.Method.GET,
                REQUEST_URL,
                LiveStatus.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());


        mRequestQueue.add(myReq);
    }

    private Response.Listener<LiveStatus> createMyReqSuccessListener() {
        return new Response.Listener<LiveStatus>() {
            @Override
            public void onResponse(LiveStatus response) {
                try {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    if (response.getResponse_code().equals("200")){
                        llData.setVisibility(View.VISIBLE);
                        txtTrainNumber.setText("TRAIN NUMBER : " + response.getTrain_number());
                        txtError.setText("ERROR : " + response.getError());
                        txtPosition.setText("POSITION : " + response.getPosition());

                        mAdapter = new TrainRouteAdapter(TrainLiveStatusActivity.this,response.getRoute());
                        listData.setAdapter(mAdapter);
                        setListViewHeightBasedOnChildren(listData);

                    }else if (response.getResponse_code().equals("204")){
                        AppUtils.showToastMessage(TrainLiveStatusActivity.this,getString(R.string.empty_response));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Log.e("Json ServiceResponse", "TryCatch");
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //String errorMsg = VolleyErrorHelper.getMessage(error, TestVolley.this);
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e("Json ServiceResponse", "Error");
            }
        };
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
