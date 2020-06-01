package com.project.focustime.Models;

import java.io.Serializable;
import java.util.List;

public class Course implements Serializable {

    private int id;
    private String instructor;
    private String category;
    private String title;
    private String url;
    private String shortDescription;
    private String description;
    private String requirements;
    private String outcomes;
    private String thumbnail;
    private String level;
    private String price;
    private int rating;
    private int totalNumberRating;
    private int numberOfEnrollment;
    private String language;
    private String firstLesson;

    public Course(int id, String instructor, String category, String title, String url, String shortDescription, String description, String requirements,
                  String outcomes,String thumbnail, String level, String price, int rating, int totalNumberRating, int numberOfEnrollment
                ,String language,String firstLesson) {

        this.id = id;
        this.instructor = instructor;
        this.category = category;
        this.title = title;
        this.url = url;
        this.shortDescription = shortDescription;
        this.description = description;
        this.requirements = requirements;
        this.outcomes=outcomes;
        this.thumbnail = thumbnail;
        this.level = level;
        this.price = price;
        this.rating = rating;
        this.totalNumberRating=totalNumberRating;
        this.numberOfEnrollment=numberOfEnrollment;
        this.language = language;
        this.firstLesson=firstLesson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(String outcomes) {
        this.outcomes = outcomes;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFirstLesson() {
        return firstLesson;
    }

    public void setFirstLesson(String firstLesson) {
        this.firstLesson = firstLesson;
    }
}
