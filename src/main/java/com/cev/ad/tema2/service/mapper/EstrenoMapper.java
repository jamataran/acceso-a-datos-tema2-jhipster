package com.cev.ad.tema2.service.mapper;

import com.cev.ad.tema2.domain.*;
import com.cev.ad.tema2.service.dto.EstrenoDTO;
import org.mapstruct.*;

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
