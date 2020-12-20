package com.bignerdranch.android.peer4u;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SendMessageFragment extends DialogFragment {
    private EditText mMessage;
    public static final String EXTRA_MESSAGE = "com.bignerdranch.android.peer4u.messagetojoin";
    public static final String EXTRA_SUBJECTCODE = "com.bignerdranch.android.peer4u.enrolpassword";
    private static final String ARG_CODE = "subjectcode";

    public static SendMessageFragment newInstance(){
        Bundle args = new Bundle();

        SendMessageFragment fragment = new SendMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SendMessageFragment newInstance(String subjectcode){
        Bundle args = new Bundle();
        args.putString(ARG_CODE, subjectcode);
        SendMessageFragment fragment = new SendMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final String subjectcode = (String) getArguments().getString(ARG_CODE);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_sendmessage, null);

        mMessage = (EditText) v.findViewById(R.id.message);
        mMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.send_message)
                .setPositiveButton(R.string.send,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sendResult(Activity.RESULT_OK, mMessage.getText().toString(), subjectcode);
                            }
                        })
                .create();
    }
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
    private void sendResult(int resultCode, String message, String subjectcode){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_SUBJECTCODE, subjectcode);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
