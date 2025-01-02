package com.matzy_byte.gocontact_android.data;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.matzy_byte.gocontact_android.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationWorker extends Worker {
    private final Context context;
    private final String[] messages = {
            "hi",
            "Test text",
            "hihi"
    };

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        showNotification();
        return Result.success();
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId="GoContact_reminder";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "GoContact notification", NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int amountContacts = appDatabase.contactDAO().getAllContacts().size();
            Contact contact = appDatabase.contactDAO().getContactById((int) (amountContacts * Math.random()) + 1);
            if (contact != null) {
                String contactName = contact.getFirstName() + " " + contact.getLastName();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setContentTitle("Contact: " + contactName)
                        .setContentText(getMessage())
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setAutoCancel(true);


                    if (notificationManager != null) {
                        notificationManager.notify(1, builder.build());
                    }
                }
            }
        });
    }

    private String getMessage() {
        return messages[(int) ((messages.length - 1) * Math.random())];
    }
}
