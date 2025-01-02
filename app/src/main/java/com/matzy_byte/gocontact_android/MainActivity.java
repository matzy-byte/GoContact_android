package com.matzy_byte.gocontact_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.matzy_byte.gocontact_android.data.AppDatabase;
import com.matzy_byte.gocontact_android.data.Contact;
import com.matzy_byte.gocontact_android.data.ContactAdapter;
import com.matzy_byte.gocontact_android.data.NotificationWorker;
import com.matzy_byte.gocontact_android.fragments.NewContactFragment;
import com.matzy_byte.gocontact_android.interfaces.DialogListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements DialogListener {
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    private RecyclerView contactListView;
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private NewContactFragment dialogAddContact;

    private void setNotification() {
        PeriodicWorkRequest notificationWork = new PeriodicWorkRequest.Builder(NotificationWorker.class, 16, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("daily_notification", ExistingPeriodicWorkPolicy.KEEP, notificationWork);
        finishAffinity();
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
        });

        Button btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener((View v) -> setNotification());
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
