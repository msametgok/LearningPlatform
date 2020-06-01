package com.project.focustime.Models;

public class User {
    private int userId;
    private String name;
    private String password;
    private String email;
    private String status; //Hesabı onayladı mı onaylamadı mı?
    private int validity;
    private String website;
    private String github;
    private String linkedin;
    private String description;
    private String user_photo;
    private String type;

    public User(int userId, String name, String password, String email, String status, int validity, String website,
                String github, String linkedin, String description, String user_photo,String type) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.status = status;
        this.validity = validity;
        this.website = website;
        this.github = github;
        this.linkedin = linkedin;
        this.description = description;
        this.user_photo = user_photo;
        this.type=type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
