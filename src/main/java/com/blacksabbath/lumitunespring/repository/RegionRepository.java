package com.blacksabbath.lumitunespring.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blacksabbath.lumitunespring.misc.RegionType;
import com.blacksabbath.lumitunespring.model.Region;

public interface RegionRepository  extends JpaRepository<Region, UUID> {
	List<Region> findByType(RegionType type);
	List<Region> findByParentId(UUID parentId);
}
