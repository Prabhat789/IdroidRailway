package com.pktworld.railway.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
 * Created by Prabhat on 05/05/16.
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Button btnSignUp;
    private EditText editEmail,editPassword,editFirstName,editLastName,editMobile,editConPass;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("SignUp");
        getSupportActionBar().setTitle("");

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editFirstName = (EditText)findViewById(R.id.editFirstName);
        editLastName = (EditText)findViewById(R.id.editLastName);
        editMobile = (EditText)findViewById(R.id.editMobile);
        editConPass = (EditText)findViewById(R.id.editConPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignUp){
            if (validate()){
                if (Utils.isConnected(this)){
                    signUp(SignUpActivity.this, "/InsertCustomerRegistration");
                }
            }
        }
    }

    private void signUp(Context mContext, String url) {

        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.BASEURL+url;
        Map<String,String> params = new HashMap<String, String>();
        params.put("lsEmailId", editEmail.getText().toString().trim());
        params.put("lsPassword", editPassword.getText().toString().trim());
        params.put("lsFirstName",editFirstName.getText().toString().trim());
        params.put("lsLastName",editLastName.getText().toString().trim());
        params.put("lsCellphone",editMobile.getText().toString().trim());
        params.put("lsParentPhone","");
        params.put("lsParentPhone2","");
        params.put("lsAddress", "");
        params.put("lsState", "");
        params.put("lsCity", "");
        params.put("lsCountry", "");
        params.put("lsZipcode", "");
        params.put("lsLatitude", "");
        params.put("lsLongitude", "");


        Log.e(TAG, REQUEST_URL);

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
                        Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();

                    }else{

                        Utils.showToastMessage(SignUpActivity.this,getString(R.string.unable_to_process_request));
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

    private boolean validate(){
        if (editFirstName.getText().toString().trim().length() == 0 || editFirstName.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter First Name");
            return false;
        }else if (editLastName.getText().toString().trim().length() == 0 || editLastName.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Last Name");
            return  false;
        }else if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Email");
            return  false;
        }else if (editMobile.getText().toString().trim().length() == 0 || editMobile.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Mobile Number");
            return  false;
        }else if (editPassword.getText().toString().trim().length() == 0 || editPassword.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Password");
            return  false;
        }else if (editConPass.getText().toString().trim().length() == 0 || editConPass.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please re-enter Password");
            return  false;
        }else if (!editPassword.getText().toString().equals(editConPass.getText().toString())){
            Utils.showToastMessage(this,"Password should be same");
            return  false;
        }else{
            return true;
        }
    }


}
