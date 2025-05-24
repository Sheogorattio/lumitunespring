package com.blacksabbath.lumitunespring.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Album;

public interface AlbumRepository extends JpaRepository<Album, UUID> {

}
