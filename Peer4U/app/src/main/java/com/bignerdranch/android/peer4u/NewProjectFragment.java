package com.bignerdranch.android.peer4u;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NewProjectFragment extends DialogFragment {

    public static final String EXTRA_PROJECT = "com.bignerdranch.android.peer4u.project";
    private static final String ARG_PROJECTID = "project_id";
    private EditText mAssignmentName;
    private EditText mAssignmentDetails;
    private Project mProject;

    public static NewProjectFragment newInstance(UUID projectId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECTID, projectId);

        NewProjectFragment fragment = new NewProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final UUID projectId = (UUID) getArguments().getSerializable(ARG_PROJECTID);

        mProject = SubjectLab.get(getActivity()).getProject(projectId);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_newproject, null);

        mAssignmentDetails = (EditText) v.findViewById(R.id.project_details);

        mAssignmentDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mProject.setAssignmentDetails(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });

        mAssignmentName = (EditText) v.findViewById(R.id.project_name);

        mAssignmentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mProject.setAssignmentName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.new_project_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SubjectLab.get(getActivity()).updateProject(mProject);
                                SubjectLab.get(getActivity()).getSubject(mProject.getSubjectUUID()).updateProject(mProject);
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
    private void sendResult(int resultCode, Project project){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PROJECT, (Serializable) project);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
