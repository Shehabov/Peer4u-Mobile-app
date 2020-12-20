package com.bignerdranch.android.peer4u;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupListFragment extends Fragment {
    private RecyclerView mGroupListRecyclerView;
    private GroupAdapter mAdapter;
    private static final String ARG_STUDENT_ID = "student";
    private static final String ARG_PROJECT_ID = "projectId";
    private static final String ARG_SUBJECT_ID = "subjectId";
    private static final String DIALOG_MESSAGE = "DialogMessage";
    private static final int REQUEST_MESSAGE = 0;
    private Student mStudent;
    private Project mProject;
    private File mPhotoFile;
    private ImageView mPhotoView;
    private TextView mStudentNameText;
    private TextView mStudentUniversityText;
    private TextView mNoGroupText;
    private List<Group> mGroups;

    public static GroupListFragment newInstance(UUID studentId, UUID projectId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT_ID, studentId);
        args.putSerializable(ARG_PROJECT_ID, projectId);
        GroupListFragment fragment = new GroupListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        UUID projectId = (UUID) getArguments().getSerializable(ARG_PROJECT_ID);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mProject = SubjectLab.get(getActivity()).getProject(projectId);
        mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        mGroupListRecyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        mGroupListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mStudentNameText = (TextView) view.findViewById(R.id.student_name);
        mStudentNameText.setText(mStudent.getFirstName());

        mStudentUniversityText = (TextView) view.findViewById(R.id.student_university);
        mStudentUniversityText.setText(mStudent.getUniveristy());

        mPhotoView = (ImageView) view.findViewById(R.id.profile_photo);
        updatePhotoView();

        StudentLab studentLab = StudentLab.get(getActivity());
        mGroups = studentLab.getGroupsByProject(mProject.getId());

        mNoGroupText = (TextView) view.findViewById(R.id.no_group_text);

        if (mGroups == null){
            mNoGroupText.setVisibility(View.VISIBLE);
        } else {
            mNoGroupText.setVisibility(View.GONE);
            updateUI();
        }

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (!(mGroups == null)){
            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }




    private void updateUI(){
        StudentLab studentLab = StudentLab.get(getActivity());
        List<Group> groups = studentLab.getGroupsByProject(mProject.getId());

        if (mAdapter == null){
            mAdapter = new GroupAdapter(groups);
            mGroupListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setGroups(groups);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mGroupNameTextView;
        private TextView mMemberNamesTextView;
        private Group mGroup;
        private Button mRequestButton;

        public GroupHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_group, parent, false));
            itemView.setOnClickListener(this);
            mGroupNameTextView = (TextView) itemView.findViewById(R.id.group_name);
            mMemberNamesTextView = (TextView) itemView.findViewById(R.id.group_member_names);
            mRequestButton = (Button) itemView.findViewById(R.id.request_button);

        }
        public void bind(Group group){
            mGroup = group;
            mGroupNameTextView.setText(mGroup.getGroupName());
            mMemberNamesTextView.setText(mGroup.getMemberNames());
            mRequestButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    FragmentManager manager = getFragmentManager();
                    SendRequestFragment dialog = SendRequestFragment.newInstance(mGroup.getId());
                    dialog.setTargetFragment(GroupListFragment.this, REQUEST_MESSAGE);
                    dialog.show(manager, DIALOG_MESSAGE);
                }
            });
        }
        @Override
        public void onClick(View view){
            //start dialog

            //!!not yet code function for sending and receiving notification/messages
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_MESSAGE){
            // Handle result

        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder>{
        private List<Group> mGroups;

        public GroupAdapter(List<Group>groups){
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new GroupHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(GroupHolder holder, int position){
            Group group = mGroups.get(position);
            holder.bind(group);
        }
        @Override
        public int getItemCount(){
            return mGroups.size();
        }

        public void setGroups(List<Group> groups){
            mGroups = groups;
        }
    } //end of class SubjectAdapter

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
} //end of class SubjectListFragment extends Fragment
