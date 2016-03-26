package com.pktworld.railway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pktworld.railway.R;

/**
 * Created by ubuntu1 on 25/3/16.
 */
public class TrainInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TrainInfoActivity.class.getSimpleName();
    private Button btnLiveStatus, btnPnrStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("Train Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        btnLiveStatus = (Button)findViewById(R.id.btnLiveStatus);
        btnPnrStatus = (Button)findViewById(R.id.btnPnrStatus);
        btnLiveStatus.setOnClickListener(this);
        btnPnrStatus.setOnClickListener(this);


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
        if (v == btnLiveStatus){
            Intent i = new Intent(TrainInfoActivity.this,TrainLiveStatusActivity.class);
            startActivity(i);
        }else if (v == btnPnrStatus){
            Intent i = new Intent(TrainInfoActivity.this,PnrStatusActivity.class);
            startActivity(i);
        }
    }
}
