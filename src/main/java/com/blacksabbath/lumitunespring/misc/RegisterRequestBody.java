package com.blacksabbath.lumitunespring.misc;

import java.time.LocalDate;

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

    // Внутрішній клас для userData
    @Schema(description = "Особисті дані користувача")
    public static class UserDataDto {

        @Schema(description = "Дата народження", example = "1990-01-01")
        private LocalDate birthDate;

        @Schema(description = "ID регіону", example = "region001")
        private String regionId;

        @Schema(description = "Чи є користувач артистом", example = "true")
        private Boolean isArtist;

        @Schema(description = "Email користувача", example = "john.doe@example.com")
        private String email;

        // Геттери і сеттери

        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

        public String getRegionId() { return regionId; }
        public void setRegionId(String regionId) { this.regionId = regionId; }

        public Boolean getIsArtist() { return isArtist; }
        public void setIsArtist(Boolean isArtist) { this.isArtist = isArtist; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
