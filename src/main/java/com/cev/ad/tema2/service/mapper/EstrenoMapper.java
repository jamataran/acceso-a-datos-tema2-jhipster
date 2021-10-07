package com.cev.ad.tema2.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cev.ad.tema2.domain.Estreno;
import com.cev.ad.tema2.service.dto.EstrenoDTO;

/**
 * Mapper for the entity {@link Estreno} and its DTO {@link EstrenoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EstrenoMapper extends EntityMapper<EstrenoDTO, Estreno> {
    @Named("fechaEstreno")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fechaEstreno", source = "fechaEstreno")
    EstrenoDTO toDtoFechaEstreno(Estreno estreno);
}
