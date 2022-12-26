package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.repository.PresetsRepository;
import rocks.zipcode.service.PresetsService;
import rocks.zipcode.service.dto.PresetsDTO;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Presets}.
 */
@RestController
@RequestMapping("/api")
public class PresetsResource {

    private final Logger log = LoggerFactory.getLogger(PresetsResource.class);

    private static final String ENTITY_NAME = "presets";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresetsService presetsService;

    private final PresetsRepository presetsRepository;

    public PresetsResource(PresetsService presetsService, PresetsRepository presetsRepository) {
        this.presetsService = presetsService;
        this.presetsRepository = presetsRepository;
    }

    /**
     * {@code POST  /presets} : Create a new presets.
     *
     * @param presetsDTO the presetsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presetsDTO, or with status {@code 400 (Bad Request)} if the presets has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presets")
    public ResponseEntity<PresetsDTO> createPresets(@RequestBody PresetsDTO presetsDTO) throws URISyntaxException {
        log.debug("REST request to save Presets : {}", presetsDTO);
        if (presetsDTO.getId() != null) {
            throw new BadRequestAlertException("A new presets cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PresetsDTO result = presetsService.save(presetsDTO);
        return ResponseEntity
            .created(new URI("/api/presets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presets/:id} : Updates an existing presets.
     *
     * @param id the id of the presetsDTO to save.
     * @param presetsDTO the presetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presetsDTO,
     * or with status {@code 400 (Bad Request)} if the presetsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presets/{id}")
    public ResponseEntity<PresetsDTO> updatePresets(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresetsDTO presetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Presets : {}, {}", id, presetsDTO);
        if (presetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PresetsDTO result = presetsService.update(presetsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, presetsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /presets/:id} : Partial updates given fields of an existing presets, field will ignore if it is null
     *
     * @param id the id of the presetsDTO to save.
     * @param presetsDTO the presetsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presetsDTO,
     * or with status {@code 400 (Bad Request)} if the presetsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the presetsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the presetsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/presets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PresetsDTO> partialUpdatePresets(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresetsDTO presetsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Presets partially : {}, {}", id, presetsDTO);
        if (presetsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presetsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presetsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PresetsDTO> result = presetsService.partialUpdate(presetsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, presetsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /presets} : get all the presets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presets in body.
     */
    @GetMapping("/presets")
    public List<PresetsDTO> getAllPresets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Presets");
        return presetsService.findAll();
    }

    /**
     * {@code GET  /presets/:id} : get the "id" presets.
     *
     * @param id the id of the presetsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presetsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presets/{id}")
    public ResponseEntity<PresetsDTO> getPresets(@PathVariable Long id) {
        log.debug("REST request to get Presets : {}", id);
        Optional<PresetsDTO> presetsDTO = presetsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presetsDTO);
    }

    /**
     * {@code DELETE  /presets/:id} : delete the "id" presets.
     *
     * @param id the id of the presetsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presets/{id}")
    public ResponseEntity<Void> deletePresets(@PathVariable Long id) {
        log.debug("REST request to delete Presets : {}", id);
        presetsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
