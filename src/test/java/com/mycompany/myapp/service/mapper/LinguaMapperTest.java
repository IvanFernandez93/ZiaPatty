package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinguaMapperTest {

    private LinguaMapper linguaMapper;

    @BeforeEach
    public void setUp() {
        linguaMapper = new LinguaMapperImpl();
    }
}
