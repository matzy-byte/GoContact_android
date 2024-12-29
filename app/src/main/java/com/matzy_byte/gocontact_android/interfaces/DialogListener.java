package com.matzy_byte.gocontact_android.interfaces;

import com.matzy_byte.gocontact_android.data.Contact;

public interface DialogListener {
    void onDialogPositiveClick(Contact contact);
    void onDialogNegativeClick();
}
