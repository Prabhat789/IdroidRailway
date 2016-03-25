package com.pktworld.railway.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.pktworld.railway.R;
import com.pktworld.railway.util.Utils;

/**
 * Created by ubuntu1 on 25/3/16.
 */
public class TrainLiveStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_status);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText mTitle = (EditText) toolbar.findViewById(R.id.editToolbar);
        mTitle.setHint("Search Train Name/Number");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        for (int i = 0; i<=2;i++){
            Utils.showToastMessage(TrainLiveStatusActivity.this,getString(R.string.live_status_info));
        }

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
}
