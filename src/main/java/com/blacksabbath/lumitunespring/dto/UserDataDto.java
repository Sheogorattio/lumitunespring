package com.blacksabbath.lumitunespring.dto;

import java.time.LocalDate;

public class UserDataDto {
    private LocalDate birthDate;
    private String regionId;
    private Boolean isArtist;
    private String email;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Boolean getIsArtist() {
        return isArtist;
    }

    public void setIsArtist(Boolean isArtist) {
        this.isArtist = isArtist;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
