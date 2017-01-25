package com.harunuyar.studentassistant.Notifier;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.harunuyar.studentassistant.MainActivity;
import com.harunuyar.studentassistant.R;

/**
 * Created by Harun on 25.01.2017.
 */

public class BildirimNotifier extends Notifier {

    private Context context;
    private int notificationID;

    public BildirimNotifier(boolean aDayAgo, boolean aWeekAgo, Context context) {
        super(aDayAgo, aWeekAgo);
        this.context = context;
        notificationID = 0;
    }

    @Override
    public void notifyUser(Object info) throws Exception {
        if (!(info instanceof Bildirim))
            throw new Exception("Nesne, bildirim tipinde değil.");
        else{
            Bildirim bildirim = (Bildirim) info;
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            if (bildirim.getTexts().length == 0){
                bildirim.setTexts(new String[]{"Bilinmeyen bildirim."});
            }

                mBuilder.setContentTitle("Student Assistant");
            mBuilder.setContentText(bildirim.getTexts()[0]);
            mBuilder.setSmallIcon(R.drawable.today);

            mBuilder.setColor(Color.BLACK);

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Student Assistant");
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
