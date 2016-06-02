package com.idroidms.railway.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.idroidms.railway.R;
import com.idroidms.railway.model.ServiceResponse;
import com.idroidms.railway.parseutils.ParseSetUp;
import com.idroidms.railway.util.AppUtils;
import com.idroidms.railway.util.ApplicationConstants;
import com.idroidms.railway.util.GsonRequestResponseHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prabhat on 05/05/16.
 */
public class SignUpActivity extends Activity implements View.OnClickListener{
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private Button btnSignUp;
    private EditText editEmail,editPassword,editFirstName,editLastName,editConPass;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");*/

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editFirstName = (EditText)findViewById(R.id.editFirstName);
        editLastName = (EditText)findViewById(R.id.editLastName);
       // editMobile = (EditText)findViewById(R.id.editMobile);
        editConPass = (EditText)findViewById(R.id.editConPassword);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
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
        if (v == btnSignUp){
            if (validate()){
                if (AppUtils.isConnected(this)){
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
        params.put("lsFirstName",editFirstName.getText().toString().trim());
        params.put("lsLastName",editLastName.getText().toString().trim());
        params.put("lsEmailId", editEmail.getText().toString().trim());
        params.put("lsPassword", editPassword.getText().toString().trim());
       /* params.put("lsCellphone","1212545212");
        params.put("lsParentPhone","2565232562");
        params.put("lsParentPhone2","2565232562");
       // params.put("lsDob","07/10/1988");
        params.put("lsAddress", "Unknown");
        params.put("lsState", "Unknown");
        params.put("lsCity", "Unknown");
        params.put("lsCountry", "Unknown");
        params.put("lsZipcode", "Unknown");
        params.put("lsLatitude", "Unknown");
        params.put("lsLongitude", "Unknown");*/


        Log.e(TAG, REQUEST_URL+" "+params.toString());

        mRequestQueue = Volley.newRequestQueue(mContext);

        // Request with API parameters
        GsonRequestResponseHelper<ServiceResponse> myReq = new GsonRequestResponseHelper<ServiceResponse>(
                Request.Method.POST,
                REQUEST_URL,
                ServiceResponse.class,
                params,
                createMyReqSuccessListener(),
                createMyReqErrorListener());

      /*  StringRequest myReq1 = new StringRequest(Request.Method.POST, REQUEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if (mProgressDialog.isShowing()){
                            mProgressDialog.dismiss();
                        }

                        Log.e(TAG,response.toString());
                        try {


                            JSONObject object = new JSONObject(response);
                            String response1 = object.getString("response");
                            if (response1.equals("Success")){
                                ParseSetUp setup = new ParseSetUp(SignUpActivity.this);
                                setup.register(editFirstName.getText().toString().trim(),editLastName.getText().toString().trim(),
                                        editEmail.getText().toString().trim(),editPassword.getText().toString().trim(),"1252565232");

                            }else{
                                AppUtils.showToastMessage(SignUpActivity.this,getString(R.string.unable_to_process_request));
                            }



                        } catch (Exception e) {
                            e.printStackTrace();

                            Log.e(TAG, "TryCatch");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                } Log.e(TAG, error.toString());
                AppUtils.showToastMessage(SignUpActivity.this, getString(R.string.unable_to_process_request));
            }
        }){

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params1 = new HashMap<String, String>();
            //params.put("Content-Type", "application/json");
            params1.put("Content-Type","application/x-www-form-urlencoded");
            return params1;
        }
    };*/
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
                Log.e(TAG,response.toString());
                try {

                    if (response.getResponse().equals("Success")){
                        ParseSetUp setup = new ParseSetUp(SignUpActivity.this);
                        setup.register(editFirstName.getText().toString().trim(),editLastName.getText().toString().trim(),
                                editEmail.getText().toString().trim(),editPassword.getText().toString().trim(),"1252565232");
                       /* Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                        AppUtils.showToastMessage(SignUpActivity.this, response.getMessage());*/
                    }else{
                        AppUtils.showToastMessage(SignUpActivity.this,getString(R.string.unable_to_process_request));
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
                } Log.e(TAG,"On Error: "+ error.toString());
                AppUtils.showToastMessage(SignUpActivity.this, getString(R.string.unable_to_process_request));
            }
        };

    }

    private boolean validate(){
        if (editFirstName.getText().toString().trim().length() == 0 || editFirstName.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please enter First Name");
            return false;
        }else if (editLastName.getText().toString().trim().length() == 0 || editLastName.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please enter Last Name");
            return  false;
        }else if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please enter Email");
            return  false;
        }/*else if (editMobile.getText().toString().trim().length() == 0 || editMobile.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please enter Mobile Number");
            return  false;
        }*/else if (editPassword.getText().toString().trim().length() == 0 || editPassword.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please enter Password");
            return  false;
        }else if (editConPass.getText().toString().trim().length() == 0 || editConPass.getText().toString().isEmpty()){
            AppUtils.showToastMessage(this,"Please re-enter Password");
            return  false;
        }else if (!editPassword.getText().toString().equals(editConPass.getText().toString())){
            AppUtils.showToastMessage(this,"Password should be same");
            return  false;
        }else{
            return true;
        }
    }



}
