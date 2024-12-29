package com.matzy_byte.gocontact_android.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matzy_byte.gocontact_android.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{
    private List<Contact> contactList;
    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.age.setText(Integer.toString(contact.getAge()));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView firstName;
        TextView lastName;
        TextView age;

        public ContactViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.txt_first_name);
            lastName = itemView.findViewById(R.id.txt_last_name);
            age = itemView.findViewById(R.id.txt_age);
        }
    }
}
