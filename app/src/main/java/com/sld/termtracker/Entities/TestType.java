package com.sld.termtracker.Entities;

public enum TestType {

    PERFORMANCE_ASSESSMENT("Performance Assessment"),
    OBJECTIVE_ASSESSMENT("Objective Assessment");

    private String displayName;

    TestType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

}
