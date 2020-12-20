package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class ProjectActivity extends SingleFragmentActivity {
    public static final String EXTRA_PROJECT_ID = "com.bignerdranch.android.peer4u.project_id";
    public static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    @Override
    protected Fragment createFragment(){
        UUID projectId = (UUID) getIntent().getSerializableExtra(EXTRA_PROJECT_ID);
        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        return ProjectFragment.newInstance(projectId, studentId);
    }

    public static Intent newIntent(Context packageContext, UUID projectId, UUID studentId){
        Intent intent = new Intent(packageContext, ProjectActivity.class);
        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        return intent;
    }


}
