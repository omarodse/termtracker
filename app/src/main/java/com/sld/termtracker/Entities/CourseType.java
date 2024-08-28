package com.sld.termtracker.Entities;

public enum CourseType {
    ONLINE_COURSE("Online Course"),
    OFFLINE_COURSE("Offline Course");
    private String displayCourseName;

    CourseType(String displayCourseName) {this.displayCourseName = displayCourseName;}

    @Override
    public String toString() {
        return "CourseType{" +
                "displayCourseName='" + displayCourseName + '\'' +
                '}';
    }
}
