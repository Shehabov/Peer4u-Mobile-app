package com.bignerdranch.android.peer4u;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

public class SubjectFragment extends Fragment {
    private RecyclerView mSubjectRecyclerView;
    private ImageButton mAddProjectButton;
    private SubjectAdapter mAdapter;
    private static final String ARG_SUBJECT_ID = "subject";
    private Subject mSubject;
    private static final String DIALOG_PROJECT = "DialogProject";
    private static final int REQUEST_PROJECT = 0;
    private TextView mSubjectNameText;
    private ImageView mPhotoView;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    public static SubjectFragment newInstance(UUID subjectId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUBJECT_ID, subjectId);
        SubjectFragment fragment = new SubjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID subjectId = (UUID) getArguments().getSerializable(ARG_SUBJECT_ID);
        mSubject = SubjectLab.get(getActivity()).getSubject(subjectId);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);


        mSubjectRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        mSubjectRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mSubjectRecyclerView.setHasFixedSize(true);

        mPhotoView = (ImageView) view.findViewById(R.id.profile_photo);
        mPhotoView.setVisibility(View.GONE);

        mSubjectNameText = (TextView) view.findViewById(R.id.subject_name);
        mSubjectNameText.setText(mSubject.getSubjectName());

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

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
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_project_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_project:
                Project project = new Project(mSubject.getId());
                SubjectLab.get(getActivity()).addProject(project);
                FragmentManager manager = getFragmentManager();
                NewProjectFragment dialog = NewProjectFragment.newInstance(project.getId());
                dialog.setTargetFragment(SubjectFragment.this, REQUEST_PROJECT);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateUI();
                    }
                });
                dialog.show(manager, DIALOG_PROJECT);
                return true;
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
        SubjectLab subjectLab = SubjectLab.get(getActivity());
        int projectCount = subjectLab.getProjectsFromSubject(mSubject.getId()).size();
        String subtitle = getString(R.string.subtitle_format, projectCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }




    private void updateUI(){
        SubjectLab subjectLab = SubjectLab.get(getActivity());
        List<Project> projects = subjectLab.getProjectsFromSubject(mSubject.getId());

        if (mAdapter == null){
            mAdapter = new SubjectAdapter(projects);
            mSubjectRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setProjects(projects);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mProjectNameTextView;
        private TextView mProjectDetailsTextView;
        private Project mProject;


        public SubjectHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_subject, parent, false));
            itemView.setOnClickListener(this);
            mProjectNameTextView = (TextView) itemView.findViewById(R.id.subject_name);
            mProjectDetailsTextView = (TextView) itemView.findViewById(R.id.subject_code);

        }
        public void bind(Project project){
            mProject = project;
            mProjectNameTextView.setText(mProject.getAssignmentName());
            mProjectDetailsTextView.setText(mProject.getAssignmentDetails());
        }
        @Override
        public void onClick(View view){

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_PROJECT){
            // Handle result
            //Project project = (Project)data.getSerializableExtra(SubjectFragment.DIALOG_PROJECT);
            updateUI();
        }
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder>{
        private List<Project> mProjects;

        public SubjectAdapter(List<Project> projects){
            mProjects = projects;
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SubjectHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(SubjectHolder holder, int position){
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
    } //end of class SubjectAdapter


} //end of class SubjectListFragment extends Fragment
