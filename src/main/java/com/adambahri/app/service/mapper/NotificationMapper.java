package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Notification;
import com.adambahri.app.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    NotificationDTO toDto(Notification s);
}
