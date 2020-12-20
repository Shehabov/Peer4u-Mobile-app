package com.bignerdranch.android.peer4u;


import androidx.fragment.app.Fragment;

public class SignupActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new SignupFragment();
    }
}
