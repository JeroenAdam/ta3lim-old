package com.adambahri.app.service.mapper;

import com.adambahri.app.domain.Favorite;
import com.adambahri.app.service.dto.FavoriteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorite} and its DTO {@link FavoriteDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ResourceMapper.class })
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "resource", source = "resource")
    FavoriteDTO toDto(Favorite s);
}