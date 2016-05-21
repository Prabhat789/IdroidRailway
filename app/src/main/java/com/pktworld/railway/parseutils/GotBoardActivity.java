package com.pktworld.railway.parseutils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.pktworld.railway.R;
import com.pktworld.railway.fragments.ChatRoomFragment;
import com.pktworld.railway.model.PnrResponse;
import com.pktworld.railway.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Prabhat on 21/05/16.
 */
public class GotBoardActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ChatRoomFragment.class.getSimpleName();
    private EditText editTrainNumber, editPnrNumber;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Group> group;
    private Button btnSearchGroup,btnCreateGroup;
    private RequestQueue mRequestQueue;
    private TextView txtTrainName;
    private LinearLayout layoutRecyclerList,layoutNoGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_got_board);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView)findViewById(R.id.txtToolbar);
        txtTitle.setText("Got Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        mRecyclerView = (RecyclerView)findViewById(R.id.listGroup);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        editTrainNumber = (EditText)findViewById(R.id.editTrainNumber);
        btnSearchGroup = (Button)findViewById(R.id.btnSearchGroup);
        layoutRecyclerList = (LinearLayout)findViewById(R.id.layoutRecyclerList);
        layoutNoGroup = (LinearLayout)findViewById(R.id.layoutNoGroup);
        txtTrainName = (TextView)findViewById(R.id.txtTrainNumber);
        editPnrNumber = (EditText)findViewById(R.id.editPnrNumber);
        btnCreateGroup = (Button)findViewById(R.id.btnCreateGroup);
        layoutNoGroup.setVisibility(View.GONE);
        layoutRecyclerList.setVisibility(View.GONE);
        btnSearchGroup.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);
        group = new ArrayList<Group>();
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

    public void SearchGroup(final String q){
        mProgressDialog = ProgressDialog.show(GotBoardActivity.this, "",
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
                    if (messages.size() == 0){
                        createGroup(q,editTrainNumber.getText().toString());
                    }else {

                        Intent i = new Intent(GotBoardActivity.this, OneToOneChatActivity.class);
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
                getPnrStatus(GotBoardActivity.this);
            }else {
                com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,"Please enter your PNR Number");
            }
        }else if (v == btnCreateGroup){
            if (editPnrNumber.getText().toString().length() !=0 || !editPnrNumber.getText().toString().isEmpty()){
                createGroup(txtTrainName.getText().toString(),editPnrNumber.getText().toString().trim());
            }else {
                com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,"Please enter your PNR Number");
            }
        }
    }


    void createGroup(final String trainNumber, String pnrNumber){
        mProgressDialog = ProgressDialog.show(GotBoardActivity.this, "",
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

                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getPnrStatus(Context mContext){
        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
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
                            com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,"Chart Not Prepared at yet");
                        }

                    }else {
                        com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,response.getError());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Log.e(TAG, "TryCatch");
                    com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,"Unable to get PNR status");
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
                com.pktworld.railway.util.Utils.showToastMessage(GotBoardActivity.this,"Unable to get PNR status");
            }
        };
    }
}
