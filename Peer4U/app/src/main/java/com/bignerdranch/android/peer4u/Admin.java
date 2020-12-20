package com.bignerdranch.android.peer4u;

import java.util.UUID;

public class Admin extends User {
    private UUID mId;
    private String mFirstName;
    private String mLastName;


    public Admin() {
        this(UUID.randomUUID());

    }

    public Admin(UUID id){
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
