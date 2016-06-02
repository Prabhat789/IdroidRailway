package com.idroidms.railway.activity;

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
import com.idroidms.railway.R;
import com.idroidms.railway.adapter.RouteAdapter;
import com.idroidms.railway.model.TrainRouteResponse;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.GsonRequestResponseHelper;
import com.idroidms.railway.util.AppUtils;


/**
 * Created by Prabhat on 14/04/16.
 */
public class TrainRouteActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = TrainRouteActivity.class.getSimpleName();
    private TextView txtSun,txtMon,txtTue,txtWed,txtThu,txtFri,txtSat;
    private EditText editTrainNumber;
    private Button btnSubmit;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;
    private LinearLayout llDatacont;
    private ListView listRoute;
    private RouteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_route);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("Train Route");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        txtSun = (TextView)findViewById(R.id.txtSun);
        txtMon = (TextView)findViewById(R.id.txtMon);
        txtTue = (TextView)findViewById(R.id.txtTue);
        txtWed = (TextView)findViewById(R.id.txtWed);
        txtThu = (TextView)findViewById(R.id.txtThu);
        txtFri = (TextView)findViewById(R.id.txtFri);
        txtSat = (TextView)findViewById(R.id.txtSat);
        editTrainNumber = (EditText)findViewById(R.id.editTrainNumber);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        llDatacont = (LinearLayout)findViewById(R.id.lldataCont);
        listRoute = (ListView)findViewById(R.id.listAvailability);
        llDatacont.setVisibility(View.GONE);

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
            if (editTrainNumber.getText().toString().trim().isEmpty() || editTrainNumber.getText().toString().length() == 0){
                AppUtils.showToastMessage(TrainRouteActivity.this, "Enter Train Number");
            }else {
                if (AppUtils.isConnected(TrainRouteActivity.this)){
                    getTrainRoute(TrainRouteActivity.this);
                }
            }
        }
    }

    private void getTrainRoute(Context mContext) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"/route/train/"+
                editTrainNumber.getText().toString().trim()+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        Log.e(TAG, REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<TrainRouteResponse> myReq = new GsonRequestResponseHelper<TrainRouteResponse>(
                Request.Method.GET,
                REQUEST_URL,
                TrainRouteResponse.class,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);
    }

    private Response.Listener<TrainRouteResponse> createMyReqSuccessListener() {
        return new Response.Listener<TrainRouteResponse>() {
            @Override
            public void onResponse(TrainRouteResponse response) {
                try {
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    if (response.getResponse_code().equals("200")){
                        Log.v(TAG, "Success");
                        llDatacont.setVisibility(View.VISIBLE);

                        txtSun.setText(response.getTrain().getDays().get(0).getRuns());
                        txtMon.setText(response.getTrain().getDays().get(1).getRuns());
                        txtTue.setText(response.getTrain().getDays().get(2).getRuns());
                        txtWed.setText(response.getTrain().getDays().get(3).getRuns());
                        txtThu.setText(response.getTrain().getDays().get(4).getRuns());
                        txtFri.setText(response.getTrain().getDays().get(5).getRuns());
                        txtSat.setText(response.getTrain().getDays().get(6).getRuns());

                        mAdapter = new RouteAdapter(TrainRouteActivity.this,response.getRoute());
                        listRoute.setAdapter(mAdapter);
                        AppUtils.setListViewHeightBasedOnChildren(listRoute);
                    }else{
                        llDatacont.setVisibility(View.GONE);
                        listRoute.setVisibility(View.GONE);
                        AppUtils.showToastMessage(TrainRouteActivity.this,getString(R.string.empty_response));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    llDatacont.setVisibility(View.GONE);
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
                llDatacont.setVisibility(View.GONE);
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e(TAG, error.toString());
            }
        };
    }
}
