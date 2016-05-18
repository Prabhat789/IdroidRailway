package com.pktworld.railway.parseutils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.pktworld.railway.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NotificationReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getSimpleName();
    private NotificationManager mNotificationManager;
    public static int NOTIFICATION_ID = 1;


    /*@Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
           Log.v(TAG, "Notification Received");
		Log.e(TAG,intent.getData().toString());
        if (OneToOneChatActivity.isAppAlive == 1) {
            OneToOneChatActivity.receiveMessage();
        }
       // super.onReceive(context, intent);

    }*/

    @Override
    protected void onPushReceive(Context context, Intent intent) {

        if (isAppIsInBackground(context)){
            super.onPushReceive(context, intent);
        }else {
            if (OneToOneChatActivity.isAppAlive == 1) {
                OneToOneChatActivity.receiveMessage();
            }
        }


        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.e(TAG, "Push received: " + json);
           // parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

           // sendNotification(message,context);


        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    private void sendNotification(String msg, Context context) {

        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        /*Intent intent = new Intent(context,NotificationActivity.class);
        intent.putExtra("TYPE", "ONE");
        contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);*/

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg).setSound(soundUri).setVibrate(new long[]{100}).setLights(Color.CYAN, 2000,2000);;

        mBuilder.setAutoCancel(true);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }


    /**
     * Method checks if the app is in background or not
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
