package com.blacksabbath.lumitunespring.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blacksabbath.lumitunespring.dto.RegionDto;
import com.blacksabbath.lumitunespring.mapper.RegionMapper;
import com.blacksabbath.lumitunespring.misc.RegionType;
import com.blacksabbath.lumitunespring.model.Region;
import com.blacksabbath.lumitunespring.repository.RegionRepository;

@Service
public class RegionService {

	@Autowired
	RegionRepository repository;

	public List<RegionDto> getAllCountries() {
		return repository.findByType(RegionType.COUNTRY).stream().map(RegionMapper::toDto).collect(Collectors.toList());
	}

	public List<RegionDto> getAllCities() {
		return repository.findByType(RegionType.CITY).stream().map(RegionMapper::toDto).collect(Collectors.toList());
	}

	public Optional<RegionDto> getById(String id) {
		return repository.findById(UUID.fromString(id)).map(RegionMapper::toDto);
	}

	public List<RegionDto> getRegionsByParentId(UUID parentId) {
		return repository.findByParentId(parentId).stream().map(RegionMapper::toDto).collect(Collectors.toList());
	}

	public RegionDto save(RegionDto dto) {
		Region parentRegion = null;
		if (dto.getParentId() != null) {
			parentRegion = repository.findById(dto.getParentId()).orElse(null);
		}

		Region region = RegionMapper.toEntity(dto, parentRegion);
		return RegionMapper.toDto(repository.save(region));
	}

	public void delete(RegionDto dto) {
		Region parentRegion = null;
		if (dto.getParentId() != null) {
			parentRegion = repository.findById(dto.getId()).orElse(null);
		}
		Region region = RegionMapper.toEntity(dto, parentRegion);
		repository.delete(region);
	}

	public Optional<RegionDto> update(RegionDto dto) {
		Region region = repository.findById(dto.getId()).orElse(null);

		if (dto == null || region == null) {
			return Optional.empty();
		}

		Region parentRegion = null;

		if (dto.getParentId() != null) {
			parentRegion = repository.findById(dto.getParentId()).orElse(null);
		}

		region.setName(dto.getName());
		region.setParent(parentRegion);
		region.setType(dto.getType());

		return Optional.of(RegionMapper.toDto(region));
	}
}
