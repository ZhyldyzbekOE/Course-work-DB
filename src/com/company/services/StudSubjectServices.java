package com.company.services;

public interface StudSubjectServices {


    void connection();
    void close();

    void addSubject();
    void selectSubject();

    void addGroup();
    void selectGroup();

    void addSubGroup();
    void selectSubGroup();

    void addStudent();
    void selectStudent();

    void fill_group_student_subject();

    void addAttendance();

}
