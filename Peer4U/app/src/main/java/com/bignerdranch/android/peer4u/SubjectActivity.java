package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class SubjectActivity extends SingleFragmentActivity {
    public static final String EXTRA_SUBJECT_ID = "com.bignerdranch.android.peer4u.subject_id";
    @Override
    protected Fragment createFragment(){
        UUID subjectId = (UUID) getIntent().getSerializableExtra(EXTRA_SUBJECT_ID);
        return SubjectFragment.newInstance(subjectId);
    }

    public static Intent newIntent(Context packageContext, UUID subjectId){
        Intent intent = new Intent(packageContext, SubjectActivity.class);
        intent.putExtra(EXTRA_SUBJECT_ID, subjectId);
        return intent;
    }
}
