package com.bignerdranch.android.peer4u;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Group {

    private UUID mId;
    private String mGroupName;
    private UUID mLeaderId;
    private List<Student> mMemberList;
    private UUID mProjectId;

    public Group() {
        this(UUID.randomUUID());

    }

    public Group(UUID id){
        mId = id;
        mMemberList = new ArrayList<>();
    }

    public UUID getId() {
        return mId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        mGroupName = groupName;
    }

    public UUID getLeaderId() {
        return mLeaderId;
    }

    public void setLeaderId(UUID leader) {
        mLeaderId = leader;
    }

    public UUID getProjectId() {
        return mProjectId;
    }

    public void setProjectId(UUID projectId) {
        mProjectId = projectId;
    }

    public List<Student> getMemberList() {
        return mMemberList;
    }

    public void setMemberList(List<Student> memberList) {
        mMemberList = memberList;
    }

    public String getMemberNames(){
        String names = "" ;
        if (!(mMemberList == null) && mMemberList.size()>0){
            for(int i=0; i< mMemberList.size(); i++){
                names = names + (i+2) + ". " + mMemberList.get(i).getFirstName() + "\n";
            }
        }
        return names;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
