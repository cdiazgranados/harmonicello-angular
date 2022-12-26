package rocks.zipcode.service.dto;

import java.io.Serializable;
import java.util.Objects;
import rocks.zipcode.domain.enumeration.Instrument;
import rocks.zipcode.domain.enumeration.Waveform;

/**
 * A DTO for the {@link rocks.zipcode.domain.Presets} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PresetsDTO implements Serializable {

    private Long id;

    private String presetTitle;

    private Integer hertz;

    private Boolean sustain;

    private Waveform waveform;

    private Instrument instrument;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresetTitle() {
        return presetTitle;
    }

    public void setPresetTitle(String presetTitle) {
        this.presetTitle = presetTitle;
    }

    public Integer getHertz() {
        return hertz;
    }

    public void setHertz(Integer hertz) {
        this.hertz = hertz;
    }

    public Boolean getSustain() {
        return sustain;
    }

    public void setSustain(Boolean sustain) {
        this.sustain = sustain;
    }

    public Waveform getWaveform() {
        return waveform;
    }

    public void setWaveform(Waveform waveform) {
        this.waveform = waveform;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresetsDTO)) {
            return false;
        }

        PresetsDTO presetsDTO = (PresetsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, presetsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresetsDTO{" +
            "id=" + getId() +
            ", presetTitle='" + getPresetTitle() + "'" +
            ", hertz=" + getHertz() +
            ", sustain='" + getSustain() + "'" +
            ", waveform='" + getWaveform() + "'" +
            ", instrument='" + getInstrument() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
