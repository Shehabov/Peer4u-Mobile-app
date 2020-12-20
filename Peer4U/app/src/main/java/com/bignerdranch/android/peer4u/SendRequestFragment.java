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
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SendRequestFragment extends DialogFragment {
    private EditText mMessage;
    public static final String EXTRA_MESSAGE = "com.bignerdranch.android.peer4u.messagetojoin";
    private static final String ARG_STUDENT = "studentId";
    private static final String ARG_GROUP = "studentId";
    private Student mStudent;
    private Group mGroup;
    private TextView mDialogDescription;

    public static SendRequestFragment newInstance(UUID groupId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP, groupId);
        SendRequestFragment fragment = new SendRequestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final UUID groupId = (UUID) getArguments().getSerializable(ARG_GROUP);

        mGroup = StudentLab.get(getActivity()).getGroup(groupId);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_sendmessage, null);
        
        mDialogDescription = (TextView) v.findViewById(R.id.dialog_description);
        mDialogDescription.setText(R.string.dialog_description_request);

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
                                //no notification to mStudent....
                                sendResult(Activity.RESULT_OK, mMessage.getText().toString());
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
    private void sendResult(int resultCode, String message){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, message);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
