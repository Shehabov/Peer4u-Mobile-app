package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;


import java.util.UUID;

import androidx.fragment.app.Fragment;

public class ProjectListActivity extends SingleFragmentActivity {
    public static final String EXTRA_SUBJECT_ID = "com.bignerdranch.android.peer4u.subject_id";
    public static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    @Override
    protected Fragment createFragment(){
        UUID subjectId = (UUID) getIntent().getSerializableExtra(EXTRA_SUBJECT_ID);
        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        return ProjectListFragment.newInstance(subjectId, studentId);
    }

    public static Intent newIntent(Context packageContext, UUID subjectId, UUID studentId){
        Intent intent = new Intent(packageContext, ProjectListActivity.class);
        intent.putExtra(EXTRA_SUBJECT_ID, subjectId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        return intent;
    }
}
