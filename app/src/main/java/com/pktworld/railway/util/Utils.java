package com.pktworld.railway.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pktworld.railway.R;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    private Context mContext;

    public Utils(Context context){
        this.mContext = context;
    }

    public static boolean isConnected(Activity context) {
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            showToastMessage(context,context.getString(R.string.no_internet));
        return false;
    }

    public static void showToastMessage(Activity mContext, String msg){
        LayoutInflater li = mContext.getLayoutInflater();
        View layout = li.inflate(R.layout.custom_toast, null);
        TextView txtMsg = (TextView)layout.findViewById(R.id.txtToast);
        txtMsg.setText(msg);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }


}
