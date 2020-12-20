package com.bignerdranch.android.peer4u;


import androidx.fragment.app.Fragment;

public class SigninActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new SigninFragment();
    }
}
