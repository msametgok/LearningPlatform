package com.project.focustime.Models;

import java.io.Serializable;

public class MyCourse implements Serializable {
    private int id;
    private String title;
    private String thumbnail;
    private String price;
    private String instructor;
    private int rating;
    private int totalNumberRating;
    private int numberOfEnrollment;
    private int courseCompletion;
    private int totalNumberOfLessons;
    private int totalNumberOfCompletedLessons;
    private String shareableLink;


    public MyCourse(int id, String title, String thumbnail, String price, String instructor, int rating, int totalNumberRating, int numberOfEnrollment, int courseCompletion, int totalNumberOfLessons, int totalNumberOfCompletedLessons, String shareableLink) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.price = price;
        this.instructor = instructor;
        this.rating = rating;
        this.totalNumberRating = totalNumberRating;
        this.numberOfEnrollment = numberOfEnrollment;
        this.courseCompletion = courseCompletion;
        this.totalNumberOfLessons = totalNumberOfLessons;
        this.totalNumberOfCompletedLessons = totalNumberOfCompletedLessons;
        this.shareableLink = shareableLink;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTotalNumberRating() {
        return totalNumberRating;
    }

    public void setTotalNumberRating(int totalNumberRating) {
        this.totalNumberRating = totalNumberRating;
    }

    public int getNumberOfEnrollment() {
        return numberOfEnrollment;
    }

    public void setNumberOfEnrollment(int numberOfEnrollment) {
        this.numberOfEnrollment = numberOfEnrollment;
    }

    public int getCourseCompletion() {
        return courseCompletion;
    }

    public void setCourseCompletion(int courseCompletion) {
        this.courseCompletion = courseCompletion;
    }

    public int getTotalNumberOfLessons() {
        return totalNumberOfLessons;
    }

    public void setTotalNumberOfLessons(int totalNumberOfLessons) {
        this.totalNumberOfLessons = totalNumberOfLessons;
    }

    public int getTotalNumberOfCompletedLessons() {
        return totalNumberOfCompletedLessons;
    }

    public void setTotalNumberOfCompletedLessons(int totalNumberOfCompletedLessons) {
        this.totalNumberOfCompletedLessons = totalNumberOfCompletedLessons;
    }

    public String getShareableLink() {
        return shareableLink;
    }

    public void setShareableLink(String shareableLink) {
        this.shareableLink = shareableLink;
    }


}
