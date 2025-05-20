package com.blacksabbath.lumitunespring.dto;


import java.util.List;

import com.blacksabbath.lumitunespring.misc.Roles;

public class UserDto {
	public String id;
	public String username;
	public String password;
	public List<ImageDto> avatar;
	public Roles role;
	public int accSubscribers;
	public int accFollowings;
	public UserDataDto userData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<ImageDto> getAvatar() {
        return avatar;
    }

    public void setAvatar(List<ImageDto> avatar) {
        this.avatar = avatar;
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
    
    @Override
    public String toString() {
        return "UserDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role=" + role +
                ", accSubscribers=" + accSubscribers +
                ", accFollowings=" + accFollowings +
                ", userData=" + userData +
                '}';
    }

}
