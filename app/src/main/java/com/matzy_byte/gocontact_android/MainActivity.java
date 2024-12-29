package com.matzy_byte.gocontact_android;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matzy_byte.gocontact_android.data.AppDatabase;
import com.matzy_byte.gocontact_android.data.Contact;
import com.matzy_byte.gocontact_android.data.ContactAdapter;
import com.matzy_byte.gocontact_android.data.NotificationReceiver;
import com.matzy_byte.gocontact_android.fragments.NewContactFragment;
import com.matzy_byte.gocontact_android.interfaces.DialogListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements DialogListener {
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    private RecyclerView contactListView;
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private NewContactFragment dialogAddContact;

    @SuppressLint("ShortAlarm")
    private void setNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll();
        }

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        appDatabase = AppDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        dialogAddContact =  new NewContactFragment();
        Button btnAddContact = findViewById(R.id.btn_add_contact);
        btnAddContact.setOnClickListener(v -> dialogAddContact.show(getSupportFragmentManager(), "addContactDialog"));

        contactListView = findViewById(R.id.contact_list);

        contactAdapter = new ContactAdapter(contactList);
        contactListView.setLayoutManager(new LinearLayoutManager(this));
        contactListView.setAdapter(contactAdapter);

        executorService.execute(() -> {
            contactList.clear();
            List<Contact> contacts = appDatabase.contactDAO().getAllContacts();
            contactList.addAll(contacts);

            setNotification();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Contact notification";
            String description = "Reminder to contact your people";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("contact_notification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDialogPositiveClick(Contact contact) {
        executorService.execute(() -> {
            appDatabase.contactDAO().insert(contact);
        });

        contactList.add(contact);
        contactAdapter.notifyItemInserted(contactList.size() - 1);
        dialogAddContact.dismiss();
    }

    @Override
    public void onDialogNegativeClick() {
    }
}
