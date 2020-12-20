package com.bignerdranch.android.peer4u;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {

    private static final int REQUEST_EPISODE = 1;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private Button mGoogleSignUpButton;
    private Button mMicrosoftSignUpButton;
    private Button mSignInButton;
    private ImageButton mGoogleIconSignUpButton;
    private ImageButton mMicrosoftIconSignUpButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        mGoogleIconSignUpButton = (ImageButton) v.findViewById(R.id.google_icon);
        mGoogleIconSignUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Student mStudent = new Student();
                StudentLab.get(getActivity()).addStudent(mStudent);
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mGoogleSignUpButton = (Button) v.findViewById(R.id.google_signup);
        mGoogleSignUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Student mStudent = new Student();
                StudentLab.get(getActivity()).addStudent(mStudent);
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mMicrosoftIconSignUpButton = (ImageButton) v.findViewById(R.id.microsoft_icon);
        mMicrosoftIconSignUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Student mStudent = new Student();
                StudentLab.get(getActivity()).addStudent(mStudent);
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mMicrosoftSignUpButton = (Button) v.findViewById(R.id.microsoft_signup);
        mMicrosoftSignUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                Student mStudent = new Student();
                StudentLab.get(getActivity()).addStudent(mStudent);
                Intent intent = SetupActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });

        mSignInButton = (Button) v.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), SigninActivity.class);
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
