package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Audio;
import com.mycompany.myapp.domain.Frase;
import com.mycompany.myapp.domain.FraseAudio;
import com.mycompany.myapp.service.dto.AudioDTO;
import com.mycompany.myapp.service.dto.FraseAudioDTO;
import com.mycompany.myapp.service.dto.FraseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FraseAudio} and its DTO {@link FraseAudioDTO}.
 */
@Mapper(componentModel = "spring")
public interface FraseAudioMapper extends EntityMapper<FraseAudioDTO, FraseAudio> {
    @Mapping(target = "frase", source = "frase", qualifiedByName = "fraseId")
    @Mapping(target = "audio", source = "audio", qualifiedByName = "audioIdAudio")
    FraseAudioDTO toDto(FraseAudio s);

    @Named("fraseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FraseDTO toDtoFraseId(Frase frase);

    @Named("audioIdAudio")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idAudio", source = "idAudio")
    AudioDTO toDtoAudioIdAudio(Audio audio);
}
