package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Votes;
import com.adambahri.app.service.dto.VotesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Votes} and its DTO {@link VotesDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ResourceMapper.class })
public interface VotesMapper extends EntityMapper<VotesDTO, Votes> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "resource", source = "resource", qualifiedByName = "id")
    VotesDTO toDto(Votes s);
}
