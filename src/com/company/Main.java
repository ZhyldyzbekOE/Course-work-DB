package com.company;

import com.company.services.StudSubjectServices;
import com.company.services.impl.StudSubjectServicesImpl;

public class Main {

    public static void main(String[] args) {

        StudSubjectServices studSubjectServices = new StudSubjectServicesImpl();
        studSubjectServices.connection();
//      studSubjectServices.addSubject();
        studSubjectServices.selectSubject();
       // studSubjectServices.addGroup();
        studSubjectServices.selectGroup();
       // studSubjectServices.addSubGroup();
        studSubjectServices.selectSubGroup();
        studSubjectServices.addStudent();
        studSubjectServices.selectStudent();
        studSubjectServices.close();
    }
}
