package com.bignerdranch.android.peer4u;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ProjectFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "project_id";
    private static final String ARG_SUBJECT_ID = "subject_id";
    private static final String ARG_STUDENT_ID = "student_id";
    private static final int REQUEST_JOINGROUP = 1;
    private static final int REQUEST_CREATEGROUP = 2;
    private static final String DIALOG_LEAVEGROUP = "DialogProject";
    private static final int REQUEST_LEAVEGROUP = 0;
    private Project mProject;
    private TextView mStudentNameField;
    private TextView mStudentUniversityField;
    private ImageView mPhotoView;
    private TextView mProjectNameField;
    private TextView mDetailsField;
    private TextView mGroupMembersField;
    private Button mCreateButton;
    private Button mJoinButton;
    private Student mStudent;
    private File mPhotoFile;
    private Group mGroup;
    private Subject mSubject;
    private Button mInviteButton;
    private Button mLeaveButton;

    public static ProjectFragment newInstance(UUID projectId, UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PROJECT_ID, projectId);
        args.putSerializable(ARG_STUDENT_ID, studentId);
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID projectId = (UUID) getArguments().getSerializable(ARG_PROJECT_ID);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mProject = SubjectLab.get(getActivity()).getProject(projectId);
        mSubject = SubjectLab.get(getActivity()).getSubject(mProject.getSubjectUUID());
        mGroup = StudentLab.get(getActivity()).getGroupByStudentProject(studentId, projectId);

    }

    @Override
    public void onPause(){
        super.onPause();

        SubjectLab.get(getActivity()).updateProject(mProject);
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_project, container, false);

        mPhotoView = (ImageView) v.findViewById(R.id.student_profilepic);
        updatePhotoView();

        mStudentNameField = (TextView) v.findViewById(R.id.student_name);
        mStudentNameField.setText(mStudent.getFirstName());

        mStudentUniversityField = (TextView) v.findViewById(R.id.student_university);
        mStudentUniversityField.setText(mStudent.getUniveristy());

        mProjectNameField = (TextView) v.findViewById(R.id.project_name);
        mProjectNameField.setText(mProject.getAssignmentName());


        mDetailsField = (TextView) v.findViewById(R.id.project_details);
        mDetailsField.setText(mProject.getAssignmentDetails());



        mCreateButton = (Button) v.findViewById(R.id.create_group_button);
        if(groupExist()){
            mCreateButton.setVisibility(View.GONE);
        } else {
            mCreateButton.setVisibility(View.VISIBLE);
        }
        mCreateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start activity to create group
                Intent intent = NewGroupActivity.newIntent(getActivity(), mStudent.getId(), mProject.getId(), mSubject.getId());
                //startActivity(intent);
                startActivity(intent);

            }
        });

        mJoinButton = (Button) v.findViewById(R.id.join_group_button);
        if(groupExist()){
            mJoinButton.setVisibility(View.GONE);
        } else {
            mJoinButton.setVisibility(View.VISIBLE);
        }
        mJoinButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start activity to grouplistfragment
                Intent intent = GroupListActivity.newIntent(getActivity(), mStudent.getId(), mProject.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mInviteButton = (Button) v.findViewById(R.id.invite_member_button);
        if (groupExist() && isLeader()){
            mInviteButton.setVisibility(View.VISIBLE);
        } else {
            mInviteButton.setVisibility(View.GONE);
        }
        mInviteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start activity to studentlistfragment
                Intent intent = StudentListActivity.newIntent(getActivity(), mStudent.getId(), mProject.getId(), mGroup.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });
        mLeaveButton = (Button) v.findViewById(R.id.leave_group_button);
        if (groupExist() && !isLeader()){
            mLeaveButton.setVisibility(View.VISIBLE);
        } else {
            mLeaveButton.setVisibility(View.GONE);
        }


        mLeaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start activity to leavegroupfragment
                FragmentManager manager = getFragmentManager();
                LeaveGroupFragment dialog = LeaveGroupFragment.newInstance(mProject.getId(), mStudent.getId());
                dialog.setTargetFragment(ProjectFragment.this, REQUEST_LEAVEGROUP);
                dialog.show(manager, DIALOG_LEAVEGROUP);
            }
        });
        mGroupMembersField = (TextView) v.findViewById(R.id.group_members);
        updateMembers();

        return v;
    } //end of onCreateView
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_LEAVEGROUP){
            updateUI();
        } else if (requestCode == REQUEST_CREATEGROUP){

        }
    }

    private void updatePhotoView(){
        try{
            mPhotoFile = null;

            mPhotoFile = new File(getActivity().getFilesDir(), mStudent.getPhotoFilename());;

        } finally {
            if (mPhotoFile == null || !mPhotoFile.exists()){
                mPhotoView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity());
                mPhotoView.setImageBitmap(bitmap);
            }
        }

    }
    private void updateMembers(){
        StudentLab studentLab = StudentLab.get(getActivity());
        if (mGroup == null){
            mGroupMembersField.setText(R.string.no_group);
        } else {
            if (mGroup.getMemberList() == null){
                mGroupMembersField.setText(getString(R.string.no_group_members, mStudent.getFirstName()));
            }
            else {
                mGroupMembersField.setText(getString(R.string.group_members_names, studentLab.getStudent(mGroup.getLeaderId()).getFirstName(), mGroup.getMemberNames()));
            }
        }
    }
    private void updateUI(){
        if(groupExist()){
            mCreateButton.setVisibility(View.GONE);
            mJoinButton.setVisibility(View.GONE);
            if (!isLeader()){
                mLeaveButton.setVisibility(View.VISIBLE);
                mInviteButton.setVisibility(View.GONE);
            } else {
                mLeaveButton.setVisibility(View.GONE);
                mInviteButton.setVisibility(View.VISIBLE);
            }
        } else {
            mCreateButton.setVisibility(View.VISIBLE);
            mJoinButton.setVisibility(View.VISIBLE);
            mLeaveButton.setVisibility(View.GONE);
        }
        updateMembers();
    }

    private boolean groupExist(){
        if (mGroup == null){
            return false;
        } else {
            return true;
        }
    }
    private boolean isLeader(){
        if (mGroup.getLeaderId().equals(mStudent.getId())){
            return true;
        } else {
            return false;
        }
    }

}
