package com.bignerdranch.android.peer4u;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.peer4u.database.Peer4UBaseHelper;
import com.bignerdranch.android.peer4u.database.Peer4UCursorWrapper;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupMemberTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.StudentSubjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.ProjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.SubjectTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SubjectLab {

    //singleton object
    private static SubjectLab sSubjectLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static SubjectLab get(Context context){
        if (sSubjectLab == null){
            sSubjectLab = new SubjectLab(context);
        }
        return sSubjectLab;
    }

    private SubjectLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new Peer4UBaseHelper(mContext)
                .getWritableDatabase();
    }


    public List<Subject> getSubjects(){
        List<Subject> subjects = new ArrayList<>();

        Peer4UCursorWrapper cursor = querySubjects(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                subjects.add(cursor.getSubject());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        List<Project> projects = getProjects();
        List<Project> temp = new ArrayList<>();
        try {
            for (int i=0; i<subjects.size(); i++){
                for (int j=0; j<projects.size(); j++){
                    if (projects.get(j).getSubjectUUID()==subjects.get(i).getId()){
                        temp.add(projects.get(j));
                    }
                    subjects.get(i).setProjects(temp);
                }

            }
        } finally {

        }

        return subjects;
    }

    private List<Project> getProjects(){
        List<Project> projects = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryProjects(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                projects.add(cursor.getProject());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return projects;
    }

    public Subject getSubject(UUID subjectUUID){

        Peer4UCursorWrapper cursor = querySubjects( SubjectTable.Cols.UUID + " = ?",
                new String[] {subjectUUID.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSubject();
        } finally {
            cursor.close();
        }

    }

    public Subject getSubject(String subjectCode){

        Peer4UCursorWrapper cursor = querySubjects( SubjectTable.Cols.SUBJECTCODE + " = ?",
                new String[] {subjectCode.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getSubject();
        } finally {
            cursor.close();
        }

    }
    public Project getProject(UUID projectUUID){

        Peer4UCursorWrapper cursor = queryProjects( ProjectTable.Cols.PROJECTUUID + " = ?",
                new String[] {projectUUID.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getProject();
        } finally {
            cursor.close();
        }

    }
    public List<Project> getProjectsFromSubject(UUID subjectUUID){
        List<Project> projects = new ArrayList<>();
        Peer4UCursorWrapper cursor = queryProjects( ProjectTable.Cols.SUBJECTUUID + " = ?",
                new String[] {subjectUUID.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                projects.add(cursor.getProject());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return projects;

    }

    public void updateSubject (Subject subject){
        String uuidString = subject.getId().toString();
        ContentValues values = getContentValues(subject);

        mDatabase.update(SubjectTable.NAME, values,
                SubjectTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
    public void addSubject (Subject subject){
        ContentValues values = getContentValues(subject);
        mDatabase.insert(SubjectTable.NAME, null, values);
    }
    public void addProject (Project project){
        ContentValues values = getContentValues(project);
        mDatabase.insert(ProjectTable.NAME, null, values);
    }
    public void updateProject (Project project){
        String uuidString = project.getId().toString();
        ContentValues values = getContentValues(project);

        mDatabase.update(ProjectTable.NAME, values,
                ProjectTable.Cols.PROJECTUUID + " = ?",
                new String[] { uuidString });
    }

    private Peer4UCursorWrapper querySubjects(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                SubjectTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
    }
    private Peer4UCursorWrapper queryProjects(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                ProjectTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Subject subject){
        ContentValues values = new ContentValues();
        values.put(SubjectTable.Cols.UUID, subject.getId().toString());
        values.put(SubjectTable.Cols.SUBJECTNAME, subject.getSubjectName());
        values.put(SubjectTable.Cols.SUBJECTCODE, subject.getSubjectCode());

        return values;
    }

    private static ContentValues getContentValues(Project project){
        ContentValues values = new ContentValues();
        values.put(ProjectTable.Cols.PROJECTUUID, project.getId().toString());
        values.put(ProjectTable.Cols.ASIGNMENTNAME, project.getAssignmentName());
        values.put(ProjectTable.Cols.DETAILS, project.getAssignmentDetails());
        values.put(ProjectTable.Cols.DATECREATED, project.getDateCreated().toString());
        values.put(ProjectTable.Cols.SUBJECTUUID, project.getSubjectUUID().toString());

        return values;
    }
}
