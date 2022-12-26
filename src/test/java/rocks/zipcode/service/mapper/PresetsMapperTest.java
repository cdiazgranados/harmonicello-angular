package rocks.zipcode.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PresetsMapperTest {

    private PresetsMapper presetsMapper;

    @BeforeEach
    public void setUp() {
        presetsMapper = new PresetsMapperImpl();
    }
}
