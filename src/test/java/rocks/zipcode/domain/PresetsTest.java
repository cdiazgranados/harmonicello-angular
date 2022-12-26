package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class PresetsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presets.class);
        Presets presets1 = new Presets();
        presets1.setId(1L);
        Presets presets2 = new Presets();
        presets2.setId(presets1.getId());
        assertThat(presets1).isEqualTo(presets2);
        presets2.setId(2L);
        assertThat(presets1).isNotEqualTo(presets2);
        presets1.setId(null);
        assertThat(presets1).isNotEqualTo(presets2);
    }
}
