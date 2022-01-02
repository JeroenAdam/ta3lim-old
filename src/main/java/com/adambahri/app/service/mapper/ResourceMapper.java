package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Resource;
import com.adambahri.app.service.dto.ResourceDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Resource} and its DTO {@link ResourceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, SubjectMapper.class, TopicMapper.class, SkillMapper.class })
public interface ResourceMapper extends EntityMapper<ResourceDTO, Resource> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "subject", source = "subject", qualifiedByName = "label")
    @Mapping(target = "topics", source = "topics", qualifiedByName = "labelSet")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "labelSet")
    ResourceDTO toDto(Resource s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ResourceDTO toDtoId(Resource resource);

    @Mapping(target = "removeTopics", ignore = true)
    @Mapping(target = "removeSkills", ignore = true)
    Resource toEntity(ResourceDTO resourceDTO);
}
