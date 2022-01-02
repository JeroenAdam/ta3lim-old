package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.UserExtended;
import com.adambahri.app.service.dto.UserExtendedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserExtended} and its DTO {@link UserExtendedDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserExtendedMapper extends EntityMapper<UserExtendedDTO, UserExtended> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UserExtendedDTO toDto(UserExtended s);
}
