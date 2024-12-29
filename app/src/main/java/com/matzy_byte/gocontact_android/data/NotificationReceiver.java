package com.matzy_byte.gocontact_android.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.matzy_byte.gocontact_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationReceiver extends BroadcastReceiver {
    private final String[] messages = {
            "hi",
            "Test text",
            "hihi"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int amountContacts = appDatabase.contactDAO().getAllContacts().size();
            Contact contact = appDatabase.contactDAO().getContactById((int) (amountContacts * Math.random()) + 1);
            if (contact != null) {
                String contactName = contact.getFirstName() + " " + contact.getLastName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Notification notification = new Notification.Builder(context, "contact_notification")
                            .setContentTitle("Contact: " + contactName)
                            .setContentText(getMessage())
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setAutoCancel(true)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, notification);
                }
            }
        });
    }

    private String getMessage() {
        return messages[(int) ((messages.length - 1) * Math.random())];
    }
}
