package com.company.models;

public class Student {

    private String firstName;
    private String secondName;
    private int id_subgroup;

    public Student(String firstName, String secondName, int id_subgroup) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.id_subgroup = id_subgroup;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getId_subgroup() {
        return id_subgroup;
    }

    public void setId_subgroup(int id_subgroup) {
        this.id_subgroup = id_subgroup;
    }
}
