package rocks.zipcode.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.domain.Presets;
import rocks.zipcode.repository.PresetsRepository;
import rocks.zipcode.service.dto.PresetsDTO;
import rocks.zipcode.service.mapper.PresetsMapper;

/**
 * Service Implementation for managing {@link Presets}.
 */
@Service
@Transactional
public class PresetsService {

    private final Logger log = LoggerFactory.getLogger(PresetsService.class);

    private final PresetsRepository presetsRepository;

    private final PresetsMapper presetsMapper;

    public PresetsService(PresetsRepository presetsRepository, PresetsMapper presetsMapper) {
        this.presetsRepository = presetsRepository;
        this.presetsMapper = presetsMapper;
    }

    /**
     * Save a presets.
     *
     * @param presetsDTO the entity to save.
     * @return the persisted entity.
     */
    public PresetsDTO save(PresetsDTO presetsDTO) {
        log.debug("Request to save Presets : {}", presetsDTO);
        Presets presets = presetsMapper.toEntity(presetsDTO);
        presets = presetsRepository.save(presets);
        return presetsMapper.toDto(presets);
    }

    /**
     * Update a presets.
     *
     * @param presetsDTO the entity to save.
     * @return the persisted entity.
     */
    public PresetsDTO update(PresetsDTO presetsDTO) {
        log.debug("Request to update Presets : {}", presetsDTO);
        Presets presets = presetsMapper.toEntity(presetsDTO);
        presets = presetsRepository.save(presets);
        return presetsMapper.toDto(presets);
    }

    /**
     * Partially update a presets.
     *
     * @param presetsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PresetsDTO> partialUpdate(PresetsDTO presetsDTO) {
        log.debug("Request to partially update Presets : {}", presetsDTO);

        return presetsRepository
            .findById(presetsDTO.getId())
            .map(existingPresets -> {
                presetsMapper.partialUpdate(existingPresets, presetsDTO);

                return existingPresets;
            })
            .map(presetsRepository::save)
            .map(presetsMapper::toDto);
    }

    /**
     * Get all the presets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PresetsDTO> findAll() {
        log.debug("Request to get all Presets");
        return presetsRepository.findAll().stream().map(presetsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the presets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PresetsDTO> findAllWithEagerRelationships(Pageable pageable) {
        return presetsRepository.findAllWithEagerRelationships(pageable).map(presetsMapper::toDto);
    }

    /**
     * Get one presets by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PresetsDTO> findOne(Long id) {
        log.debug("Request to get Presets : {}", id);
        return presetsRepository.findOneWithEagerRelationships(id).map(presetsMapper::toDto);
    }

    /**
     * Delete the presets by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Presets : {}", id);
        presetsRepository.deleteById(id);
    }
}
