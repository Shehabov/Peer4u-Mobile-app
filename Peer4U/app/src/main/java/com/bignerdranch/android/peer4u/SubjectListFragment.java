package com.bignerdranch.android.peer4u;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class SubjectListFragment extends Fragment {
    private RecyclerView mSubjectListRecyclerView;
    private ImageButton mAddSubjectButton;
    private SubjectAdapter mAdapter;
    private static final String ARG_STUDENT_ID = "student";
    private Student mStudent;
    private File mPhotoFile;
    private TextView mStudentNameText;
    private TextView mStudentUniversityText;
    private TextView mEnrolledSubjectsText;
    private ImageView mPhotoView;
    private ImageView mNoSubjectsEnrolledImageView;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    public static SubjectListFragment newInstance(UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT_ID, studentId);
        SubjectListFragment fragment = new SubjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        if (studentId == null){
            studentId = StudentLab.get(getActivity()).getStudents().get(0).getId();
        }
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);


        mSubjectListRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        mSubjectListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mSubjectListRecyclerView.setHasFixedSize(true);

        mAddSubjectButton = (ImageButton) view.findViewById(R.id.add_subject_button);
        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mAddSubjectButton.setVisibility(View.VISIBLE);
        } else {
            mAddSubjectButton.setVisibility(View.GONE);
        }
        mAddSubjectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //start activity to allsubjectlistfragment
                Intent intent = AllSubjectListActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mNoSubjectsEnrolledImageView = (ImageView) view.findViewById(R.id.no_subjects_enrolled_imageview);
        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mNoSubjectsEnrolledImageView.setVisibility(View.VISIBLE);
        } else {
            mNoSubjectsEnrolledImageView.setVisibility(View.GONE);
        }

        mEnrolledSubjectsText = (TextView) view.findViewById(R.id.enrolled_subjects_title);
        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mEnrolledSubjectsText.setVisibility(View.GONE);
        } else {
            mEnrolledSubjectsText.setVisibility(View.VISIBLE);
        }

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
        inflater.inflate(R.menu.fragment_subject_list, menu);

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
            //enrol subject
            case R.id.new_subject:

                Intent intent = AllSubjectListActivity
                        .newIntent(getActivity(), mStudent.getId());
                startActivity(intent);
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
        StudentLab studentLab = StudentLab.get(getActivity());
        int subjectCount = studentLab.getSubjects(mStudent.getId()).size();
        String subtitle = getString(R.string.subtitle_format_subject, subjectCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        StudentLab studentLab = StudentLab.get(getActivity());
        List<Subject> subjects = studentLab.getSubjects(mStudent.getId());

        if (mAdapter == null){
            mAdapter = new SubjectAdapter(subjects);
            mSubjectListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSubjects(subjects);
            mAdapter.notifyDataSetChanged();
        }

        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mAddSubjectButton.setVisibility(View.VISIBLE);
        } else {
            mAddSubjectButton.setVisibility(View.GONE);
        }
        //Toast.makeText(getActivity(),subjects.get(0).getSubjectName() + ", " + subjects.get(1).getSubjectName(),Toast.LENGTH_LONG).show();
        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mNoSubjectsEnrolledImageView.setVisibility(View.VISIBLE);
        } else {
            mNoSubjectsEnrolledImageView.setVisibility(View.GONE);
        }
        if (StudentLab.get(getActivity()).getSubjects(mStudent.getId()).size() == 0){
            mEnrolledSubjectsText.setVisibility(View.GONE);
        } else {
            mEnrolledSubjectsText.setVisibility(View.VISIBLE);
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mSubjectNameTextView;
        private TextView mSubjectCodeTextView;
        private Subject mSubject;


        public SubjectHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_subject, parent, false));
            itemView.setOnClickListener(this);
            mSubjectNameTextView = (TextView) itemView.findViewById(R.id.subject_name);
            mSubjectCodeTextView = (TextView) itemView.findViewById(R.id.subject_code);

        }
        public void bind(Subject subject){
            mSubject = subject;
            mSubjectNameTextView.setText(mSubject.getSubjectName());
            mSubjectCodeTextView.setText(mSubject.getSubjectCode());
        }
        @Override
        public void onClick(View view){
            Intent intent = SubjectPagerActivity.newIntent(getActivity(), mSubject.getId(), mStudent.getId());
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

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder>{
        private List<Subject> mSubjects;

        public SubjectAdapter(List<Subject> subjects){
            mSubjects = subjects;
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SubjectHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(SubjectHolder holder, int position){
            Subject subject = mSubjects.get(position);
            holder.bind(subject);
        }
        @Override
        public int getItemCount(){
            return mSubjects.size();
        }

        public void setSubjects(List<Subject> subjects){
            mSubjects = subjects;
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
