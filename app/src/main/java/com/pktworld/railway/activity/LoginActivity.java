package com.pktworld.railway.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.pktworld.railway.R;
import com.pktworld.railway.model.ServiceResponse;
import com.pktworld.railway.parseutils.ParseSetUp;
import com.pktworld.railway.util.ApplicationConstants;
import com.pktworld.railway.util.GsonRequestResponseHelper;
import com.pktworld.railway.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prabhat on 05/05/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin, btnSignUp,btnForgotPassword;
    private EditText editEmail,editPassword;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private CheckBox saveLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (isLycenceExpire()){
            finish();
        }

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnForgotPassword = (Button)findViewById(R.id.btnForgotPassword);

        saveLoginCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences(ApplicationConstants.LOGIN_PREFERENCE, MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean(ApplicationConstants.SAVE_LOGIN, false);

        if (saveLogin == true) {
            editEmail.setText(loginPreferences.getString(ApplicationConstants.USERNAME, ""));
            editPassword.setText(loginPreferences.getString(ApplicationConstants.PASSWORD, ""));
            saveLoginCheckBox.setChecked(true);
        }

        setupUI(findViewById(R.id.LoginContainer));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
    }
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    editEmail.clearFocus();
                    editPassword.clearFocus();
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            if (validate()){
                if (Utils.isConnected(this)){
                    if (saveLoginCheckBox.isChecked()) {

                        loginPrefsEditor.putBoolean(ApplicationConstants.SAVE_LOGIN, true);
                        loginPrefsEditor.putString(ApplicationConstants.USERNAME, editEmail.getText().toString());
                        loginPrefsEditor.putString(ApplicationConstants.PASSWORD, editPassword.getText().toString());
                        loginPrefsEditor.commit();

                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                    login(LoginActivity.this,"/SetCustomerLogin",editEmail.getText().toString().trim(),editPassword.getText().toString().trim());
                }
            }
        }else if(v == btnSignUp){
            Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(i);

        }else if (v == btnForgotPassword){
            Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
            startActivity(i);
        }


    }


    private boolean validate(){
        if (editEmail.getText().toString().trim().length() == 0 || editEmail.getText().toString().isEmpty()){
            Utils.showToastMessage(this,"Please enter Email");
            return false;
        }else if (editPassword.getText().toString().trim().length() == 0 || editPassword.getText().toString().isEmpty()){
            Utils.showToastMessage(this, "Please enter Password");
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
                        /*Intent i = new Intent(LoginActivity.this,SlideMenuActivity.class);
                        startActivity(i);
                        finish();*/
                        ParseSetUp setup = new ParseSetUp(LoginActivity.this);
                        setup.login(editEmail.getText().toString().trim(),editPassword.getText().toString().trim());
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


    private boolean isLycenceExpire(){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        int dayCount = Utils.get_count_of_days(currentDate, ApplicationConstants.EXPIRED_DATE);
        if (dayCount <= 0){
            Utils.showToastMessage(LoginActivity.this,"Licence Expired !");
            return true;
        }else {
            return false;
        }
    }



}
