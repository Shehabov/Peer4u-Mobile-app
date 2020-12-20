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
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.UUID;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LeaveGroupFragment extends DialogFragment {

    public static final String EXTRA_GROUP = "com.bignerdranch.android.peer4u.group";

    private static final String ARG_PROJECTID = "project_id";
    private static final String ARG_STUDENTID = "student_id";
    private TextView mGroupName;
    private TextView mProjectName;
    private TextView mMembersName;
    private TextView mLeaderName;
    private Group mGroup;
    private Student mStudent;

    public static LeaveGroupFragment newInstance(UUID projectId, UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECTID, projectId);
        args.putSerializable(ARG_STUDENTID, studentId);
        LeaveGroupFragment fragment = new LeaveGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final UUID projectId = (UUID) getArguments().getSerializable(ARG_PROJECTID);
        final UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENTID);
        mGroup = StudentLab.get(getActivity()).getGroupByStudentProject(studentId, projectId);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_leavegroup, null);


        /*mGroupName = (TextView) v.findViewById(R.id.group_name_text);
        mGroupName.setText(mGroup.getGroupName());

        mProjectName = (TextView) v.findViewById(R.id.project_name_text);
        mProjectName.setText(SubjectLab.get(getActivity()).getProject(mGroup.getProjectId()).getAssignmentName());

        mLeaderName = (TextView) v.findViewById(R.id.leader_name_text);
        mLeaderName.setText(StudentLab.get(getActivity()).getStudent(mGroup.getLeaderId()).getFirstName());

        mMembersName = (TextView) v.findViewById(R.id.members_name_text);
        mMembersName.setText(mGroup.getMemberNames());**/

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.leave_group_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                StudentLab.get(getActivity()).leaveGroup(mGroup.getId(), mStudent.getId());
                                Toast.makeText(getActivity(),"Left group successfully!",Toast.LENGTH_LONG).show();
                                sendResult(getActivity().RESULT_OK);
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return;
        }

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode,null);
    }
}
