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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pktworld.railway.R;
import com.pktworld.railway.model.ServiceResponse;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.GsonRequestResponseHelper;
import com.pktworld.railway.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ubuntu1 on 29/5/16.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG= ForgotPasswordActivity.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;
    private EditText editEmail;
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        txtTitle.setText("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        btnSend = (Button)findViewById(R.id.btnNext);
        editEmail = (EditText)findViewById(R.id.editEmail);

        btnSend.setOnClickListener(this);
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
        if (v ==btnSend){
            if (validate()){
                forgotPassword(this,"/SetCustomerForgotPassword",editEmail.getText().toString().trim());
            }
        }
    }

    private boolean validate() {
        if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().isEmpty()) {
            Utils.showToastMessage(this, "Please enter Email");
            return false;
        } else {
            return true;
        }

    }


    private void forgotPassword(Context mContext,String url,String email){

        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.BASEURL+url;
        Map<String,String> params = new HashMap<String, String>();
        params.put("lsEmailId", email);

        Log.e(TAG, REQUEST_URL + " " + params.toString());

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<ServiceResponse> myReq = new GsonRequestResponseHelper<ServiceResponse>(
                Request.Method.POST,
                REQUEST_URL,
                ServiceResponse.class,
                params,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                ApplicationConstants.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(myReq);


    }

    private Response.Listener<ServiceResponse> createMyReqSuccessListener() {
        return new Response.Listener<ServiceResponse>() {
            @Override
            public void onResponse(ServiceResponse response) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                try {

                    if (response.getResponse().equals("Success")){

                    }else{
                        Utils.showToastMessage(ForgotPasswordActivity.this,getString(R.string.invalid_user));
                    }

                } catch (Exception e) {
                    e.printStackTrace();

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
