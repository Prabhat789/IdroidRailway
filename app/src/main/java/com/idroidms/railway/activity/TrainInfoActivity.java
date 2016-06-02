package com.idroidms.railway.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.idroidms.railway.R;


/**
 * Created by ubuntu1 on 25/3/16.
 */
public class TrainInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TrainInfoActivity.class.getSimpleName();
    private Button btnLiveStatus, btnPnrStatus,btnSeatAvailability,
                    btnFareEnqury,btnTrainRoute,btnTrainbwStation,
                    btnCancelledTrain,btnTrainArrivalsAtStation;

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
        btnSeatAvailability = (Button)findViewById(R.id.btnSeatAvailability);
        btnFareEnqury = (Button)findViewById(R.id.btnFareEnqury);
        btnTrainRoute = (Button)findViewById(R.id.btnTrainRoute);
        btnTrainbwStation = (Button)findViewById(R.id.btnTrainbwStation);
        btnCancelledTrain = (Button)findViewById(R.id.btnCancelTrain);
        btnTrainArrivalsAtStation = (Button)findViewById(R.id.btnTrainArrivesAtStation);
        btnLiveStatus.setOnClickListener(this);
        btnPnrStatus.setOnClickListener(this);
        btnSeatAvailability.setOnClickListener(this);
        btnFareEnqury.setOnClickListener(this);
        btnTrainRoute.setOnClickListener(this);
        btnTrainbwStation.setOnClickListener(this);
        btnCancelledTrain.setOnClickListener(this);
        btnTrainArrivalsAtStation.setOnClickListener(this);

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
            Intent i = new Intent(TrainInfoActivity.this, TrainLiveStatusActivity.class);
            startActivity(i);
        }else if (v == btnPnrStatus){
            Intent i = new Intent(TrainInfoActivity.this,PnrStatusActivity.class);
            startActivity(i);
        }else if (v == btnSeatAvailability){
            Intent i = new Intent(TrainInfoActivity.this,SeatAvilabilityActivity.class);
            startActivity(i);
        }else if (v == btnFareEnqury){
            Intent i = new Intent(TrainInfoActivity.this,FareEnquryActivity.class);
            startActivity(i);
        }else if (v == btnTrainRoute){
            Intent i = new Intent(TrainInfoActivity.this, TrainRouteActivity.class);
            startActivity(i);
        } else if (v == btnTrainbwStation){
            Intent i = new Intent(TrainInfoActivity.this,TrainBetweenStationActivity.class);
            startActivity(i);
        }else if (v == btnCancelledTrain){
            Intent i = new Intent(TrainInfoActivity.this,CancelTrainActivity.class);
            startActivity(i);
        }else if (v == btnTrainArrivalsAtStation){
            Intent i = new Intent(TrainInfoActivity.this,TrainArrivalsAtStationActivity.class);
            startActivity(i);
        }
    }
}
