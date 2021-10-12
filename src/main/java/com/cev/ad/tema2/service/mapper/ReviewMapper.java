package com.cev.ad.tema2.service.mapper;

import com.cev.ad.tema2.domain.*;
import com.cev.ad.tema2.service.dto.ReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring", uses = { PeliculaMapper.class })
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {
    @Mapping(target = "pelicula", source = "pelicula", qualifiedByName = "titulo")
    ReviewDTO toDto(Review s);
}
