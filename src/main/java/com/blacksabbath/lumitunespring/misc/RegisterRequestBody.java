package com.blacksabbath.lumitunespring.misc;

import java.time.LocalDate;
import java.util.UUID;

import com.blacksabbath.lumitunespring.dto.UserDataDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тіло запиту для реєстрації нового користувача")
public class RegisterRequestBody {
	@Schema(description = "Унікальне ім'я користувача", example = "john_doe1", required = true)
    private String username;

    @Schema(description = "Пароль користувача", example = "pass1234", required = true)
    private String password;

    @Schema(description = "ID аватара", example = "avatar123")
    private String avatarId;

    @Schema(description = "Роль користувача", example = "USER")
    private Roles role;
    
    @Schema(description = "Ідентифікатор регіона")
    private UUID regionId;

    @Schema(description = "Кількість підписників", example = "0")
    private int accSubscribers;

    @Schema(description = "Кількість підписок", example = "0")
    private int accFollowings;

    @Schema(description = "Особисті дані користувача")
    private UserDataDto userData;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAvatarId() { return avatarId; }
    public void setAvatarId(String avatarId) { this.avatarId = avatarId; }

    public Roles getRole() { return role; }
    public void setRole(Roles role) { this.role = role; }

    public int getAccSubscribers() { return accSubscribers; }
    public void setAccSubscribers(int accSubscribers) { this.accSubscribers = accSubscribers; }

    public int getAccFollowings() { return accFollowings; }
    public void setAccFollowings(int accFollowings) { this.accFollowings = accFollowings; }

    public UserDataDto getUserData() { return userData; }
    public void setUserData(UserDataDto userData) { this.userData = userData; }
    
    public UUID getRegionId() {return regionId;}
    public void setRegionId(UUID regionId) {this.regionId = regionId;}

}
