package com.bignerdranch.android.peer4u;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class ProjectListFragment extends Fragment {
    private RecyclerView mProjectListRecyclerView;
    private ProjectAdapter mAdapter;
    private static final String ARG_SUBJECT_ID = "subject";
    private static final String ARG_STUDENT_ID = "student";
    private Subject mSubject;
    private Student mStudent;
    private TextView mStudentNameText;
    private TextView mStudentUniversityText;
    private TextView mSubjectNameText;
    private ImageView mPhotoView;
    private File mPhotoFile;
    private boolean mSubtitleVisible;

    public static ProjectListFragment newInstance(UUID subjectId, UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUBJECT_ID, subjectId);
        args.putSerializable(ARG_STUDENT_ID, studentId);
        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID subjectId = (UUID) getArguments().getSerializable(ARG_SUBJECT_ID);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        mSubject = SubjectLab.get(getActivity()).getSubject(subjectId);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);

        mProjectListRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        mProjectListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mStudentNameText = (TextView) view.findViewById(R.id.student_name);
        mStudentNameText.setText(mStudent.getFirstName());
        mStudentNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                startActivity(intent);
            }
        });

        mStudentUniversityText = (TextView) view.findViewById(R.id.student_university);
        mStudentUniversityText.setText(mStudent.getUniveristy());
        mStudentUniversityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                startActivity(intent);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.profile_photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                startActivity(intent);
            }
        });
        updatePhotoView();

        mSubjectNameText = (TextView) view.findViewById(R.id.subject_name);
        mSubjectNameText.setText(getString(R.string.subject_code_name_text, mSubject.getSubjectCode(), mSubject.getSubjectName()));

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            /**case R.id.new_project:
                Intent intent = NewGroupActivity
                        .newIntent(getActivity(), mStudent.getId());
                startActivity(intent);
                return true;**/
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        StudentLab studentLab = StudentLab.get(getActivity());
        int subjectCount = studentLab.getSubjects(mStudent.getId()).size();
        String subtitle = getString(R.string.subtitle_format, subjectCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        List<Project> projects = SubjectLab.get(getActivity()).getProjectsFromSubject(mSubject.getId());

        if (mAdapter == null){
            mAdapter = new ProjectAdapter(projects);
            mProjectListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setProjects(projects);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDetailsTextView;
        private Project mProject;


        public ProjectHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_project, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.project_name);
            mDetailsTextView = (TextView) itemView.findViewById(R.id.project_details);

        }
        public void bind(Project project){
            mProject = project;
            mTitleTextView.setText(mProject.getAssignmentName());
            mDetailsTextView.setText(mProject.getAssignmentDetails());

        }
        @Override
        public void onClick(View view){
            Intent intent = ProjectPagerActivity.newIntent(getActivity(), mProject.getId(), mStudent.getId());
            //startActivity(intent);
            startActivity(intent);
        }
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CRIME){
            // Handle result
            mCrimeId = data.getIntExtra(ProjectListActivity.EXTRA_EPISODE_ID, 0);
        }
    }*/

    private class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder>{
        private List<Project> mProjects;

        public ProjectAdapter(List<Project> projects){
            mProjects = projects;
        }

        @Override
        public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ProjectHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(ProjectHolder holder, int position){
            Project project = mProjects.get(position);
            holder.bind(project);
        }
        @Override
        public int getItemCount(){
            return mProjects.size();
        }

        public void setProjects(List<Project> projects){
            mProjects = projects;
        }
    } //end of class ProjectAdapter

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
} //end of class ProjectListFragment extends Fragment
