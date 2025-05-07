package com.blacksabbath.lumitunespring.dto;

import java.util.UUID;

import com.blacksabbath.lumitunespring.misc.Roles;

public class UserDto {
    private UUID id;
    private String username;
    private String password;
    private String avatarId;
    private Roles role;
    private int accSubscribers;
    private int accFollowings;
    private UserDataDto userData;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getAccSubscribers() {
        return accSubscribers;
    }

    public void setAccSubscribers(int accSubscribers) {
        this.accSubscribers = accSubscribers;
    }

    public int getAccFollowings() {
        return accFollowings;
    }

    public void setAccFollowings(int accFollowings) {
        this.accFollowings = accFollowings;
    }

    public UserDataDto getUserData() {
        return userData;
    }

    public void setUserData(UserDataDto userData) {
        this.userData = userData;
    }
}
