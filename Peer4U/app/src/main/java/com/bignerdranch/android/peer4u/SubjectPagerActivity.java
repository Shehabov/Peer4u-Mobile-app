package com.bignerdranch.android.peer4u;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SubjectPagerActivity extends AppCompatActivity {
    private static final String EXTRA_SUBJECT_ID = "com.bignerdranch.android.peer4u.subject_id";
    private static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    private ViewPager mViewPager;
    private List<Subject> mSubjects;

    public static Intent newIntent(Context packageContext, UUID subjectId, UUID studentId){
        Intent intent = new Intent(packageContext, SubjectPagerActivity.class);
        intent.putExtra(EXTRA_SUBJECT_ID, subjectId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pager);

        final UUID subjectId = (UUID) getIntent().getSerializableExtra(EXTRA_SUBJECT_ID);
        final UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);

        mViewPager = (ViewPager) findViewById(R.id.group_view_pager);
        mViewPager.setPadding(16,16,16,16);

        mSubjects = StudentLab.get(this).getSubjects(studentId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public androidx.fragment.app.Fragment getItem(int position) {
                Subject subject = mSubjects.get(position);
                return ProjectListFragment.newInstance(subject.getId(), studentId);
            }

            @Override
            public int getCount() {
                return mSubjects.size();
            }
        });

        for (int i = 0; i< mSubjects.size(); i++){
            if (mSubjects.get(i).getId().equals(subjectId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
