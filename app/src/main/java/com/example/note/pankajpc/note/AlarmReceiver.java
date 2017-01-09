package com.example.note.pankajpc.note;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Pankaj PC on 01-01-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String notificationTitle = intent.getStringExtra("title");
        String notificationDetail = intent.getStringExtra("description");
        String notificationTimeStamp = intent.getStringExtra("timestamp");
        int notificationpriority = intent.getIntExtra("priority",0);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(notificationTitle)
                        .setSound(uri)
                        .setAutoCancel(true)
                        .setContentText(notificationDetail);
        Intent resultIntent = new Intent(context, ShowNote.class);
        resultIntent.putExtra("title",notificationTitle);
        resultIntent.putExtra("description",notificationDetail);
        resultIntent.putExtra("timestamp",notificationTimeStamp);
        resultIntent.putExtra("priority",notificationpriority);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId,mBuilder.build());
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
}
