package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Audio;
import com.mycompany.myapp.service.dto.AudioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Audio} and its DTO {@link AudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface AudioMapper extends EntityMapper<AudioDTO, Audio> {}
