package com.bignerdranch.android.peer4u;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Project {
    private UUID mId;
    private String mAssignmentName;
    private String mAssignmentDetails;
    private Date mDateCreated;
    private UUID mSubjectUUID;
    private List<Group> mGroupList;

    public Project(UUID subject) {
        this(UUID.randomUUID(), subject);

    }

    public Project(UUID id, UUID subject){
        mId = id;
        mDateCreated = new Date();
        mSubjectUUID = subject;
        mGroupList = new ArrayList<>();
    }
    public Project() {
        this(UUID.randomUUID(), UUID.randomUUID());

    }

    public UUID getId() {
        return mId;
    }

    public String getAssignmentName() {
        return mAssignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        mAssignmentName = assignmentName;
    }

    public String getAssignmentDetails() {
        return mAssignmentDetails;
    }

    public void setAssignmentDetails(String assignmentDetails) {
        mAssignmentDetails = assignmentDetails;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        mDateCreated = dateCreated;
    }

    public UUID getSubjectUUID() {
        return mSubjectUUID;
    }

    public void setSubject(UUID subject) {
        mSubjectUUID = subject;
    }


    public List<Group> getGroupList() {
        return mGroupList;
    }

    public void setGroupList(List<Group> groupList) {
        mGroupList = groupList;
    }

    public void addGroup(Group group){
        mGroupList.add(group);
    }
}
