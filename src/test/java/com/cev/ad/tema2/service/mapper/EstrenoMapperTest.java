package com.cev.ad.tema2.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstrenoMapperTest {

    private EstrenoMapper estrenoMapper;

    @BeforeEach
    public void setUp() {
        estrenoMapper = new EstrenoMapperImpl();
    }
}
