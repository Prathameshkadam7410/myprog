package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EmailConfig;
import com.mycompany.myapp.repository.EmailConfigRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.EmailConfig}.
 */
@RestController
@RequestMapping("/api/email-configs")
@Transactional
public class EmailConfigResource {

    private final Logger log = LoggerFactory.getLogger(EmailConfigResource.class);

    private static final String ENTITY_NAME = "emailConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmailConfigRepository emailConfigRepository;

    public EmailConfigResource(EmailConfigRepository emailConfigRepository) {
        this.emailConfigRepository = emailConfigRepository;
    }

    /**
     * {@code POST  /email-configs} : Create a new emailConfig.
     *
     * @param emailConfig the emailConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emailConfig, or with status {@code 400 (Bad Request)} if the emailConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EmailConfig>> createEmailConfig(@RequestBody EmailConfig emailConfig) throws URISyntaxException {
        log.debug("REST request to save EmailConfig : {}", emailConfig);
        if (emailConfig.getId() != null) {
            throw new BadRequestAlertException("A new emailConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return emailConfigRepository
            .save(emailConfig)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/email-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /email-configs/:id} : Updates an existing emailConfig.
     *
     * @param id the id of the emailConfig to save.
     * @param emailConfig the emailConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailConfig,
     * or with status {@code 400 (Bad Request)} if the emailConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emailConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmailConfig>> updateEmailConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailConfig emailConfig
    ) throws URISyntaxException {
        log.debug("REST request to update EmailConfig : {}, {}", id, emailConfig);
        if (emailConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emailConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return emailConfigRepository
                    .save(emailConfig)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        result ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                                .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /email-configs/:id} : Partial updates given fields of an existing emailConfig, field will ignore if it is null
     *
     * @param id the id of the emailConfig to save.
     * @param emailConfig the emailConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emailConfig,
     * or with status {@code 400 (Bad Request)} if the emailConfig is not valid,
     * or with status {@code 404 (Not Found)} if the emailConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the emailConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmailConfig>> partialUpdateEmailConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EmailConfig emailConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmailConfig partially : {}, {}", id, emailConfig);
        if (emailConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emailConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emailConfigRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmailConfig> result = emailConfigRepository
                    .findById(emailConfig.getId())
                    .map(existingEmailConfig -> {
                        if (emailConfig.getEmailId() != null) {
                            existingEmailConfig.setEmailId(emailConfig.getEmailId());
                        }
                        if (emailConfig.getTokenString() != null) {
                            existingEmailConfig.setTokenString(emailConfig.getTokenString());
                        }

                        return existingEmailConfig;
                    })
                    .flatMap(emailConfigRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(
                        res ->
                            ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                                .body(res)
                    );
            });
    }

    /**
     * {@code GET  /email-configs} : get all the emailConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emailConfigs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<EmailConfig>> getAllEmailConfigs() {
        log.debug("REST request to get all EmailConfigs");
        return emailConfigRepository.findAll().collectList();
    }

    /**
     * {@code GET  /email-configs} : get all the emailConfigs as a stream.
     * @return the {@link Flux} of emailConfigs.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<EmailConfig> getAllEmailConfigsAsStream() {
        log.debug("REST request to get all EmailConfigs as a stream");
        return emailConfigRepository.findAll();
    }

    /**
     * {@code GET  /email-configs/:id} : get the "id" emailConfig.
     *
     * @param id the id of the emailConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emailConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmailConfig>> getEmailConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get EmailConfig : {}", id);
        Mono<EmailConfig> emailConfig = emailConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(emailConfig);
    }

    /**
     * {@code DELETE  /email-configs/:id} : delete the "id" emailConfig.
     *
     * @param id the id of the emailConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmailConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmailConfig : {}", id);
        return emailConfigRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
