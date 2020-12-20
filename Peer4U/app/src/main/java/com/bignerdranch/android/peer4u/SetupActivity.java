package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class SetupActivity extends SingleFragmentActivity {
    public static final String EXTRA_STUDENT_ID = "com.bignerdranch.android.peer4u.student_id";

    @Override
    protected Fragment createFragment(){
        UUID studentId = (UUID) getIntent().getSerializableExtra(EXTRA_STUDENT_ID);
        return SetupFragment.newInstance(studentId);
    }

    public static Intent newIntent(Context packageContext, UUID studentId){
        Intent intent = new Intent(packageContext, SetupActivity.class);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        return intent;
    }
}
