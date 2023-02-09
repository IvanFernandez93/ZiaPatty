package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FraseMapperTest {

    private FraseMapper fraseMapper;

    @BeforeEach
    public void setUp() {
        fraseMapper = new FraseMapperImpl();
    }
}
