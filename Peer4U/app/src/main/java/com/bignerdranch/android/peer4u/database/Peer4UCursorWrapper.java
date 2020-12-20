package com.bignerdranch.android.peer4u.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.peer4u.Group;
import com.bignerdranch.android.peer4u.Lecturer;
import com.bignerdranch.android.peer4u.Project;
import com.bignerdranch.android.peer4u.Student;
import com.bignerdranch.android.peer4u.Subject;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.StudentTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.LecturerTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.LecturerSubjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.SubjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.StudentSubjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.ProjectTable;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.GroupMemberTable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Peer4UCursorWrapper extends CursorWrapper {
    public Peer4UCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //no grouplist
    public Student getStudent(){
        String uuidString = getString(getColumnIndex(StudentTable.Cols.UUID));
        String firstname = getString(getColumnIndex(StudentTable.Cols.FIRSTNAME));
        String lastname = getString(getColumnIndex(StudentTable.Cols.LASTNAME));
        long intake = getLong(getColumnIndex(StudentTable.Cols.INTAKE));
        String programme = getString(getColumnIndex(StudentTable.Cols.PROGRAMME));
        String university = getString(getColumnIndex(StudentTable.Cols.UNIVERSITY));
        String introduction = getString(getColumnIndex(StudentTable.Cols.INTRODUCTION));

        Student student = new Student(UUID.fromString(uuidString));
        student.setFirstName(firstname);
        student.setLastName(lastname);
        student.setIntake(new Date(intake));
        student.setProgramme(programme);
        student.setUniveristy(university);
        student.setIntroduction(introduction);

        return student;
    }

    //no memberlist
    public Group getGroup(){
        String uuidString = getString(getColumnIndex(GroupTable.Cols.UUID));
        String uuidLeader = getString(getColumnIndex(GroupTable.Cols.LEADER));
        String groupname = getString(getColumnIndex(GroupTable.Cols.GROUPNAME));
        String projectuuidString = getString(getColumnIndex(GroupTable.Cols.PROJECTUUID));

        Group group = new Group(UUID.fromString(uuidString));
        group.setGroupName(groupname);
        group.setProjectId(UUID.fromString(projectuuidString));
        group.setLeaderId(UUID.fromString(uuidLeader));

        return group;

    }

    //no subject list
    public Lecturer getLecturer(){
        String uuidString = getString(getColumnIndex(LecturerTable.Cols.UUID));
        String firstname = getString(getColumnIndex(LecturerTable.Cols.FIRSTNAME));
        String lastname = getString(getColumnIndex(LecturerTable.Cols.LASTNAME));

        Lecturer lecturer = new Lecturer(UUID.fromString(uuidString));
        lecturer.setFirstName(firstname);
        lecturer.setLastName(lastname);

        return lecturer;
    }

    public void updateLecturerSubject(List<Lecturer> lecturers, List<Subject> subjects){
        String lecturerUuidString = getString(getColumnIndex(LecturerSubjectTable.Cols.LECTURERUUID));
        String subjectUuidString = getString(getColumnIndex(LecturerSubjectTable.Cols.SUBJECTUUID));


        for (int i=0; i<lecturers.size(); i++){
            for (int j=0; j< subjects.size(); j++) {
                if (lecturers.get(i).getId().equals(UUID.fromString(lecturerUuidString)) && subjects.get(j).getId().equals(UUID.fromString(subjectUuidString))) {
                    if (!lecturers.get(i).getSubjects().contains(subjects.get(j))){
                        lecturers.get(i).getSubjects().add(subjects.get(j));
                    }
                }
            }
        }

    }

    //no project list
    public Subject getSubject(){
        String uuidString = getString(getColumnIndex(SubjectTable.Cols.UUID));
        String subjectname = getString(getColumnIndex(SubjectTable.Cols.SUBJECTNAME));
        String subjectcode = getString(getColumnIndex(SubjectTable.Cols.SUBJECTCODE));

        Subject subject = new Subject(UUID.fromString(uuidString));
        subject.setSubjectName(subjectname);
        subject.setSubjectCode(subjectcode);

        return subject;
    }
    public Subject getSubject(UUID subjectuuidString){

        String subjectname = getString(getColumnIndex(SubjectTable.Cols.SUBJECTNAME));
        String subjectcode = getString(getColumnIndex(SubjectTable.Cols.SUBJECTCODE));

        Subject subject = new Subject(subjectuuidString);
        subject.setSubjectName(subjectname);
        subject.setSubjectCode(subjectcode);

        return subject;
    }
    public UUID getSubjectUUID(){

        String subjectuuidString = getString(getColumnIndex(StudentSubjectTable.Cols.SUBJECTUUID));

        return UUID.fromString(subjectuuidString);
    }
    public UUID getGroupUUID(){

        String groupuuidString = getString(getColumnIndex(GroupMemberTable.Cols.UUID));

        return UUID.fromString(groupuuidString);
    }
    public UUID getStudentUUID(){

        String studentuuidString = getString(getColumnIndex(GroupMemberTable.Cols.MEMBER));

        return UUID.fromString(studentuuidString);
    }
    public UUID getStudentUUIDfromSubject(){

        String studentuuidString = getString(getColumnIndex(StudentSubjectTable.Cols.STUDENTUUID));

        return UUID.fromString(studentuuidString);
    }

    //no grouplist
    public Project getProject(){
        String uuidString = getString(getColumnIndex(ProjectTable.Cols.PROJECTUUID));
        String assignmentname = getString(getColumnIndex(ProjectTable.Cols.ASIGNMENTNAME));
        String assignmentdetails = getString(getColumnIndex(ProjectTable.Cols.DETAILS));
        long datecreated = getLong(getColumnIndex(ProjectTable.Cols.DATECREATED));
        String subjectuuidString = getString(getColumnIndex(ProjectTable.Cols.SUBJECTUUID));

        Project project = new Project(UUID.fromString(uuidString), UUID.fromString(subjectuuidString));
        project.setAssignmentName(assignmentname);
        project.setDateCreated(new Date(datecreated));
        project.setAssignmentDetails(assignmentdetails);

        return project;
    }
}
