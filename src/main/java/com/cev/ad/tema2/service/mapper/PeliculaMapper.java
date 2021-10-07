package com.cev.ad.tema2.service.mapper;

import com.cev.ad.tema2.domain.*;
import com.cev.ad.tema2.service.dto.PeliculaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pelicula} and its DTO {@link PeliculaDTO}.
 */
@Mapper(componentModel = "spring", uses = { EstrenoMapper.class })
public interface PeliculaMapper extends EntityMapper<PeliculaDTO, Pelicula> {
    @Mapping(target = "estreno", source = "estreno", qualifiedByName = "fechaEstreno")
    PeliculaDTO toDto(Pelicula s);

    @Named("titulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "estreno", source = "estreno")
    @Mapping(target = "titulo", source = "titulo")
    PeliculaDTO toDtoTitulo(Pelicula pelicula);
}
