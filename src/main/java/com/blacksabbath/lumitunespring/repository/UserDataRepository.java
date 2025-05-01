package com.blacksabbath.lumitunespring.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.UserData;

public interface UserDataRepository extends JpaRepository<UserData, UUID> {
	public Optional<UserData> getByEmail(String email);
}
