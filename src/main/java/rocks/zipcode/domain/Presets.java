package rocks.zipcode.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import rocks.zipcode.domain.enumeration.Instrument;
import rocks.zipcode.domain.enumeration.Waveform;

/**
 * A Presets.
 */
@Entity
@Table(name = "presets")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Presets implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "preset_title")
    private String presetTitle;

    @Column(name = "hertz")
    private Integer hertz;

    @Column(name = "sustain")
    private Boolean sustain;

    @Enumerated(EnumType.STRING)
    @Column(name = "waveform")
    private Waveform waveform;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument")
    private Instrument instrument;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Presets id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresetTitle() {
        return this.presetTitle;
    }

    public Presets presetTitle(String presetTitle) {
        this.setPresetTitle(presetTitle);
        return this;
    }

    public void setPresetTitle(String presetTitle) {
        this.presetTitle = presetTitle;
    }

    public Integer getHertz() {
        return this.hertz;
    }

    public Presets hertz(Integer hertz) {
        this.setHertz(hertz);
        return this;
    }

    public void setHertz(Integer hertz) {
        this.hertz = hertz;
    }

    public Boolean getSustain() {
        return this.sustain;
    }

    public Presets sustain(Boolean sustain) {
        this.setSustain(sustain);
        return this;
    }

    public void setSustain(Boolean sustain) {
        this.sustain = sustain;
    }

    public Waveform getWaveform() {
        return this.waveform;
    }

    public Presets waveform(Waveform waveform) {
        this.setWaveform(waveform);
        return this;
    }

    public void setWaveform(Waveform waveform) {
        this.waveform = waveform;
    }

    public Instrument getInstrument() {
        return this.instrument;
    }

    public Presets instrument(Instrument instrument) {
        this.setInstrument(instrument);
        return this;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Presets user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presets)) {
            return false;
        }
        return id != null && id.equals(((Presets) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presets{" +
            "id=" + getId() +
            ", presetTitle='" + getPresetTitle() + "'" +
            ", hertz=" + getHertz() +
            ", sustain='" + getSustain() + "'" +
            ", waveform='" + getWaveform() + "'" +
            ", instrument='" + getInstrument() + "'" +
            "}";
    }
}
