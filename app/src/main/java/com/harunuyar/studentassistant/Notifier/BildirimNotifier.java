package com.harunuyar.studentassistant.Notifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import com.harunuyar.studentassistant.Constants;
import com.harunuyar.studentassistant.MainActivity;
import com.harunuyar.studentassistant.R;

/**
 * Created by Harun on 25.01.2017.
 */

public class BildirimNotifier extends Notifier {

    private Context context;
    private int notificationID;

    public BildirimNotifier(@NonNull Context context) {
        this.context = context;
        notificationID = 0;
    }

    @Override
    public void notifyUser(@NonNull Object info) throws Exception {
        if (!(info instanceof Bildirim))
            throw new Exception("Nesne, bildirim tipinde değil.");
        else{
            Bildirim bildirim = (Bildirim) info;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            if (bildirim.getTexts().length == 0){
                bildirim.setTexts(new String[]{"Bilinmeyen bildirim."});
            }

            mBuilder.setContentTitle("Sınav Asistanı");
            mBuilder.setContentText(bildirim.getTexts()[0]);
            mBuilder.setColor(Constants.MAINCOLOR);
            mBuilder.setSmallIcon(R.drawable.smallicon);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource( context.getResources(), R.mipmap.ic_launcher));
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Sınav Asistanı");
            for (String s : bildirim.getTexts()) {
                inboxStyle.addLine(s);
            }
            mBuilder.setStyle(inboxStyle);

            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(notificationID, mBuilder.build());
            notificationID++;
        }
    }
}
