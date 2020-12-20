package com.bignerdranch.android.peer4u;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Student extends User {

    private UUID mId;
    private String mFirstName;
    private String mLastName;
    private Date mIntake;
    private String mProgramme;
    private String mUniveristy;
    private String mIntroduction;
    private List<Subject> mSubjects;
    private List<Group> mGroups;

    public Student() {
        this(UUID.randomUUID());

    }

    public Student(UUID id){
        mId = id;
        mIntake = new Date();
        mSubjects = new ArrayList<>();
        mGroups = new ArrayList<>();
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

    public Date getIntake() {
        return mIntake;
    }

    public void setIntake(Date intake) {
        mIntake = intake;
    }

    public String getProgramme() {
        return mProgramme;
    }

    public void setProgramme(String programme) {
        mProgramme = programme;
    }


    public List<Subject> getSubjects() {
        return mSubjects;
    }

    public void setSubjects(List<Subject> subjects) {
        mSubjects = subjects;
    }

    public String getUniveristy() {
        return mUniveristy;
    }

    public void setUniveristy(String univeristy) {
        mUniveristy = univeristy;
    }

    public String getIntroduction() {
        return mIntroduction;
    }

    public void setIntroduction(String introduction) {
        mIntroduction = introduction;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups = groups;
    }

    public void addSubject(Subject subject){
        mSubjects.add(subject);
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
