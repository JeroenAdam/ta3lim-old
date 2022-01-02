package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Message;
import com.adambahri.app.service.dto.MessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "receiver", source = "receiver", qualifiedByName = "login")
    @Mapping(target = "sender", source = "sender", qualifiedByName = "login")
    MessageDTO toDto(Message s);
}
