package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Skill;
import com.adambahri.app.service.dto.SkillDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Named("labelSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "label", source = "label")
    Set<SkillDTO> toDtoLabelSet(Set<Skill> skill);
}
