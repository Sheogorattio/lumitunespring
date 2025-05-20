package com.blacksabbath.lumitunespring.mapper;

import com.blacksabbath.lumitunespring.dto.RegionDto;
import com.blacksabbath.lumitunespring.model.Region;

public class RegionMapper {

	public static RegionDto toDto(Region region) {
		RegionDto dto = new RegionDto();
		dto.setId(region.getId());
		dto.setName(region.getName());
		dto.setType(region.getType());
		dto.setParentId(region.getParent() != null ? region.getParent().getId() : null);
		return dto;
	}

	public static Region toEntity(RegionDto dto, Region parentRegion) {
		Region region = new Region();
		region.setId(dto.getId());
		region.setName(dto.getName());
		region.setType(dto.getType());
		region.setParent(parentRegion);
		return region;
	}
}
