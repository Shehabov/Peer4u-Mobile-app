package com.bignerdranch.android.peer4u;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class User {

    private UUID mId;
    private String mFirstName;
    private String mLastName;


    public User() {
        this(UUID.randomUUID());

    }

    public User(UUID id){
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
