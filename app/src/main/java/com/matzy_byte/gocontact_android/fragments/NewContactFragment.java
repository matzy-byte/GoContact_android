package com.matzy_byte.gocontact_android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.matzy_byte.gocontact_android.R;
import com.matzy_byte.gocontact_android.data.Contact;
import com.matzy_byte.gocontact_android.interfaces.DialogListener;

public class NewContactFragment extends DialogFragment {
    private DialogListener listener;

    private Contact createContact(String firstName, String lastName, int age) {
        return new Contact(firstName, lastName, age);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.new_contact, null);

        TextView firstName = dialogView.findViewById(R.id.edit_first_name);
        TextView lastName = dialogView.findViewById(R.id.edit_last_name);
        TextView age = dialogView.findViewById(R.id.edit_age);

        builder.setView(dialogView);
        Button btnOkay = dialogView.findViewById(R.id.btn_okay);
        btnOkay.setOnClickListener(v -> listener.onDialogPositiveClick(createContact(firstName.getText().toString(), lastName.getText().toString(), Integer.parseInt(age.getText().toString()))));
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> this.dismiss());

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context; // Ensure parent implements listener
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }
}
