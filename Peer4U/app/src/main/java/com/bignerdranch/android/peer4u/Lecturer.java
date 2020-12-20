package com.bignerdranch.android.peer4u;

import java.util.List;
import java.util.UUID;

public class Lecturer extends User {

    private UUID mId;
    private String mFirstName;
    private String mLastName;
    private List<Subject> mSubjects;


    public Lecturer() {
        this(UUID.randomUUID());

    }

    public Lecturer(UUID id) {
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

    public List<Subject> getSubjects() {
        return mSubjects;
    }

    public void setSubjects(List<Subject> subjects) {
        mSubjects = subjects;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
