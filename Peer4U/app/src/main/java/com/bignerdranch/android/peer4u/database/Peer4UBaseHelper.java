package com.bignerdranch.android.peer4u.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.peer4u.Lecturer;
import com.bignerdranch.android.peer4u.Project;
import com.bignerdranch.android.peer4u.Student;
import com.bignerdranch.android.peer4u.Subject;
import com.bignerdranch.android.peer4u.database.Peer4UDbSchema.*;

public class Peer4UBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "peer4uBase.db";

    public Peer4UBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
    db.execSQL("create table " + StudentTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            StudentTable.Cols.UUID + ", " +
            StudentTable.Cols.FIRSTNAME + ", " +
            StudentTable.Cols.LASTNAME + ", " +
            StudentTable.Cols.INTAKE + ", " +
            StudentTable.Cols.PROGRAMME + ", " +
            StudentTable.Cols.UNIVERSITY + ", " +
            StudentTable.Cols.INTRODUCTION +
            ")");

        db.execSQL("create table " + LecturerTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                LecturerTable.Cols.UUID + ", " +
                LecturerTable.Cols.FIRSTNAME + ", " +
                LecturerTable.Cols.LASTNAME  +
                ")");
        db.execSQL("create table " + GroupMemberTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GroupMemberTable.Cols.UUID + ", " +
                GroupMemberTable.Cols.MEMBER +
                ")");
        db.execSQL("create table " + GroupTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                GroupTable.Cols.UUID + ", " +
                GroupTable.Cols.GROUPNAME + ", " +
                GroupTable.Cols.LEADER + ", " +
                GroupTable.Cols.PROJECTUUID +
                ")");
        db.execSQL("create table " + SubjectTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SubjectTable.Cols.UUID + ", " +
                SubjectTable.Cols.SUBJECTNAME + ", " +
                SubjectTable.Cols.SUBJECTCODE +
                ")");
        db.execSQL("create table " + LecturerSubjectTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                LecturerSubjectTable.Cols.LECTURERUUID + ", " +
                LecturerSubjectTable.Cols.SUBJECTUUID +
                ")");
        db.execSQL("create table " + StudentSubjectTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                StudentSubjectTable.Cols.STUDENTUUID + ", " +
                StudentSubjectTable.Cols.SUBJECTUUID +
                ")");
        db.execSQL("create table " + ProjectTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ProjectTable.Cols.PROJECTUUID + ", " +
                ProjectTable.Cols.ASIGNMENTNAME + ", " +
                ProjectTable.Cols.DETAILS + ", " +
                ProjectTable.Cols.DATECREATED + ", " +
                ProjectTable.Cols.SUBJECTUUID +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}

