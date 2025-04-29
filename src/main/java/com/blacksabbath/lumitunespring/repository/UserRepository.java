package com.blacksabbath.lumitunespring.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}