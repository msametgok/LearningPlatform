package com.project.focustime.Models;

public class Category {
    private int id;
    private String title;
    private String thumbnail;
    private int numberOfCourses;

    public Category(int id, String title, String thumbnail, int numberOfCourses) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.numberOfCourses = numberOfCourses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getNumberOfCourses() {
        return numberOfCourses;
    }

    public void setNumberOfCourses(int numberOfCourses) {
        this.numberOfCourses = numberOfCourses;
    }

    @Override
    public String toString() {
        return title;
    }
}
