package com.bignerdranch.android.peer4u;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NewSubjectFragment extends DialogFragment {

    private EditText mSubjectName;
    private EditText mSubjectCode;
    private Subject mSubject;
    private static final String ARG_SUBJECT_ID = "subject";

    public static NewSubjectFragment newInstance(){
        Bundle args = new Bundle();

        NewSubjectFragment fragment = new NewSubjectFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public static NewSubjectFragment newInstance(UUID subjectId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUBJECT_ID, subjectId);
        NewSubjectFragment fragment = new NewSubjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        UUID subjectId = (UUID) getArguments().getSerializable(ARG_SUBJECT_ID);
        if (subjectId == null){
            mSubject = new Subject();
            SubjectLab.get(getActivity()).addSubject(mSubject);
        } else {
            mSubject = SubjectLab.get(getActivity()).getSubject(subjectId);
        }

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_newsubject, null);

        mSubjectCode = (EditText) v.findViewById(R.id.subject_code);
        mSubjectCode.setText(mSubject.getSubjectCode());

        mSubjectCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mSubject.setSubjectCode(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });

        mSubjectName = (EditText) v.findViewById(R.id.subject_name);
        mSubjectName.setText(mSubject.getSubjectName());

        mSubjectName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mSubject.setSubjectName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.new_subject)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubjectLab.get(getActivity()).updateSubject(mSubject);
                                //sendResult(Activity.RESULT_OK, mProject);
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
/**
    private void sendResult(int resultCode, Project project){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PROJECT, (Serializable) project);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }**/
}
