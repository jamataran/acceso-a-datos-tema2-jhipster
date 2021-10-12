package com.cev.ad.tema2.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cev.ad.tema2.domain.Actor;
import com.cev.ad.tema2.service.dto.ActorDTO;

/**
 * Mapper for the entity {@link Actor} and its DTO {@link ActorDTO}.
 */
@Mapper(componentModel = "spring", uses = { PeliculaMapper.class })
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {
    @Mapping(target = "peliculas", source = "peliculas", qualifiedByName = "tituloSet")
    ActorDTO toDto(Actor s);

    @Mapping(target = "removePelicula", ignore = true)
    Actor toEntity(ActorDTO actorDTO);
}
