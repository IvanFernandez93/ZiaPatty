package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.service.dto.CategoriaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {
    @Mapping(target = "categorieFiglie", source = "categorieFiglie", qualifiedByName = "categoriaIdCategoria")
    CategoriaDTO toDto(Categoria s);

    @Named("categoriaIdCategoria")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "idCategoria", source = "idCategoria")
    CategoriaDTO toDtoCategoriaIdCategoria(Categoria categoria);
}
