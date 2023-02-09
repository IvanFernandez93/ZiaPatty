package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Frase;
import com.mycompany.myapp.domain.Lingua;
import com.mycompany.myapp.service.dto.FraseDTO;
import com.mycompany.myapp.service.dto.LinguaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Frase} and its DTO {@link FraseDTO}.
 */
@Mapper(componentModel = "spring")
public interface FraseMapper extends EntityMapper<FraseDTO, Frase> {
    @Mapping(target = "lingua", source = "lingua", qualifiedByName = "linguaIdLingua")
    FraseDTO toDto(Frase s);

    @Named("linguaIdLingua")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idLingua", source = "idLingua")
    LinguaDTO toDtoLinguaIdLingua(Lingua lingua);
}
