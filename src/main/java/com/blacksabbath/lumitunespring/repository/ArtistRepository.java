package com.blacksabbath.lumitunespring.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.model.Artist;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {

}
