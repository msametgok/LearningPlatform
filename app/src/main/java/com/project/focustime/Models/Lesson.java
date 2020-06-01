package com.project.focustime.Models;

public class Lesson {
    private int id;
    private String title;
   // private String duration;
    private int courseId;
    //private int sectionId;
    private String videoUrl;
    private String lessonType; //eklerken ''video'' yaz
    private int order;
    private String duration;
    private int isCompleted;
    private String videoType; // html mi youtube mu BEN YOUTUBE YAZIYORUM.
    //private String summary;
   // private String attachmentType;
   // private String attachment;
   // private String attachmentUrl;

    public Lesson(int id, String title, int courseId, String videoUrl, String lessonType, int order, String duration, int isCompleted,String videoType){
        this.title = title;

        this.courseId = courseId;

        this.videoUrl = videoUrl;
        this.lessonType = lessonType;
        this.order=order;
        this.duration=duration;
        this.isCompleted=isCompleted;
        this.videoType = videoType;

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

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
}
