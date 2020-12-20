package com.bignerdranch.android.peer4u;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class Subject {

    private UUID mId;
    private String mSubjectName;
    private String mSubjectCode;
    private List<Project> mProjects;

    public Subject() {
        this(UUID.randomUUID());
    }

    public Subject(UUID id){
        mId = id;
        mProjects = new ArrayList<>();
    }

    public UUID getId() {
        return mId;
    }

    public String getSubjectName() {
        return mSubjectName;
    }

    public void setSubjectName(String subjectName) {
        mSubjectName = subjectName;
    }

    public String getSubjectCode() {
        return mSubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        mSubjectCode = subjectCode;
    }


    public List<Project> getProjects() {
        return mProjects;
    }

    public Project getProject(UUID projectId) {
        Project project = new Project();
        try {
            for (int i = 0; i< mProjects.size(); i++) {
                if (mProjects.get(i).getId().equals(projectId)) {
                    project = mProjects.get(i);
                }
            }
        } finally {

        }
        return project;
    }

    public void setProjects(List<Project> projects) {
        mProjects = projects;
    }

    public Project createNewProject(){
        Project project = new Project(this.mId);
        mProjects.add(project);
        return project;
    }
    public void updateProject(Project project){
        if (mProjects.size() != 0){
            for (int i=0; i> mProjects.size(); i++){
                if (mProjects.get(i).getId() == project.getId()){
                    mProjects.set(i, project);
                }
            }
        }
    }
    public void addProject(Project project){
        mProjects.add(project);
    }
    public void deleteProject(Project project){
        mProjects.remove(project);
    }
    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
}
