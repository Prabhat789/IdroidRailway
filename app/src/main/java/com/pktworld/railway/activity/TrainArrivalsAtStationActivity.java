package com.pktworld.railway.activity;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pktworld.railway.R;
import com.pktworld.railway.adapter.TrainArrivalAtStationAdapter;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Prabhat on 16/04/16.
 */
public class TrainArrivalsAtStationActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = TrainArrivalsAtStationActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;
    private EditText editHours,editStationCode;
    private Button btnSearch;
    private ListView listArrivesTrain;
    private TrainArrivalAtStationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_arrival_station);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("Train Arrivals at Station");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        editHours = (EditText)findViewById(R.id.editHours);
        editStationCode = (EditText)findViewById(R.id.editStationCode);
        editStationCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        listArrivesTrain = (ListView)findViewById(R.id.listTrains);
        btnSearch = (Button)findViewById(R.id.btnGo);
        btnSearch.setOnClickListener(this);
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
                if (Utils.isConnected(TrainArrivalsAtStationActivity.this)){
                    getArrivesTrains(TrainArrivalsAtStationActivity.this);
                }
            }

        }
    }

    private boolean validate(){
        if (editHours.getText().toString().trim().length() == 0 || editHours.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(TrainArrivalsAtStationActivity.this,"Please enter Hours");
            return false;
        }else if (editStationCode.getText().toString().trim().length() == 0 || editStationCode.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(TrainArrivalsAtStationActivity.this,"Please enter Station Code");
            return  false;
        }else{
            return  true;
        }
    }

    private void getArrivesTrains(Context mContext) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"arrivals/"+"/station/"+
                editStationCode.getText().toString()+"/hours/"+editHours.getText().toString()+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

        Log.e(TAG, REQUEST_URL);

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        StringRequest stringRequest = new StringRequest(Request.Method.GET, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            Log.e(TAG,response.toString());
                            JSONObject response1 = new JSONObject(response);
                            if (response1.getString("response_code").equals("200")){
                                JSONArray train = response1.getJSONArray("train");

                                mAdapter = new TrainArrivalAtStationAdapter(TrainArrivalsAtStationActivity.this,train);
                                listArrivesTrain.setAdapter(mAdapter);
                                Utils.setListViewHeightBasedOnChildren(listArrivesTrain);
                                if (mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                            }else{
                                if (mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                Utils.showToastMessage(TrainArrivalsAtStationActivity.this,getString(R.string.empty_response));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            if (mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                            Log.e(TAG, "TryCatch");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e(TAG, error.toString());
            }
        });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

}
