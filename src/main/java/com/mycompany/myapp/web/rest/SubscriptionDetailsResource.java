package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SubscriptionDetails;
import com.mycompany.myapp.repository.SubscriptionDetailsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SubscriptionDetails}.
 */
@RestController
@RequestMapping("/api/subscription-details")
@Transactional
public class SubscriptionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionDetailsResource.class);

    private static final String ENTITY_NAME = "subscriptionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionDetailsRepository subscriptionDetailsRepository;

    public SubscriptionDetailsResource(SubscriptionDetailsRepository subscriptionDetailsRepository) {
        this.subscriptionDetailsRepository = subscriptionDetailsRepository;
    }

    /**
     * {@code POST  /subscription-details} : Create a new subscriptionDetails.
     *
     * @param subscriptionDetails the subscriptionDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionDetails, or with status {@code 400 (Bad Request)} if the subscriptionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SubscriptionDetails>> createSubscriptionDetails(@Valid @RequestBody SubscriptionDetails subscriptionDetails)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionDetails : {}", subscriptionDetails);
        if (subscriptionDetails.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subscriptionDetailsRepository
            .save(subscriptionDetails)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/subscription-details/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /subscription-details/:id} : Updates an existing subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to save.
     * @param subscriptionDetails the subscriptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetails,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SubscriptionDetails>> updateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionDetails subscriptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionDetails : {}, {}", id, subscriptionDetails);
        if (subscriptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subscriptionDetailsRepository
                    .save(subscriptionDetails)
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
     * {@code PATCH  /subscription-details/:id} : Partial updates given fields of an existing subscriptionDetails, field will ignore if it is null
     *
     * @param id the id of the subscriptionDetails to save.
     * @param subscriptionDetails the subscriptionDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionDetails,
     * or with status {@code 400 (Bad Request)} if the subscriptionDetails is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SubscriptionDetails>> partialUpdateSubscriptionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionDetails subscriptionDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionDetails partially : {}, {}", id, subscriptionDetails);
        if (subscriptionDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionDetailsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SubscriptionDetails> result = subscriptionDetailsRepository
                    .findById(subscriptionDetails.getId())
                    .map(existingSubscriptionDetails -> {
                        if (subscriptionDetails.getSubscriptionName() != null) {
                            existingSubscriptionDetails.setSubscriptionName(subscriptionDetails.getSubscriptionName());
                        }
                        if (subscriptionDetails.getSubscriptionAmount() != null) {
                            existingSubscriptionDetails.setSubscriptionAmount(subscriptionDetails.getSubscriptionAmount());
                        }
                        if (subscriptionDetails.getTaxAmount() != null) {
                            existingSubscriptionDetails.setTaxAmount(subscriptionDetails.getTaxAmount());
                        }
                        if (subscriptionDetails.getTotalAmount() != null) {
                            existingSubscriptionDetails.setTotalAmount(subscriptionDetails.getTotalAmount());
                        }
                        if (subscriptionDetails.getSubscriptionStartDate() != null) {
                            existingSubscriptionDetails.setSubscriptionStartDate(subscriptionDetails.getSubscriptionStartDate());
                        }
                        if (subscriptionDetails.getSubscriptionExpiryDate() != null) {
                            existingSubscriptionDetails.setSubscriptionExpiryDate(subscriptionDetails.getSubscriptionExpiryDate());
                        }
                        if (subscriptionDetails.getAdditionalComments() != null) {
                            existingSubscriptionDetails.setAdditionalComments(subscriptionDetails.getAdditionalComments());
                        }
                        if (subscriptionDetails.getCategory() != null) {
                            existingSubscriptionDetails.setCategory(subscriptionDetails.getCategory());
                        }
                        if (subscriptionDetails.getNotificationBeforeExpiry() != null) {
                            existingSubscriptionDetails.setNotificationBeforeExpiry(subscriptionDetails.getNotificationBeforeExpiry());
                        }
                        if (subscriptionDetails.getNotificationMuteFlag() != null) {
                            existingSubscriptionDetails.setNotificationMuteFlag(subscriptionDetails.getNotificationMuteFlag());
                        }
                        if (subscriptionDetails.getNotificationTo() != null) {
                            existingSubscriptionDetails.setNotificationTo(subscriptionDetails.getNotificationTo());
                        }
                        if (subscriptionDetails.getNotificationCc() != null) {
                            existingSubscriptionDetails.setNotificationCc(subscriptionDetails.getNotificationCc());
                        }
                        if (subscriptionDetails.getNotificationBcc() != null) {
                            existingSubscriptionDetails.setNotificationBcc(subscriptionDetails.getNotificationBcc());
                        }

                        return existingSubscriptionDetails;
                    })
                    .flatMap(subscriptionDetailsRepository::save);

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
     * {@code GET  /subscription-details} : get all the subscriptionDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionDetails in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<SubscriptionDetails>> getAllSubscriptionDetails() {
        log.debug("REST request to get all SubscriptionDetails");
        return subscriptionDetailsRepository.findAll().collectList();
    }

    /**
     * {@code GET  /subscription-details} : get all the subscriptionDetails as a stream.
     * @return the {@link Flux} of subscriptionDetails.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SubscriptionDetails> getAllSubscriptionDetailsAsStream() {
        log.debug("REST request to get all SubscriptionDetails as a stream");
        return subscriptionDetailsRepository.findAll();
    }

    /**
     * {@code GET  /subscription-details/:id} : get the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SubscriptionDetails>> getSubscriptionDetails(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscriptionDetails : {}", id);
        Mono<SubscriptionDetails> subscriptionDetails = subscriptionDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subscriptionDetails);
    }

    /**
     * {@code DELETE  /subscription-details/:id} : delete the "id" subscriptionDetails.
     *
     * @param id the id of the subscriptionDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSubscriptionDetails(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscriptionDetails : {}", id);
        return subscriptionDetailsRepository
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
