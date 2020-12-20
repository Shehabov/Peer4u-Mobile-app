package com.bignerdranch.android.peer4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SigninFragment extends Fragment {

    private Button mGoogleSignInButton;
    private Button mMicrosoftSignInButton;
    private Button mSignUpButton;
    private ImageButton mGoogleIconSignInButton;
    private ImageButton mMicrosoftIconSignInButton;
    private Button mAdminButton;
    //always sign in the first student created, change the index to sign in different students
    private int mStudentIndex = 0;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_signin, container, false);

        mGoogleIconSignInButton = (ImageButton) v.findViewById(R.id.google_icon);
        mGoogleIconSignInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //checking if there is any student in the database
                if(StudentLab.get(getActivity()).getStudents().size()==0){
                    Toast.makeText(getActivity(), "Please sign up first!", Toast.LENGTH_LONG).show();
                } else {
                    //getting student specified by mStudentIndex=0
                    Student student = StudentLab.get(getActivity()).getStudents().get(mStudentIndex);
                    Intent intent = SubjectListActivity.newIntent(getActivity(), student.getId());
                    startActivity(intent);
                }
            }
        });

        mGoogleSignInButton = (Button) v.findViewById(R.id.google_signin);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //checking if there is any student in the database
                if(StudentLab.get(getActivity()).getStudents().size()==0){
                    Toast.makeText(getActivity(), "Please sign up first!", Toast.LENGTH_LONG).show();
                } else {
                    //getting student specified by mStudentIndex=0
                    Student student = StudentLab.get(getActivity()).getStudents().get(mStudentIndex);
                    Intent intent = SubjectListActivity.newIntent(getActivity(), student.getId());
                    startActivity(intent);
                }
            }
        });

        mMicrosoftIconSignInButton = (ImageButton) v.findViewById(R.id.microsoft_icon);
        mMicrosoftIconSignInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //checking if there is any student in the database
                if(StudentLab.get(getActivity()).getStudents().size()==0){
                    Toast.makeText(getActivity(), "Please sign up first!", Toast.LENGTH_LONG).show();
                } else {
                    //getting student specified by mStudentIndex=0
                    Student student = StudentLab.get(getActivity()).getStudents().get(mStudentIndex);
                    Intent intent = SubjectListActivity.newIntent(getActivity(), student.getId());
                    startActivity(intent);
                }
            }
        });

        mMicrosoftSignInButton = (Button) v.findViewById(R.id.microsoft_signin);
        mMicrosoftSignInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //checking if there is any student in the database
                if(StudentLab.get(getActivity()).getStudents().size()==0){
                    Toast.makeText(getActivity(), "Please sign up first!", Toast.LENGTH_LONG).show();
                } else {
                    //getting student specified by mStudentIndex=0
                    Student student = StudentLab.get(getActivity()).getStudents().get(mStudentIndex);
                    Intent intent = SubjectListActivity.newIntent(getActivity(), student.getId());
                    startActivity(intent);
                }
            }
        });

        mSignUpButton = (Button) v.findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), SignupActivity.class);
                    startActivity(intent);
            }
        });

        mAdminButton = (Button) v.findViewById(R.id.admin_button);
        mAdminButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }





    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_CRIME){
            // Handle result
            mCrimeId = data.getIntExtra(ProjectListActivity.EXTRA_EPISODE_ID, 0);
        }
    }*/


} //end of class SignupFragment extends Fragment
