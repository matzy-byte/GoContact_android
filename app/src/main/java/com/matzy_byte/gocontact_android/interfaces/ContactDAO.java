package com.matzy_byte.gocontact_android.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.matzy_byte.gocontact_android.data.Contact;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    void insert(Contact contact);
    @Query("SELECT * FROM contacts WHERE id = :id")
    Contact getContactById(int id);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();
}
