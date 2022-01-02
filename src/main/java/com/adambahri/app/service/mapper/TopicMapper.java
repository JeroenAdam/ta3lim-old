package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Topic;
import com.adambahri.app.service.dto.TopicDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Topic} and its DTO {@link TopicDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {
    @Named("labelSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "label", source = "label")
    Set<TopicDTO> toDtoLabelSet(Set<Topic> topic);
}
