package com.bignerdranch.android.peer4u.database;

public class Peer4UDbSchema {

    public static final class StudentTable {
        public static final String NAME = "student";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIRSTNAME = "firstname";
            public static final String LASTNAME = "lastname";
            public static final String INTAKE = "intake";
            public static final String PROGRAMME = "programme";
            public static final String UNIVERSITY = "university";
            public static final String INTRODUCTION = "introduction";
        }
    }
    public static final class LecturerTable {
        public static final String NAME = "lecturer";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String FIRSTNAME = "firstname";
            public static final String LASTNAME = "lastname";
        }
    }
    public static final class GroupMemberTable {
        public static final String NAME = "groupMember";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String MEMBER = "memberId";
        }
    }
    public static final class GroupTable {
        public static final String NAME = "groupTable";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String GROUPNAME = "groupname";
            public static final String LEADER = "leaderId";
            public static final String PROJECTUUID = "projectuuid";

        }
    }
    public static final class SubjectTable {
        public static final String NAME = "subject";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String SUBJECTNAME = "subjectname";
            public static final String SUBJECTCODE = "subjectcode";
        }
    }

    public static final class LecturerSubjectTable {
        public static final String NAME = "lecturersubject";

        public static final class Cols {
            public static final String LECTURERUUID = "lecturerUUID";
            public static final String SUBJECTUUID = "subjectUUID";

        }
    }
    public static final class StudentSubjectTable {
        public static final String NAME = "studentsubject";

        public static final class Cols {
            public static final String STUDENTUUID = "studentUUID";
            public static final String SUBJECTUUID = "subjectUUID";

        }
    }

    public static final class ProjectTable {
        public static final String NAME = "project";

        public static final class Cols {

            public static final String PROJECTUUID = "projectUUID";
            public static final String ASIGNMENTNAME = "assignmentname";
            public static final String DETAILS = "details";
            public static final String DATECREATED = "datecreated";
            public static final String SUBJECTUUID = "subjectUUID";
        }
    }
}
