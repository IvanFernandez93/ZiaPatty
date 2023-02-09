package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.CategoriaFrase;
import com.mycompany.myapp.domain.Frase;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import com.mycompany.myapp.service.dto.CategoriaFraseDTO;
import com.mycompany.myapp.service.dto.FraseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaFrase} and its DTO {@link CategoriaFraseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaFraseMapper extends EntityMapper<CategoriaFraseDTO, CategoriaFrase> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaIdCategoria")
    @Mapping(target = "frase", source = "frase", qualifiedByName = "fraseId")
    CategoriaFraseDTO toDto(CategoriaFrase s);

    @Named("categoriaIdCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idCategoria", source = "idCategoria")
    CategoriaDTO toDtoCategoriaIdCategoria(Categoria categoria);

    @Named("fraseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FraseDTO toDtoFraseId(Frase frase);
}
