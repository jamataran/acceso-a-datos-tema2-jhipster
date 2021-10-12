package com.cev.ad.tema2.service.mapper;

import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cev.ad.tema2.domain.Pelicula;
import com.cev.ad.tema2.service.dto.PeliculaDTO;

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

    @Named("tituloSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "estreno", source = "estreno")
    @Mapping(target = "titulo", source = "titulo")
    Set<PeliculaDTO> toDtoTituloSet(Set<Pelicula> pelicula);
}
