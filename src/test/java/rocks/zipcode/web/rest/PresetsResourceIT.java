package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Presets;
import rocks.zipcode.domain.enumeration.Instrument;
import rocks.zipcode.domain.enumeration.Waveform;
import rocks.zipcode.repository.PresetsRepository;
import rocks.zipcode.service.PresetsService;
import rocks.zipcode.service.dto.PresetsDTO;
import rocks.zipcode.service.mapper.PresetsMapper;

/**
 * Integration tests for the {@link PresetsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PresetsResourceIT {

    private static final String DEFAULT_PRESET_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_PRESET_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HERTZ = 1;
    private static final Integer UPDATED_HERTZ = 2;

    private static final Boolean DEFAULT_SUSTAIN = false;
    private static final Boolean UPDATED_SUSTAIN = true;

    private static final Waveform DEFAULT_WAVEFORM = Waveform.SINE;
    private static final Waveform UPDATED_WAVEFORM = Waveform.TRIANGLE;

    private static final Instrument DEFAULT_INSTRUMENT = Instrument.VIOLIN;
    private static final Instrument UPDATED_INSTRUMENT = Instrument.VIOLA;

    private static final String ENTITY_API_URL = "/api/presets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresetsRepository presetsRepository;

    @Mock
    private PresetsRepository presetsRepositoryMock;

    @Autowired
    private PresetsMapper presetsMapper;

    @Mock
    private PresetsService presetsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresetsMockMvc;

    private Presets presets;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presets createEntity(EntityManager em) {
        Presets presets = new Presets()
            .presetTitle(DEFAULT_PRESET_TITLE)
            .hertz(DEFAULT_HERTZ)
            .sustain(DEFAULT_SUSTAIN)
            .waveform(DEFAULT_WAVEFORM)
            .instrument(DEFAULT_INSTRUMENT);
        return presets;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presets createUpdatedEntity(EntityManager em) {
        Presets presets = new Presets()
            .presetTitle(UPDATED_PRESET_TITLE)
            .hertz(UPDATED_HERTZ)
            .sustain(UPDATED_SUSTAIN)
            .waveform(UPDATED_WAVEFORM)
            .instrument(UPDATED_INSTRUMENT);
        return presets;
    }

    @BeforeEach
    public void initTest() {
        presets = createEntity(em);
    }

    @Test
    @Transactional
    void createPresets() throws Exception {
        int databaseSizeBeforeCreate = presetsRepository.findAll().size();
        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);
        restPresetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presetsDTO)))
            .andExpect(status().isCreated());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeCreate + 1);
        Presets testPresets = presetsList.get(presetsList.size() - 1);
        assertThat(testPresets.getPresetTitle()).isEqualTo(DEFAULT_PRESET_TITLE);
        assertThat(testPresets.getHertz()).isEqualTo(DEFAULT_HERTZ);
        assertThat(testPresets.getSustain()).isEqualTo(DEFAULT_SUSTAIN);
        assertThat(testPresets.getWaveform()).isEqualTo(DEFAULT_WAVEFORM);
        assertThat(testPresets.getInstrument()).isEqualTo(DEFAULT_INSTRUMENT);
    }

    @Test
    @Transactional
    void createPresetsWithExistingId() throws Exception {
        // Create the Presets with an existing ID
        presets.setId(1L);
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        int databaseSizeBeforeCreate = presetsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresetsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presetsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPresets() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        // Get all the presetsList
        restPresetsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presets.getId().intValue())))
            .andExpect(jsonPath("$.[*].presetTitle").value(hasItem(DEFAULT_PRESET_TITLE)))
            .andExpect(jsonPath("$.[*].hertz").value(hasItem(DEFAULT_HERTZ)))
            .andExpect(jsonPath("$.[*].sustain").value(hasItem(DEFAULT_SUSTAIN.booleanValue())))
            .andExpect(jsonPath("$.[*].waveform").value(hasItem(DEFAULT_WAVEFORM.toString())))
            .andExpect(jsonPath("$.[*].instrument").value(hasItem(DEFAULT_INSTRUMENT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(presetsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPresetsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(presetsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPresetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(presetsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPresetsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(presetsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPresets() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        // Get the presets
        restPresetsMockMvc
            .perform(get(ENTITY_API_URL_ID, presets.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presets.getId().intValue()))
            .andExpect(jsonPath("$.presetTitle").value(DEFAULT_PRESET_TITLE))
            .andExpect(jsonPath("$.hertz").value(DEFAULT_HERTZ))
            .andExpect(jsonPath("$.sustain").value(DEFAULT_SUSTAIN.booleanValue()))
            .andExpect(jsonPath("$.waveform").value(DEFAULT_WAVEFORM.toString()))
            .andExpect(jsonPath("$.instrument").value(DEFAULT_INSTRUMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPresets() throws Exception {
        // Get the presets
        restPresetsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPresets() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();

        // Update the presets
        Presets updatedPresets = presetsRepository.findById(presets.getId()).get();
        // Disconnect from session so that the updates on updatedPresets are not directly saved in db
        em.detach(updatedPresets);
        updatedPresets
            .presetTitle(UPDATED_PRESET_TITLE)
            .hertz(UPDATED_HERTZ)
            .sustain(UPDATED_SUSTAIN)
            .waveform(UPDATED_WAVEFORM)
            .instrument(UPDATED_INSTRUMENT);
        PresetsDTO presetsDTO = presetsMapper.toDto(updatedPresets);

        restPresetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
        Presets testPresets = presetsList.get(presetsList.size() - 1);
        assertThat(testPresets.getPresetTitle()).isEqualTo(UPDATED_PRESET_TITLE);
        assertThat(testPresets.getHertz()).isEqualTo(UPDATED_HERTZ);
        assertThat(testPresets.getSustain()).isEqualTo(UPDATED_SUSTAIN);
        assertThat(testPresets.getWaveform()).isEqualTo(UPDATED_WAVEFORM);
        assertThat(testPresets.getInstrument()).isEqualTo(UPDATED_INSTRUMENT);
    }

    @Test
    @Transactional
    void putNonExistingPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presetsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(presetsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresetsWithPatch() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();

        // Update the presets using partial update
        Presets partialUpdatedPresets = new Presets();
        partialUpdatedPresets.setId(presets.getId());

        partialUpdatedPresets.hertz(UPDATED_HERTZ).sustain(UPDATED_SUSTAIN).waveform(UPDATED_WAVEFORM).instrument(UPDATED_INSTRUMENT);

        restPresetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresets))
            )
            .andExpect(status().isOk());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
        Presets testPresets = presetsList.get(presetsList.size() - 1);
        assertThat(testPresets.getPresetTitle()).isEqualTo(DEFAULT_PRESET_TITLE);
        assertThat(testPresets.getHertz()).isEqualTo(UPDATED_HERTZ);
        assertThat(testPresets.getSustain()).isEqualTo(UPDATED_SUSTAIN);
        assertThat(testPresets.getWaveform()).isEqualTo(UPDATED_WAVEFORM);
        assertThat(testPresets.getInstrument()).isEqualTo(UPDATED_INSTRUMENT);
    }

    @Test
    @Transactional
    void fullUpdatePresetsWithPatch() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();

        // Update the presets using partial update
        Presets partialUpdatedPresets = new Presets();
        partialUpdatedPresets.setId(presets.getId());

        partialUpdatedPresets
            .presetTitle(UPDATED_PRESET_TITLE)
            .hertz(UPDATED_HERTZ)
            .sustain(UPDATED_SUSTAIN)
            .waveform(UPDATED_WAVEFORM)
            .instrument(UPDATED_INSTRUMENT);

        restPresetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresets.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresets))
            )
            .andExpect(status().isOk());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
        Presets testPresets = presetsList.get(presetsList.size() - 1);
        assertThat(testPresets.getPresetTitle()).isEqualTo(UPDATED_PRESET_TITLE);
        assertThat(testPresets.getHertz()).isEqualTo(UPDATED_HERTZ);
        assertThat(testPresets.getSustain()).isEqualTo(UPDATED_SUSTAIN);
        assertThat(testPresets.getWaveform()).isEqualTo(UPDATED_WAVEFORM);
        assertThat(testPresets.getInstrument()).isEqualTo(UPDATED_INSTRUMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presetsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresets() throws Exception {
        int databaseSizeBeforeUpdate = presetsRepository.findAll().size();
        presets.setId(count.incrementAndGet());

        // Create the Presets
        PresetsDTO presetsDTO = presetsMapper.toDto(presets);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresetsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(presetsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Presets in the database
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresets() throws Exception {
        // Initialize the database
        presetsRepository.saveAndFlush(presets);

        int databaseSizeBeforeDelete = presetsRepository.findAll().size();

        // Delete the presets
        restPresetsMockMvc
            .perform(delete(ENTITY_API_URL_ID, presets.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presets> presetsList = presetsRepository.findAll();
        assertThat(presetsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
