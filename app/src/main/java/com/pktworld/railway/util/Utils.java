package com.pktworld.railway.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pktworld.railway.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        toast.setDuration(Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, WindowManager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /*public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }*/


    public static int get_count_of_days(String Created_date_String,String Expire_date_String) {
        Log.e(TAG,"CurrentDate : "+Created_date_String+" ,Enddate : "+Expire_date_String);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date Created_convertedDate=null,Expire_CovertedDate=null,todayWithZeroTime=null;
        try
        {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

            Date today = new Date();

            todayWithZeroTime =dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }


        int c_year=0,c_month=0,c_day=0;

        if(Created_convertedDate.after(todayWithZeroTime))
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        }
        else
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }


            /*Calendar today_cal = Calendar.getInstance();
            int today_year = today_cal.get(Calendar.YEAR);
            int today = today_cal.get(Calendar.MONTH);
            int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
            */





        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);


        return ((int) dayCount);
    }

}
