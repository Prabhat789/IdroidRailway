package com.pktworld.railway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.pktworld.railway.R;
import com.pktworld.railway.adapter.PassangerAdapter;
import com.pktworld.railway.model.PnrResponse;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.GsonRequestResponseHelper;
import com.pktworld.railway.util.Utils;

/**
 * Created by Prabhat on 26/03/16.
 */
public class PnrStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PnrStatusActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private EditText editPnr;
    private Button btnSubmit;
    private TextView txtTrainNumber,txtPnr,txtDoj,
            txtTotalPass,txtTrainName,txtChartPrep,txtTrainStartDate,txtFromStation,txtToStation,txtpas;
    private RequestQueue mRequestQueue;
    private LinearLayout llDatacont;
    private PassangerAdapter mAdapers;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnr_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("PNR Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        llDatacont = (LinearLayout)findViewById(R.id.lldataCont);
        llDatacont.setVisibility(View.GONE);
        editPnr = (EditText)findViewById(R.id.editPnrNumber);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        txtChartPrep = (TextView)findViewById(R.id.txtChartPrep);
        txtTrainName = (TextView)findViewById(R.id.txtTrainName);
        txtTotalPass = (TextView)findViewById(R.id.txtTotalPass);
        txtDoj = (TextView)findViewById(R.id.txtDoj);
        txtPnr = (TextView)findViewById(R.id.txtPnr);
        txtTrainNumber = (TextView)findViewById(R.id.txtTrainNumber);
        txtTrainStartDate = (TextView)findViewById(R.id.txtTrainStartDate);
        txtFromStation = (TextView)findViewById(R.id.txtFrom);
        txtToStation = (TextView)findViewById(R.id.txtTo);
        mList = (ListView)findViewById(R.id.listPassangers);
        txtpas = (TextView)findViewById(R.id.txtpas);
        txtpas.setVisibility(View.GONE);


        btnSubmit.setOnClickListener(this);

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
        if (v == btnSubmit){
            if (editPnr.getText().toString().trim().isEmpty() || editPnr.getText().toString().length() == 0){
                Utils.showToastMessage(PnrStatusActivity.this,"Enter Pnr Number");
            }else {
                if (Utils.isConnected(PnrStatusActivity.this)){
                    getPnrStatus(PnrStatusActivity.this);
                }
            }
        }
    }



    private void getPnrStatus(Context mContext){
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        //"http://api.railwayapi.com/pnr_status/pnr/1234567890/apikey/myapikey/"
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"pnr_status/pnr/"+
                editPnr.getText().toString().trim()+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<PnrResponse> myReq = new GsonRequestResponseHelper<PnrResponse>(
                Request.Method.GET,
                REQUEST_URL,
                PnrResponse.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                ApplicationConstants.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);
    }

    private Response.Listener<PnrResponse> createMyReqSuccessListener() {
        return new Response.Listener<PnrResponse>() {
            @Override
            public void onResponse(PnrResponse response) {
                try {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    if (response.getResponse_code().equals("200")){
                        llDatacont.setVisibility(View.VISIBLE);
                        txtpas.setVisibility(View.VISIBLE);

                        txtTrainName.setText("Train Name : " + response.getTrain_name());
                        txtTrainNumber.setText("Train Number : " + response.getTrain_num());
                        txtPnr.setText("PNR : "+response.getPnr());
                        txtDoj.setText("Date Of Journey : "+response.getDoj());
                        txtChartPrep.setText("Chart Prepared : "+response.getChart_prepared());
                        txtTotalPass.setText("Total Passengers : "+response.getTotal_passengers());
                        txtTrainStartDate.setText("Start Date : "+response.getTrain_start_date().getDay()+"/"+
                        response.getTrain_start_date().getMonth()+"/"+response.getTrain_start_date().getYear());
                        txtFromStation.setText("From Station : "+response.getFrom_station().getName()+" ["+response.getFrom_station().getCode()+"]");
                        txtToStation.setText("To Station : "+response.getTo_station().getName()+" ["+response.getTo_station().getCode()+"]");

                        mAdapers = new PassangerAdapter(PnrStatusActivity.this,response.getPassengers());
                        mList.setAdapter(mAdapers);
                        //txtBookingStatus.setText("Booking Status : "+response.get);


                    }else {
                        Utils.showToastMessage(PnrStatusActivity.this,response.getError());
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
                } Log.e(TAG, "Error");
            }
        };
    }
}
