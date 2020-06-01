package com.project.focustime.Models;

import java.util.List;

public class CourseDetails {
    private int courseId;
    private String courseIncludes;
    private String courseRequirements;
    private String courseOutcomes;
    //private List<Section> sections;
    private boolean isWishlisted;

    public CourseDetails(int courseId, String courseIncludes, String courseOutcomes, String courseRequirements, boolean isWishlisted) {
        this.courseId = courseId;
        this.courseIncludes = courseIncludes;
        this.courseRequirements = courseRequirements;
        this.courseOutcomes = courseOutcomes;
        //this.sections=sections;
        this.isWishlisted = isWishlisted;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseIncludes() {
        return courseIncludes;
    }

    public void setCourseIncludes(String courseIncludes) {
        this.courseIncludes = courseIncludes;
    }

    public String getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(String courseRequirements) {
        this.courseRequirements = courseRequirements;
    }

    public String getCourseOutcomes() {
        return courseOutcomes;
    }

    public void setCourseOutcomes(String courseOutcomes) {
        this.courseOutcomes = courseOutcomes;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

}
