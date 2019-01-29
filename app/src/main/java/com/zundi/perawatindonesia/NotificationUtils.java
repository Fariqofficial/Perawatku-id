package com.zundi.perawatindonesia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.zundi.inc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zindx on 09/09/2017.
 */

public class NotificationUtils {

    private Context mContext;
    private Uri mSound;

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
        this.mSound = Uri.parse("android.resource://com.zundi.visio.Visio.visio.inc/" + R.raw.ding);
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        PendingIntent penInt = null;

        if (TextUtils.isEmpty(message))
            return;

        if(intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            penInt =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        showSmallNotification(mBuilder, title, message, timeStamp, penInt);
    }


    private void showSmallNotification(NotificationCompat.Builder builder, String title,
                                       String message, String timeStamp,
                                       PendingIntent penIntent) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(penIntent)
                .setSound(mSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(App.NOTIFICATION_ID, notification);
    }

    public void playSound() {
        try {
            Ringtone r = RingtoneManager.getRingtone(mContext, mSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
