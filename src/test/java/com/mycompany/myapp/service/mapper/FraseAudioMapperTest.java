package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FraseAudioMapperTest {

    private FraseAudioMapper fraseAudioMapper;

    @BeforeEach
    public void setUp() {
        fraseAudioMapper = new FraseAudioMapperImpl();
    }
}
