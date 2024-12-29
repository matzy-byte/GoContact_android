package com.matzy_byte.gocontact_android;

import android.os.AsyncTask;
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
import com.matzy_byte.gocontact_android.fragments.NewContactFragment;
import com.matzy_byte.gocontact_android.interfaces.DialogListener;

import java.util.ArrayList;
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
