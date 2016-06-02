package com.idroidms.railway.parseutils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idroidms.railway.R;
import com.idroidms.railway.util.ApplicationConstants;

import java.util.List;

/**
 * Created by Prabhat on 10/05/16.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.DataObjectHolder> {

    private static final String TAG = GroupListAdapter.class.getSimpleName();
    private List<Group> mDataset;
    private Context mContext;


    public GroupListAdapter(Context context, List<Group> myDataset) {
        mDataset = myDataset;
        mContext = context;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_group, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.txtGroupName.setText(mDataset.get(position).getGroupName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mDataset.get(position).getObjectId(),mDataset.get(position).getGroupName(),
                        mDataset.get(position).getGroupName());
            }
        });



    }



    public void addItem(Group dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void sendMessage(String fId, String usName,String gName) {
        Log.d(TAG, "Broadcasting message");
        Intent intent = new Intent(ApplicationConstants.GROUP_LIST_ADAPTER);
        intent.putExtra(ApplicationConstants.FLAG1, fId);
        intent.putExtra(ApplicationConstants.FLAG2, usName);
        intent.putExtra(ApplicationConstants.FLAG3,gName);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView txtGroupName;
        CardView view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txtGroupName = (TextView) itemView.findViewById(R.id.txtGroupName);
            view = (CardView) itemView.findViewById(R.id.card_view);

        }

    }
}
