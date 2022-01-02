package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.File;
import com.adambahri.app.service.dto.FileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring", uses = { ResourceMapper.class })
public interface FileMapper extends EntityMapper<FileDTO, File> {
    @Mapping(target = "resource", source = "resource", qualifiedByName = "id")
    FileDTO toDto(File s);
}
