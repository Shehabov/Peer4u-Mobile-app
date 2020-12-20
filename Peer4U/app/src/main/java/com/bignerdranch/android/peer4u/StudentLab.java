package com.bignerdranch.android.peer4u;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.peer4u.database.Peer4UBaseHelper;
import com.bignerdranch.android.peer4u.database.Peer4UCursorWrapper;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.*;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.StudentTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupMemberTable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentLab {

    //singleton object
    private static StudentLab sStudentLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static StudentLab get(Context context){
        if (sStudentLab == null){
            sStudentLab = new StudentLab(context);
        }
        return sStudentLab;
    }

    private StudentLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new Peer4UBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addStudent(Student student){
        ContentValues values = getContentValues(student);
        mDatabase.insert(StudentTable.NAME, null, values);
    }

    public void addSubject(Student s, Subject c){
        ContentValues values = getContentValues(s, c);
        mDatabase.insert(StudentSubjectTable.NAME, null, values);
    }
    public Group createGroup(UUID leaderId, UUID projectId){
        Group group = new Group();
        group.setLeaderId(leaderId);
        group.setProjectId(projectId);
        ContentValues values = getContentValues(group);
        mDatabase.insert(GroupTable.NAME, null, values);
        return group;
    }

    public void addMember(Student student, Group group){
        ContentValues values = getContentValues(student, group);
        mDatabase.insert(GroupMemberTable.NAME, null, values);
    }

    public void leaveGroup(UUID groupId, UUID studentId){
        mDatabase.delete(GroupMemberTable.NAME, GroupMemberTable.Cols.UUID + " = ? and " + GroupMemberTable.Cols.MEMBER + " = ?",
                new String[] { groupId.toString(), studentId.toString() });
    }
    public List<Student> getStudents(){
        List<Student> students = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryStudents(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                students.add(cursor.getStudent());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return students;
    }

    public List<Student> getStudentsByProject(UUID projectId){
        List<Student> students = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryProjects(ProjectTable.Cols.PROJECTUUID + " = ?",
                new String[] {projectId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                students.addAll(getStudentsBySubject(cursor.getProject().getSubjectUUID()));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return students;
    }
    public List<Student> getStudentsWithoutGroupByProject(UUID projectId){
        List<Student> students = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryProjects(ProjectTable.Cols.PROJECTUUID + " = ?",
                new String[] {projectId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                List<Student> studentsOfSubject = getStudentsBySubject(cursor.getProject().getSubjectUUID());
                if (!(studentsOfSubject == null)){
                    for (int i=0; i< studentsOfSubject.size(); i++){
                        if (getGroupByStudentProject(studentsOfSubject.get(i).getId(), projectId)==null){
                            students.add(studentsOfSubject.get(i));
                        }
                    }
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return students;
    }
    public List<Student> getStudentsBySubject(UUID subjectId){
        List<Student> students = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryTable(StudentSubjectTable.NAME, StudentSubjectTable.Cols.SUBJECTUUID + " = ?",
                new String[] {subjectId.toString()});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                students.add(getStudent(cursor.getStudentUUIDfromSubject()));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return students;
    }

    public Student getStudent(UUID id){
        Peer4UCursorWrapper cursor = queryStudents(
                StudentTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getStudent();
        } finally {
            cursor.close();
        }
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

    //successful
    public List<Subject> getSubjects(UUID studentUUID){
        List<Subject> subjects = new ArrayList<>();

        Peer4UCursorWrapper cursor = queryTable(StudentSubjectTable.NAME,StudentSubjectTable.Cols.STUDENTUUID + " = ?",
                new String[] { studentUUID.toString() });

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                subjects.add(getSubject(cursor.getSubjectUUID()));
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();

        }
        return subjects;
    }

    public List<Group> getGroups(UUID studentId){
        List<Group> groups = new ArrayList<>();


            Peer4UCursorWrapper cursor = queryGroupMember(
                    GroupMemberTable.Cols.MEMBER + " = ?",
                    new String[] { studentId.toString() }
            );
            try {
                if (cursor.getCount() == 0){
                    return null;
                }
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    Group group = getGroup(cursor.getGroupUUID());
                    group.setMemberList(getGroupMembers(group.getId()));
                    groups.add(group);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            if (!(getGroupListByLeader(studentId) == null)){
                groups.addAll(getGroupListByLeader(studentId));
            }
        return groups;
    }
    public File getPhotoFile(Student student){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, student.getPhotoFilename());
    }
    public File getPhotoFile(Group group){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, group.getPhotoFilename());
    }

    public void updateStudent(Student student){
        String uuidString = student.getId().toString();
        ContentValues values = getContentValues(student);

        mDatabase.update(StudentTable.NAME, values,
                StudentTable.Cols.UUID + " = ?",
                new String[] { uuidString });

        try {
            for (int i=0; i<student.getSubjects().size();i++){
                ContentValues stusubvalues = getContentValues(student, student.getSubjects().get(i));
                mDatabase.update(StudentSubjectTable.NAME, stusubvalues,
                        StudentSubjectTable.Cols.STUDENTUUID + " = ?",
                        new String[] { uuidString });
            }
        } catch (NullPointerException e) {
            Log.d("PEER4U", "Subject list is empty in Student");
        }
    }

    public void updateGroup(Group group){
        String uuidString = group.getId().toString();
        ContentValues values = getContentValues(group);

        mDatabase.update(GroupTable.NAME, values,
                GroupTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public Group getGroupByLeader(UUID leaderId, UUID projectId){
        Peer4UCursorWrapper cursor = queryGroup(
                GroupTable.Cols.LEADER + " = ? and " + GroupTable.Cols.PROJECTUUID + " = ?",
                new String[] { leaderId.toString(), projectId.toString() }
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            Group group = cursor.getGroup();
            group.setMemberList(getGroupMembers(group.getId()));
            return group;
        } finally {
            cursor.close();
        }

    }
    private List<Group> getGroupListByLeader(UUID leaderId){
        List<Group> groups = new ArrayList<>();
        Peer4UCursorWrapper cursor = queryGroup(
                GroupTable.Cols.LEADER + " = ?",
                new String[] { leaderId.toString() }
        );
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                Group group = cursor.getGroup();
                group.setMemberList(getGroupMembers(group.getId()));
                groups.add(group);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();

        }
        return groups;

    }

    public Group getGroupByStudentProject(UUID studentId, UUID projectId){

        if (getGroupByLeader(studentId, projectId) == null){
            Peer4UCursorWrapper cursor = queryGroupMember(
                    GroupMemberTable.Cols.MEMBER + " = ?",
                    new String[] { studentId.toString() }
            );
            try {
                if (cursor.getCount() == 0){
                    return null;
                }
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    if (getGroup(cursor.getGroupUUID()).getProjectId().equals(projectId)){
                        Group group = getGroup(cursor.getGroupUUID());
                        group.setMemberList(getGroupMembers(group.getId()));
                        return group;
                    } else {
                        cursor.moveToNext();
                    }
                }
                return null;
            } finally {
                cursor.close();
            }
        } else {
            return getGroupByLeader(studentId, projectId);
        }

    }

    public List<Student> getGroupMembers(UUID groupId){
        ArrayList<Student> members = new ArrayList<>();
        Peer4UCursorWrapper cursor = queryGroupMember(
                GroupMemberTable.Cols.UUID + " = ?",
                new String[] { groupId.toString() }
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                members.add(getStudent(cursor.getStudentUUID()));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return members;
    }
    public List<Group> getGroupsByProject(UUID projectId){
        List<Group> groups = new ArrayList<>();
        Peer4UCursorWrapper cursor = queryGroup(
                GroupTable.Cols.PROJECTUUID + " = ?",
                new String[] { projectId.toString() }
        );

        try {

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Group group = cursor.getGroup();
            group.setMemberList(getGroupMembers(group.getId()));
            groups.add(group);
            cursor.moveToNext();
        }
    } finally {
        cursor.close();
    }
        return groups;
    }
    public Group getGroup(UUID groupId){
        Peer4UCursorWrapper cursor = queryGroup(
                GroupTable.Cols.UUID + " = ?",
                new String[] { groupId.toString() }
        );

        try {
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            Group group = cursor.getGroup();
            group.setMemberList(getGroupMembers(group.getId()));
            return group;
        } finally {
            cursor.close();
        }
    }


    private Peer4UCursorWrapper queryStudents(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                StudentTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
    }
    private Peer4UCursorWrapper queryGroup(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                GroupTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
    }

    private Peer4UCursorWrapper queryGroupMember(String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                GroupMemberTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
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
    private Peer4UCursorWrapper queryTable(String tablename, String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                tablename,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );

        return new Peer4UCursorWrapper(cursor);
    }
    private static ContentValues getContentValues(Student student){
        ContentValues values = new ContentValues();
        values.put(StudentTable.Cols.UUID, student.getId().toString());
        values.put(StudentTable.Cols.FIRSTNAME, student.getFirstName());
        values.put(StudentTable.Cols.LASTNAME, student.getLastName());
        values.put(StudentTable.Cols.INTAKE, student.getIntake().getTime());
        values.put(StudentTable.Cols.PROGRAMME, student.getProgramme());
        values.put(StudentTable.Cols.UNIVERSITY, student.getUniveristy());
        values.put(StudentTable.Cols.INTRODUCTION, student.getIntroduction());

        return values;
    }

    private static ContentValues getContentValues(Group group){
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.UUID, group.getId().toString());
        values.put(GroupTable.Cols.GROUPNAME, group.getGroupName());
        values.put(GroupTable.Cols.LEADER, group.getLeaderId().toString());
        values.put(GroupTable.Cols.PROJECTUUID, group.getProjectId().toString());

        return values;
    }
    private static ContentValues getContentValues(Student student, Group group){
        ContentValues values = new ContentValues();
        values.put(GroupMemberTable.Cols.UUID, group.getId().toString());
        values.put(GroupMemberTable.Cols.MEMBER, student.getId().toString());
        return values;
    }

    private static ContentValues getContentValues(Student student, Subject subject){
        ContentValues values = new ContentValues();
        values.put(StudentSubjectTable.Cols.STUDENTUUID, student.getId().toString());
        values.put(StudentSubjectTable.Cols.SUBJECTUUID, subject.getId().toString());
        return values;
    }
    private static ContentValues getContentValues(Subject subject){
        ContentValues values = new ContentValues();
        values.put(SubjectTable.Cols.UUID, subject.getId().toString());
        values.put(SubjectTable.Cols.SUBJECTNAME, subject.getSubjectName());
        values.put(SubjectTable.Cols.SUBJECTCODE, subject.getSubjectCode());

        return values;
    }

}
