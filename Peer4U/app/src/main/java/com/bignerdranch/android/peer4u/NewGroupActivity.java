package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class NewGroupActivity extends SingleFragmentActivity {
    public static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    public static final String EXTRA_PROJECT_ID = "com.bignerdranch.android.peer4u.project_id";
    @Override
    protected Fragment createFragment(){
        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        UUID projectId = (UUID) getIntent().getSerializableExtra(EXTRA_PROJECT_ID);
        return NewGroupFragment.newInstance(projectId, studentId);
    }

    public static Intent newIntent(Context packageContext, UUID studentId, UUID projectId, UUID subjectId){
        Intent intent = new Intent(packageContext, NewGroupActivity.class);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        return intent;
    }
}
