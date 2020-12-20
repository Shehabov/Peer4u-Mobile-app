package com.bignerdranch.android.peer4u;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllSubjectListFragment extends Fragment {
    private RecyclerView mSubjectListRecyclerView;
    private SubjectAdapter mAdapter;
    private static final String ARG_STUDENT_ID = "student";
    private Student mStudent;
    private File mPhotoFile;
    private static final String DIALOG_PASSWORD = "DialogPassword";
    private static final int REQUEST_PASSWORD = 0;
    private String mPassword;
    private ImageButton mAddSubjectButton;
    private TextView mStudentName;
    private TextView mStudentUniversity;
    private ImageView mPhotoView;

    public static AllSubjectListFragment newInstance(UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT_ID, studentId);
        AllSubjectListFragment fragment = new AllSubjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_allsubjects_list, container, false);

        mSubjectListRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        mSubjectListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mPhotoView = (ImageView) view.findViewById(R.id.profile_photo);

        mStudentName = (TextView) view.findViewById(R.id.student_name);
        mStudentName.setText(mStudent.getFirstName());

        mStudentUniversity = (TextView) view.findViewById(R.id.student_university);
        mStudentUniversity.setText(mStudent.getUniveristy());

        updatePhotoView();
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




    private void updateUI(){
        StudentLab studentLab = StudentLab.get(getActivity());
        List<Subject> subjects = SubjectLab.get(getActivity()).getSubjects();

        if (mAdapter == null){
            mAdapter = new SubjectAdapter(subjects);
            mSubjectListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSubjects(subjects);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PASSWORD){
            // Handle result
            String password = data.getStringExtra(SendMessageFragment.EXTRA_MESSAGE);
            String correctpassword = data.getStringExtra(SendMessageFragment.EXTRA_SUBJECTCODE);

            if (correctpassword.equals(password)){
                StudentLab studentLab = StudentLab.get(getActivity());
                SubjectLab subjectLab = SubjectLab.get(getActivity());
                studentLab.addSubject(mStudent, subjectLab.getSubject(password));
                //StudentLab.get(getActivity()).updateStudent(mStudent);
                Toast.makeText(getActivity(),"Enrol success!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(),"Password is incorrect! Please check with your lecturer!", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mSubjectNameTextView;
        private TextView mSubjectCodeTextView;
        private Subject mSubject;
        private Button mEnrolButton;


        public SubjectHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_allsubject, parent, false));
            itemView.setOnClickListener(this);
            mSubjectNameTextView = (TextView) itemView.findViewById(R.id.subject_name);
            mSubjectCodeTextView = (TextView) itemView.findViewById(R.id.subject_code);
            mEnrolButton = (Button) itemView.findViewById(R.id.enrol_button);


        }
        public void bind(Subject subject){
            mSubject = subject;
            mSubjectNameTextView.setText(mSubject.getSubjectName());
            mSubjectCodeTextView.setText(mSubject.getSubjectCode());
            mEnrolButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    FragmentManager manager = getFragmentManager();
                    SendMessageFragment dialog = SendMessageFragment.newInstance(mSubject.getSubjectCode());
                    dialog.setTargetFragment(AllSubjectListFragment.this, REQUEST_PASSWORD);
                    dialog.show(manager, DIALOG_PASSWORD);
                }
            });
        }
        @Override
        public void onClick(View view){
            //start dialog
            /**
            FragmentManager manager = getFragmentManager();
            SendMessageFragment dialog = SendMessageFragment.newInstance();
            dialog.setTargetFragment(AllSubjectListFragment.this, REQUEST_PASSWORD);
            dialog.show(manager, DIALOG_PASSWORD);**/
            //!!not yet code function for sending and receiving notification/messages

            Intent intent = SubjectActivity.newIntent(getActivity(), mSubject.getId());
            //startActivity(intent);
            startActivity(intent);
        }
    }



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
