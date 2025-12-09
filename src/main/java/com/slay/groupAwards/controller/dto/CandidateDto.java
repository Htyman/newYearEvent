package com.slay.groupAwards.controller.dto;

public class CandidateDto {

    private Integer id;
    private String name;
    private String surname;
    private String photoUrl;
    private String description;

    public CandidateDto(Integer id, String name, String surname, String photoUrl, String description) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.photoUrl = photoUrl;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getDescription() {
        return description;
    }
}
