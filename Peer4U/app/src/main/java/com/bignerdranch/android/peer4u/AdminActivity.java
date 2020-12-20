package com.bignerdranch.android.peer4u;

import android.content.Context;
import android.content.Intent;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class AdminActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new AdminFragment();
    }

}
