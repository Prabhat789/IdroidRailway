package com.idroidms.railway.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.idroidms.railway.R;
import com.idroidms.railway.adapter.AvailabilityAdapter;
import com.idroidms.railway.adapter.SimpleListAdapter;
import com.idroidms.railway.model.QuotaCodes;
import com.idroidms.railway.model.SeatAvilabilityResponse;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.GsonRequestResponseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Prabhat on 26/03/16.
 */
public class SeatAvilabilityActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SeatAvilabilityActivity.class.getSimpleName();
    private static Button btnSelectDate, btnSelectQuota, btnGo, btnClass;
    private EditText editSourcestation, editDestinationStation, editTrainNumber;
    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;
    private LinearLayout llDatacont;
    private static String date1 = null;
    private String classString = null;
    private String quotaString = null;
    private TextView txtTrainName, txtTrainNumber,
            txtTrainFrom,txtTrainTo,txtQuota, txtLastUpadte;
    private ListView listAvailability;
    private AvailabilityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("Seat Availability");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        editSourcestation = (EditText)findViewById(R.id.editSourceStation);
        editSourcestation.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editDestinationStation = (EditText)findViewById(R.id.editDestinationStation);
        editDestinationStation.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editTrainNumber = (EditText)findViewById(R.id.editTrainNumber);
        btnClass = (Button)findViewById(R.id.btnSelectClass);
        btnSelectDate = (Button)findViewById(R.id.btnSelectDate);
        btnSelectQuota = (Button)findViewById(R.id.btnSelectQuota);
        btnGo = (Button)findViewById(R.id.btnGo);
        listAvailability = (ListView)findViewById(R.id.listAvailability);
        llDatacont = (LinearLayout)findViewById(R.id.lldataCont);
        llDatacont.setVisibility(View.GONE);

        txtQuota = (TextView)findViewById(R.id.txtQuota);
        txtTrainFrom = (TextView)findViewById(R.id.txtFrom);
        txtTrainTo = (TextView)findViewById(R.id.txtTo);
        txtTrainName = (TextView)findViewById(R.id.txtTrainName);
        txtTrainNumber = (TextView)findViewById(R.id.txtTrainNumber);
        txtLastUpadte = (TextView)findViewById(R.id.txtLastUpdate1);


        btnGo.setOnClickListener(this);
        btnClass.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);
        btnSelectQuota.setOnClickListener(this);
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
    private void pickDate() {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Override
    public void onClick(View v) {
        if (v == btnSelectDate){
            pickDate();
        }else if (v == btnSelectQuota){
            showQuotaDialog(SeatAvilabilityActivity.this, "Select Quota :-");
        }else if(v == btnGo){
            if (validate()){
                if (AppUtils.isConnected(SeatAvilabilityActivity.this)){
                    getSeatAvailability(SeatAvilabilityActivity.this,date1);
                }
            }
        }else if (v == btnClass){
            showClassDialog(SeatAvilabilityActivity.this,"Select class");
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
            btnSelectDate.setText(date);

        }

    }

    void showQuotaDialog(Context mContext,String title){
        final Dialog gameOver = new Dialog(mContext);
        gameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOver.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOver.setCancelable(false);
        gameOver.setContentView(R.layout.dialog_list);

        ListView list = (ListView)gameOver.findViewById(R.id.dialogList);
        TextView txtTitle = (TextView)gameOver.findViewById(R.id.txtDialogTitle);
        txtTitle.setText(title);
        final List<QuotaCodes> arrayAdapter = new ArrayList<QuotaCodes>();
        arrayAdapter.add(0,new QuotaCodes("GN","General Quota"));
        arrayAdapter.add(1,new QuotaCodes("LD","Ladies Quota"));
        arrayAdapter.add(2,new QuotaCodes("HO","Head quarters/high official Quota"));
        arrayAdapter.add(3,new QuotaCodes("DF","Defence Quota"));
        arrayAdapter.add(4,new QuotaCodes("PH","Parliament house Quota"));
        arrayAdapter.add(5,new QuotaCodes("FT","Foreign Tourist Quota"));
        arrayAdapter.add(6,new QuotaCodes("DP","Duty Pass Quota"));
        arrayAdapter.add(7,new QuotaCodes("CK","Tatkal Quota"));
        arrayAdapter.add(8,new QuotaCodes("PT","Premium Tatkal Quota"));
        arrayAdapter.add(9,new QuotaCodes("SS","Female(above 45 Year)/Senior Citizen/Travelling alone"));
        arrayAdapter.add(10,new QuotaCodes("HP","Physically Handicapped Quota"));
        arrayAdapter.add(11,new QuotaCodes("RE","Railway Employee Staff on Duty for the train"));
        arrayAdapter.add(12,new QuotaCodes("GNRS","General Quota Road Side"));
        arrayAdapter.add(13,new QuotaCodes("OS","Out Station"));
        arrayAdapter.add(14,new QuotaCodes("PQ","Pooled Quota"));
        arrayAdapter.add(15,new QuotaCodes("RC","Reservation Against Cancellation"));
        arrayAdapter.add(16,new QuotaCodes("RS","Road Side"));
        arrayAdapter.add(17,new QuotaCodes("YU","Yuva"));
        arrayAdapter.add(18,new QuotaCodes("LB","Lower Berth"));

        SimpleListAdapter mAdapter = new SimpleListAdapter(mContext,arrayAdapter);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = arrayAdapter.get(position).getQuotaName();
                btnSelectQuota.setText(data);
                quotaString = arrayAdapter.get(position).getCode();
                gameOver.dismiss();
            }
        });
        gameOver.show();

    }

    void showClassDialog(Context mContext, String title){

        final Dialog gameOver = new Dialog(mContext);
        gameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameOver.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameOver.setCancelable(false);
        gameOver.setContentView(R.layout.dialog_list);

        ListView list = (ListView)gameOver.findViewById(R.id.dialogList);
        TextView txtTitle = (TextView)gameOver.findViewById(R.id.txtDialogTitle);
        txtTitle.setText(title);
        final List<QuotaCodes> arrayAdapter = new ArrayList<QuotaCodes>();
        arrayAdapter.add(0,new QuotaCodes("1A","First class Air-Conditioned (AC)"));
        arrayAdapter.add(1,new QuotaCodes("2A","AC 2-tier sleeper"));
        arrayAdapter.add(2,new QuotaCodes("FC","First class"));
        arrayAdapter.add(3,new QuotaCodes("3A","AC 3 Tier"));
        arrayAdapter.add(4,new QuotaCodes("AC3","3 E - AC 3 Tier Economy"));
        arrayAdapter.add(5,new QuotaCodes("CC","AC chair Car"));
        arrayAdapter.add(6,new QuotaCodes("SL","Sleeper Class"));
        arrayAdapter.add(7,new QuotaCodes("2S","Second Sitting"));

        SimpleListAdapter mAdapter = new SimpleListAdapter(mContext,arrayAdapter);
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = arrayAdapter.get(position).getQuotaName();
                btnClass.setText(data);
                classString = arrayAdapter.get(position).getCode();
                gameOver.dismiss();
            }
        });
        gameOver.show();

    }

    private boolean validate(){
        if (editTrainNumber.getText().toString().trim().length() == 0 || editTrainNumber.getText().toString().trim().isEmpty()){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this, "Please enter Train Number");
            return false;
        }else if (editSourcestation.getText().toString().trim().length() == 0 || editSourcestation.getText().toString().trim().isEmpty()){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this, "Please enter Source Station Code");
            return false;
        }else if (editDestinationStation.getText().toString().trim().length() == 0 || editDestinationStation.getText().toString().trim().isEmpty()){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this, "Please enter Destination Station Code");
            return false;
        }
        else if (btnSelectDate.getText().toString().equalsIgnoreCase("date")){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this,"Please select date");
            return false;
        }
        else if (btnSelectQuota.getText().toString().equalsIgnoreCase("quota")){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this,"Please select Quota");
            return false;
        }
        else if (btnClass.getText().toString().equalsIgnoreCase("class")){
            AppUtils.showToastMessage(SeatAvilabilityActivity.this,"Please select Class");
            return false;
        }else {
            return true;
        }
    }


    private void getSeatAvailability(Context mContext, String date) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
       // http://api.railwayapi.com/check_seat/train/12001/source/BPL/dest/NDLS/date/14-10-2014/class/CC/quota/GN/apikey/myapikey/

        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"check_seat/train/"+
                editTrainNumber.getText().toString().trim()+"/source/"+editSourcestation.getText().toString()+
                "/dest/"+editDestinationStation.getText().toString()+"/date/"+date+"/class/"+classString+"/quota/"
                +quotaString+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        Log.e(TAG,REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<SeatAvilabilityResponse> myReq = new GsonRequestResponseHelper<SeatAvilabilityResponse>(
                Request.Method.GET,
                REQUEST_URL,
                SeatAvilabilityResponse.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

       /* http://api.railwayapi.com/check_seat/train/12001/source/BPL/dest/NDLS/date/28-03-2016/class/CC/quota/GN/apikey/uyhif2848/
        http://api.railwayapi.com/check_seat/train/12001/source/BPL/dest/NDLS/date/28-03-2016/class/FC/quota/GN/apikey/uyhif2848/*/
        mRequestQueue.add(myReq);
    }

    private Response.Listener<SeatAvilabilityResponse> createMyReqSuccessListener() {
        return new Response.Listener<SeatAvilabilityResponse>() {
            @Override
            public void onResponse(SeatAvilabilityResponse response) {
                try {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    if (response.getResponse_code().equals("200")){
                        llDatacont.setVisibility(View.VISIBLE);
                        txtTrainNumber.setText("Train Number : " + response.getTrain_number());
                        txtTrainName.setText("Train Name : " + response.getTrain_name());
                        txtTrainFrom.setText("From : "+response.getFrom().getName()+" ("+response.getFrom().getCode()+")");
                        txtTrainTo.setText("To : "+response.getTo().getName()+" ("+response.getTo().getCode()+")");
                        txtQuota.setText("Quota : "+response.getQuota().getQuota_name());
                        txtLastUpadte.setText("Last Update : "+response.getLast_updated().getTime()
                                +", "+response.getLast_updated().getDate());

                        mAdapter = new AvailabilityAdapter(SeatAvilabilityActivity.this,response.getAvailability());
                        listAvailability.setAdapter(mAdapter);
                        AppUtils.setListViewHeightBasedOnChildren(listAvailability);

                    }else if (response.getResponse_code().equals("204")){
                        llDatacont.setVisibility(View.VISIBLE);
                        txtTrainNumber.setText("Train Number : " + response.getTrain_number());
                        txtTrainName.setText("Train Name : "+response.getTrain_name());
                        txtTrainFrom.setText("From : "+response.getFrom().getName()+" ("+response.getFrom().getCode()+")");
                        txtTrainTo.setText("To : "+response.getTo().getName()+" ("+response.getTo().getCode()+")");
                        txtQuota.setText("Quota : "+response.getQuota().getQuota_name());
                        txtLastUpadte.setText("Last Update : "+response.getLast_updated().getTime()
                                +", "+response.getLast_updated().getDate());
                        mAdapter = new AvailabilityAdapter(SeatAvilabilityActivity.this,response.getAvailability());
                        listAvailability.setAdapter(mAdapter);
                        AppUtils.setListViewHeightBasedOnChildren(listAvailability);

                        AppUtils.showToastMessage(SeatAvilabilityActivity.this,response.getError());
                    }else{
                        llDatacont.setVisibility(View.GONE);
                        listAvailability.setVisibility(View.GONE);
                        AppUtils.showToastMessage(SeatAvilabilityActivity.this,getString(R.string.empty_response));
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
                //String errorMsg = VolleyErrorHelper.getMessage(error, TestVolley.this);
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e(TAG, error.toString());
            }
        };
    }


}
