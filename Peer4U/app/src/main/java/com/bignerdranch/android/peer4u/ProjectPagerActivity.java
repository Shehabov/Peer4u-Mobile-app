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

public class ProjectPagerActivity extends AppCompatActivity {
    private static final String EXTRA_PROJECT_ID = "com.bignerdranch.android.peer4u.project_id";
    private static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    private ViewPager mViewPager;
    private List<Project> mProjects;

    public static Intent newIntent(Context packageContext, UUID projectId,  UUID studentId){
        Intent intent = new Intent(packageContext, ProjectPagerActivity.class);
        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pager);

        final UUID projectId = (UUID) getIntent().getSerializableExtra(EXTRA_PROJECT_ID);
        final UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);

        mViewPager = (ViewPager) findViewById(R.id.group_view_pager);
        mViewPager.setPadding(16,16,16,16);
        Project project = SubjectLab.get(this).getProject(projectId);
        mProjects = SubjectLab.get(this).getProjectsFromSubject(project.getSubjectUUID());
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public androidx.fragment.app.Fragment getItem(int position) {
                Project project = mProjects.get(position);
                return ProjectFragment.newInstance(project.getId(), studentId);
            }

            @Override
            public int getCount() {
                return mProjects.size();
            }
        });

        for (int i = 0; i< mProjects.size(); i++){
            if (mProjects.get(i).getId().equals(projectId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
