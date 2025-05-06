package com.blacksabbath.lumitunespring.misc;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тіло запиту для логіну користувача")
public class LoginRequestBody {
	
	@Schema(description = "Ім'я користувача", example = "john_doe1")
	public String username;
	
	@Schema(description = "Пароль користувача", example = "pass1234")
	public String password;
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
