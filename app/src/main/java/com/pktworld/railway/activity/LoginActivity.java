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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin, btnSignUp;
    private EditText editEmail,editPassword;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("Login");
        getSupportActionBar().setTitle("");

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            if (validate()){
                if (Utils.isConnected(this)){
                    login(LoginActivity.this,"/SetCustomerLogin",editEmail.getText().toString().trim(),editPassword.getText().toString().trim());
                }
            }
        }else if(v == btnSignUp){
            Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(i);

        }


    }


    private boolean validate(){
        if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Email");
            return false;
        }else if (editPassword.getText().toString().trim().length() == 0 || editPassword.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Password");
            return  false;
        }else{
            return true;
        }
    }


    private void login(Context mContext,String url,String email, String password){

        mProgressDialog = ProgressDialog.show(mContext, "",
                getResources().getString(R.string.processing), true);
        mProgressDialog.show();
        String REQUEST_URL = ApplicationConstants.BASEURL+url;
        Map<String,String> params = new HashMap<String, String>();
        params.put("lsEmailId", email);
        params.put("lsPassword", password);

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
                        Intent i = new Intent(LoginActivity.this,SlideMenuActivity.class);
                        startActivity(i);
                        finish();
                    }else{

                        Utils.showToastMessage(LoginActivity.this,getString(R.string.invalid_user));
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
