package com.bignerdranch.android.peer4u;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class StudentListFragment extends Fragment {
    private RecyclerView mStudentListRecyclerView;
    private StudentAdapter mAdapter;
    private static final String ARG_STUDENT_ID = "student";
    private static final String ARG_PROJECT_ID = "project";
    private static final String ARG_GROUP_ID = "group";
    private Student mStudent;
    private Project mProject;
    private Group mGroup;
    private File mPhotoFile;
    private static final String DIALOG_INVITE = "DialogInvite";
    private static final int REQUEST_INVITE = 0;
    private String mPassword;
    private ImageButton mAddSubjectButton;
    private TextView mStudentName;
    private TextView mStudentUniversity;
    private ImageView mPhotoView;

    public static StudentListFragment newInstance(UUID studentId, UUID projectId, UUID groupId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT_ID, studentId);
        args.putSerializable(ARG_PROJECT_ID, projectId);
        args.putSerializable(ARG_GROUP_ID, groupId);
        StudentListFragment fragment = new StudentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        UUID projectId = (UUID) getArguments().getSerializable(ARG_PROJECT_ID);
        UUID groupId = (UUID) getArguments().getSerializable(ARG_GROUP_ID);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mProject = SubjectLab.get(getActivity()).getProject(projectId);
        mGroup = StudentLab.get(getActivity()).getGroup(groupId);
        mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        mStudentListRecyclerView = (RecyclerView) view.findViewById(R.id.student_recycler_view);
        mStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        if (studentListExist()){
            StudentLab studentLab = StudentLab.get(getActivity());
            List<Student> students = studentLab.getStudentsWithoutGroupByProject(mProject.getId());

            if (mAdapter == null){
                mAdapter = new StudentAdapter(students);
                mStudentListRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setStudents(students);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(getActivity(),"There is no students without group!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_INVITE){
            // Handle result
            //message to be received is neglected on purpose
            String message = data.getStringExtra(SendInviteFragment.EXTRA_MESSAGE);
            UUID studentId = (UUID) data.getSerializableExtra(SendInviteFragment.EXTRA_INVITE);
            StudentLab studentLab = StudentLab.get(getActivity());
            studentLab.addMember(studentLab.getStudent(studentId), mGroup);
            Toast.makeText(getActivity(),"Invited successful!", Toast.LENGTH_LONG).show();

        }
    }

    private class StudentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mStudentNameTextView;
        private TextView mStudentIntroTextView;
        private Student mStudent;
        private Button mInviteButton;
        private ImageView mPhotoView;
        private File mPhotoFile;


        public StudentHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_student, parent, false));
            itemView.setOnClickListener(this);
            mStudentNameTextView = (TextView) itemView.findViewById(R.id.student_name);
            mStudentIntroTextView = (TextView) itemView.findViewById(R.id.student_details);
            mInviteButton = (Button) itemView.findViewById(R.id.invite_button);
            mPhotoView = (ImageView) itemView.findViewById(R.id.student_photo);


        }
        public void bind(Student student){
            mStudent = student;
            mStudentNameTextView.setText(mStudent.getFirstName());
            mStudentIntroTextView.setText(mStudent.getIntroduction());
            mInviteButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    FragmentManager manager = getFragmentManager();
                    SendInviteFragment dialog = SendInviteFragment.newInstance(mStudent.getId());
                    dialog.setTargetFragment(StudentListFragment.this, REQUEST_INVITE);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            updateUI();
                        }
                    });
                    dialog.show(manager, DIALOG_INVITE);
                }
            });
            mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);
            updatePhotoView();
        }
        private void updatePhotoView(){
            if (mPhotoFile == null || !mPhotoFile.exists()){
                mPhotoView.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(
                        mPhotoFile.getPath(), getActivity());
                mPhotoView.setImageBitmap(bitmap);
            }
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

            Intent intent = SubjectActivity.newIntent(getActivity(), mStudent.getId());
            //startActivity(intent);
            startActivity(intent);
        }
    }



    private class StudentAdapter extends RecyclerView.Adapter<StudentHolder>{
        private List<Student> mStudents;

        public StudentAdapter(List<Student> students){
            mStudents = students;
        }

        @Override
        public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new StudentHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(StudentHolder holder, int position){
            Student student = mStudents.get(position);
            holder.bind(student);
        }
        @Override
        public int getItemCount(){
            return mStudents.size();
        }

        public void setStudents(List<Student> students){
            mStudents = students;
        }
    } //end of class StudentAdapter

    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private boolean studentListExist(){
        if (StudentLab.get(getActivity()).getStudentsWithoutGroupByProject(mProject.getId())==null){
            return false;
        } else {
            return true;
        }
    }
} //end of class SubjectListFragment extends Fragment
