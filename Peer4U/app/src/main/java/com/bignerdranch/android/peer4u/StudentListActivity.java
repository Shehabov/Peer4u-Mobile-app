package com.bignerdranch.android.peer4u;


import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class StudentListActivity extends SingleFragmentActivity {

    public static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";
    public static final String EXTRA_PROJECT_ID = "com.bignerdranch.android.peer4u.project_id";
    public static final String EXTRA_GROUP_ID = "com.bignerdranch.android.peer4u.group_id";
    @Override
    protected Fragment createFragment(){
        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        UUID projectId = (UUID) getIntent().getSerializableExtra(EXTRA_PROJECT_ID);
        UUID groupId = (UUID) getIntent().getSerializableExtra(EXTRA_GROUP_ID);
        return StudentListFragment.newInstance(studentId, projectId, groupId);
    }

    public static Intent newIntent(Context packageContext, UUID studentId, UUID projectId, UUID groupId){
        Intent intent = new Intent(packageContext, StudentListActivity.class);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        intent.putExtra(EXTRA_PROJECT_ID, projectId);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        return intent;
    }
}
