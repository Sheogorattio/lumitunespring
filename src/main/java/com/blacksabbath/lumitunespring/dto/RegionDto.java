package com.blacksabbath.lumitunespring.dto;

import java.util.UUID;

import com.blacksabbath.lumitunespring.misc.RegionType;

public class RegionDto {

	private UUID id;
	private String name;
	private RegionType type;
	private UUID parent_id;
	
	public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegionType getType() {
        return type;
    }

    public void setType(RegionType type) {
        this.type = type;
    }

    public UUID getParentId() {
        return parent_id;
    }

    public void setParentId(UUID parent_id) {
        this.parent_id = parent_id;
    }
}
