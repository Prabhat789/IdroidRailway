package com.idroidms.railway.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.idroidms.railway.R;
import com.idroidms.railway.model.PnrResponse;
import com.idroidms.railway.parseutils.Group;
import com.idroidms.railway.parseutils.OneToOneChatActivity;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.GsonRequestResponseHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class ChatRoomFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = ChatRoomFragment.class.getSimpleName();
    private EditText editTrainNumber, editPnrNumber;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Group> group;
   // private GroupListAdapter mAdapter;
    private Button btnSearchGroup,btnCreateGroup;
    private RequestQueue mRequestQueue;
    private TextView txtTrainName;
    private LinearLayout layoutRecyclerList,layoutNoGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        /*LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(ApplicationConstants.GROUP_LIST_ADAPTER));*/

       /* mRecyclerView = (RecyclerView)rootView.findViewById(R.id.listGroup);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        editTrainNumber = (EditText)rootView.findViewById(R.id.editTrainNumber);
        btnSearchGroup = (Button)rootView.findViewById(R.id.btnSearchGroup);
        layoutRecyclerList = (LinearLayout)rootView.findViewById(R.id.layoutRecyclerList);
        layoutNoGroup = (LinearLayout)rootView.findViewById(R.id.layoutNoGroup);
        txtTrainName = (TextView)rootView.findViewById(R.id.txtTrainNumber);
        editPnrNumber = (EditText)rootView.findViewById(R.id.editPnrNumber);
        btnCreateGroup = (Button)rootView.findViewById(R.id.btnCreateGroup);
        layoutNoGroup.setVisibility(View.GONE);
        layoutRecyclerList.setVisibility(View.GONE);
        btnSearchGroup.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);
        group = new ArrayList<Group>();*/

        return rootView;

    }

   /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userId = intent.getStringExtra(ApplicationConstants.FLAG1);
            String userName = intent.getStringExtra(ApplicationConstants.FLAG2);
            String gName = intent.getStringExtra(ApplicationConstants.FLAG3);
            Log.d(TAG, "Got message: " + userId);
            Intent i = new Intent(getActivity(), OneToOneChatActivity.class);
            i.putExtra(ApplicationConstants.FLAG1, userId);
            i.putExtra(ApplicationConstants.FLAG2, userName);
            i.putExtra(ApplicationConstants.FLAG3,gName);
            startActivity(i);
        }
    };*/


   public void SearchGroup(final String q){
       mProgressDialog = ProgressDialog.show(getActivity(), "",
               getResources().getString(R.string.processing), true);
       mProgressDialog.show();
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
        query.whereContains("groupName",q);
        query.setLimit(1);
        query.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> messages, ParseException e) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                if (e == null) {
                    group.clear();
                    Collections.reverse(messages);
                    group.addAll(messages);
                    /*mAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();*/

                    if (messages.size() == 0){
                        /*txtTrainName.setText(q);
                        layoutNoGroup.setVisibility(View.VISIBLE);
                        layoutRecyclerList.setVisibility(View.GONE);*/
                        createGroup(q,editTrainNumber.getText().toString());
                    }else {
                        /*layoutRecyclerList.setVisibility(View.VISIBLE);
                        layoutNoGroup.setVisibility(View.GONE);*/
                        /*String userId = intent.getStringExtra(ApplicationConstants.FLAG1);
                        String userName = intent.getStringExtra(ApplicationConstants.FLAG2);
                        String gName = intent.getStringExtra(ApplicationConstants.FLAG3);*/
                        Intent i = new Intent(getActivity(), OneToOneChatActivity.class);
                        i.putExtra(ApplicationConstants.FLAG1, group.get(0).getObjectId());
                        i.putExtra(ApplicationConstants.FLAG2, group.get(0).getGroupName());
                        i.putExtra(ApplicationConstants.FLAG3,group.get(0).getGroupName());
                        startActivity(i);
                    }

                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnSearchGroup){
            if (editTrainNumber.getText().toString().length() !=0 || !editTrainNumber.getText().toString().isEmpty()){
                //getTrainName(getActivity());
                getPnrStatus(getActivity());
            }else {
                AppUtils.showToastMessage(getActivity(),"Please enter your PNR Number");
            }
        }else if (v == btnCreateGroup){
            if (editPnrNumber.getText().toString().length() !=0 || !editPnrNumber.getText().toString().isEmpty()){
                createGroup(txtTrainName.getText().toString(),editPnrNumber.getText().toString().trim());
            }else {
                AppUtils.showToastMessage(getActivity(),"Please enter your PNR Number");
            }
        }
    }

   /* private void getTrainName(Context mContext) {
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"name_number/train/"+editTrainNumber.getText().toString().trim()+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

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
                                JSONObject train = response1.getJSONObject("train");

                                if (mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                               // SearchGroup(train.getString("number")+" "+train.getString("name"));
                                mAdapter = new GroupListAdapter(getActivity(), group);
                                mRecyclerView.setAdapter(mAdapter);

                            }else{
                                if (mProgressDialog.isShowing()){
                                    mProgressDialog.dismiss();
                                }
                                AppUtils.showToastMessage(getActivity(),getString(R.string.empty_response));
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
    }*/

    void createGroup(final String trainNumber, String pnrNumber){
        mProgressDialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        Group message = new Group();
        message.setGroupName(trainNumber);
        message.setPnr(pnrNumber);
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                if (e == null) {
                    SearchGroup(trainNumber);
                   /* mAdapter = new GroupListAdapter(getActivity(), group);
                    mRecyclerView.setAdapter(mAdapter);*/
                }

            }
        });
    }

    @Override
    public void onDestroy() {
       // LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void getPnrStatus(Context mContext){
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        //"http://api.railwayapi.com/pnr_status/pnr/1234567890/apikey/myapikey/"
        String REQUEST_URL = ApplicationConstants.RAILWAY_API_URL+"pnr_status/pnr/"+
                editTrainNumber.getText().toString().trim()+"/apikey/"+ApplicationConstants.RAILWAY_API_KEY;

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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
                        String trainName = response.getTrain_name();
                        String txtTrainNumber = response.getTrain_num();
                        String chartPrepared = response.getChart_prepared();
                        if (chartPrepared.equalsIgnoreCase("Y")){
                            SearchGroup(txtTrainNumber+" "+trainName);
                        }else {
                            AppUtils.showToastMessage(getActivity(),"Chart Not Prepared at yet");
                        }
                        /*txtPnr.setText("PNR : "+response.getPnr());
                        txtDoj.setText("Date Of Journey : "+response.getDoj());
                        txtChartPrep.setText("Chart Prepared : "+response.getChart_prepared());
                        txtTotalPass.setText("Total Passengers : "+response.getTotal_passengers());
                        txtTrainStartDate.setText("Start Date : "+response.getTrain_start_date().getDay()+"/"+
                                response.getTrain_start_date().getMonth()+"/"+response.getTrain_start_date().getYear());
                        txtFromStation.setText("From Station : "+response.getFrom_station().getName()+" ["+response.getFrom_station().getCode()+"]");
                        txtToStation.setText("To Station : "+response.getTo_station().getName()+" ["+response.getTo_station().getCode()+"]");

                        mAdapers = new PassangerAdapter(PnrStatusActivity.this,response.getPassengers());
                        mList.setAdapter(mAdapers);*/


                    }else {
                        AppUtils.showToastMessage(getActivity(),response.getError());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Log.e(TAG, "TryCatch");
                    AppUtils.showToastMessage(getActivity(),"Unable to get PNR status");
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
                } Log.e(TAG, "Error");
                AppUtils.showToastMessage(getActivity(),"Unable to get PNR status");
            }
        };
    }
}
