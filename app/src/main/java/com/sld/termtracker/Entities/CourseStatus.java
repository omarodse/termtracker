package com.sld.termtracker.Entities;

public enum CourseStatus {

    SELECT_STATUS("Select status"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    DROPPED("Dropped"),
    PLAN_TO_TAKE("Plan to Take");

    private String displayName;

    CourseStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
