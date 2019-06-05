package com.example.micheatlight.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.micheatlight.R;
import java.io.IOException;

public class NotificationSender extends ContextWrapper {
    private static final String TAG="NotificationSender";
    private static final int SLEEP=2000;
    private static final String ERROR_MESSAGE="ERROR";
    private Converter converter;
    private boolean errorMessages;


    public NotificationSender(Context context, Converter converter) {
        super(context);
        this.converter=converter;
    }

    public NotificationSender(Context context, Converter converter, boolean errorMessages) {
        super(context);
        this.converter=converter;
        this.errorMessages=errorMessages;
    }



    //Sendet eine Benachrichtigung, wenn zu Groß wird ein Fehler geloggt aber trotzdem gesendet
    public void sendSingleNotification(String head, String desctiption) {
        int temp=SLEEP/2;
        if(converter.islegal(desctiption)) {
            issueNotification(head, desctiption);
            SystemClock.sleep(temp);
            clearAllNotifications();
            SystemClock.sleep(temp);

        } else {
            Log.e(TAG, "sendSingleNotification: despription more than "+converter.getMaxZeichen());
            issueNotification(head, desctiption);
        }
    }

    private void clearAllNotifications() {
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    public void sendMultipleNotifications(String head, String[] description, int level) {
        level*=converter.getMaxBenachrichtigungen();
        String[] toSend=new String[converter.getMaxBenachrichtigungen()];
        for (int i = 0; i < converter.getMaxBenachrichtigungen(); i++) {
            Log.d(TAG, "sendMultipleNotifications: iritation "+i);
            if(i+level<description.length) {
                toSend[i]=description[i+level];
            } else break;
        }
        for (int i = 0; i < toSend.length; i++) {
            if(toSend[i]!=null && toSend[i]!="") {
                sendSingleNotification(head, toSend[i]);
            } else break;
        }
    }

    //Sendet eine Fehlermeldung ans MiBand
    public void errorMessage(String errorMessage) {
        if(!this.errorMessages) return;
        if(converter.islegal(errorMessage)) {
            this.sendSingleNotification(ERROR_MESSAGE,errorMessage);
        } else {
            try {
                converter.convertToLegal(errorMessage);
                this.sendSingleNotification(ERROR_MESSAGE,errorMessage);
            } catch (IOException e) {
                Log.e(TAG, "errorMessage: java IO error");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    private void issueNotification(String title, String description) {

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHEAT_1", "MiCheating", NotificationManager.IMPORTANCE_DEFAULT);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHEAT_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
                .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
                .setContentTitle(title)
                .setContentText(description)
                .setNumber(3); // this shows a number in the notification dots

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }
}
