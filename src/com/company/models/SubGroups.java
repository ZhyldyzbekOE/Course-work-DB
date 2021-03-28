package com.company.models;

public class SubGroups {

    private String subGroupName;
    private int id_subgroups;

    public SubGroups(String subGroupNme, int id_subgroups) {
        this.subGroupName = subGroupNme;
        this.id_subgroups = id_subgroups;
    }

    public String getSubGroupName() {
        return subGroupName;
    }

    public void setSubGroupName(String subGroupName) {
        this.subGroupName = subGroupName;
    }

    public int getId_subgroups() {
        return id_subgroups;
    }

    public void setId_subgroups(int id_subgroups) {
        this.id_subgroups = id_subgroups;
    }
}
