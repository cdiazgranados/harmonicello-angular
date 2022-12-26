package rocks.zipcode.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class PresetsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PresetsDTO.class);
        PresetsDTO presetsDTO1 = new PresetsDTO();
        presetsDTO1.setId(1L);
        PresetsDTO presetsDTO2 = new PresetsDTO();
        assertThat(presetsDTO1).isNotEqualTo(presetsDTO2);
        presetsDTO2.setId(presetsDTO1.getId());
        assertThat(presetsDTO1).isEqualTo(presetsDTO2);
        presetsDTO2.setId(2L);
        assertThat(presetsDTO1).isNotEqualTo(presetsDTO2);
        presetsDTO1.setId(null);
        assertThat(presetsDTO1).isNotEqualTo(presetsDTO2);
    }
}
