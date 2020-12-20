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

public class SendInviteFragment extends DialogFragment {
    private EditText mMessage;
    public static final String EXTRA_MESSAGE = "com.bignerdranch.android.peer4u.messagetojoin";
    public static final String EXTRA_INVITE = "com.bignerdranch.android.peer4u.invite";
    private static final String ARG_STUDENT = "studentId";
    private Student mStudent;
    private TextView mDialogDescription;

    public static SendInviteFragment newInstance(UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT, studentId);
        SendInviteFragment fragment = new SendInviteFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_sendmessage, null);

        mDialogDescription = (TextView) v.findViewById(R.id.dialog_description);
        mDialogDescription.setText(R.string.dialog_description_invite);

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
                                sendResult(Activity.RESULT_OK, mMessage.getText().toString(), mStudent.getId());
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
    private void sendResult(int resultCode, String message, UUID studentId){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_INVITE, studentId);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
