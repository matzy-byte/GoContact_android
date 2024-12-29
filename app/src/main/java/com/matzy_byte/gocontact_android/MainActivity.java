package com.matzy_byte.gocontact_android;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matzy_byte.gocontact_android.data.Contact;
import com.matzy_byte.gocontact_android.data.ContactAdapter;
import com.matzy_byte.gocontact_android.fragments.NewContactFragment;
import com.matzy_byte.gocontact_android.interfaces.DialogListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogListener {
    private RecyclerView contactListView;
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private NewContactFragment dialogAddContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dialogAddContact =  new NewContactFragment();
        Button btnAddContact = findViewById(R.id.btn_add_contact);
        btnAddContact.setOnClickListener(v -> dialogAddContact.show(getSupportFragmentManager(), "addContactDialog"));

        contactListView = findViewById(R.id.contact_list);

        contactAdapter = new ContactAdapter(contactList);
        contactListView.setLayoutManager(new LinearLayoutManager(this));
        contactListView.setAdapter(contactAdapter);
    }

    @Override
    public void onDialogPositiveClick(Contact contact) {
        contactList.add(contact);
        contactAdapter.notifyItemInserted(contactList.size() - 1);
        dialogAddContact.dismiss();
    }

    @Override
    public void onDialogNegativeClick() {

    }
}
