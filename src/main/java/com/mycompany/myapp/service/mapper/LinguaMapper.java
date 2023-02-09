package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Lingua;
import com.mycompany.myapp.service.dto.LinguaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lingua} and its DTO {@link LinguaDTO}.
 */
@Mapper(componentModel = "spring")
public interface LinguaMapper extends EntityMapper<LinguaDTO, Lingua> {}
