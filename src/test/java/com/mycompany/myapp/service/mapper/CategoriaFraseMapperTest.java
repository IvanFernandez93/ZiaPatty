package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaFraseMapperTest {

    private CategoriaFraseMapper categoriaFraseMapper;

    @BeforeEach
    public void setUp() {
        categoriaFraseMapper = new CategoriaFraseMapperImpl();
    }
}
