package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EmailConfigAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmailConfig;
import com.mycompany.myapp.repository.EmailConfigRepository;
import com.mycompany.myapp.repository.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link EmailConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmailConfigResourceIT {

    private static final String DEFAULT_EMAIL_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_STRING = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_STRING = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailConfigRepository emailConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EmailConfig emailConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailConfig createEntity(EntityManager em) {
        EmailConfig emailConfig = new EmailConfig().emailId(DEFAULT_EMAIL_ID).tokenString(DEFAULT_TOKEN_STRING);
        return emailConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailConfig createUpdatedEntity(EntityManager em) {
        EmailConfig emailConfig = new EmailConfig().emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);
        return emailConfig;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EmailConfig.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        emailConfig = createEntity(em);
    }

    @Test
    void createEmailConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailConfig
        var returnedEmailConfig = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(EmailConfig.class)
            .returnResult()
            .getResponseBody();

        // Validate the EmailConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmailConfigUpdatableFieldsEquals(returnedEmailConfig, getPersistedEmailConfig(returnedEmailConfig));
    }

    @Test
    void createEmailConfigWithExistingId() throws Exception {
        // Create the EmailConfig with an existing ID
        emailConfig.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEmailConfigsAsStream() {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        List<EmailConfig> emailConfigList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(EmailConfig.class)
            .getResponseBody()
            .filter(emailConfig::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(emailConfigList).isNotNull();
        assertThat(emailConfigList).hasSize(1);
        EmailConfig testEmailConfig = emailConfigList.get(0);

        // Test fails because reactive api returns an empty object instead of null
        // assertEmailConfigAllPropertiesEquals(emailConfig, testEmailConfig);
        assertEmailConfigUpdatableFieldsEquals(emailConfig, testEmailConfig);
    }

    @Test
    void getAllEmailConfigs() {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        // Get all the emailConfigList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(emailConfig.getId().intValue()))
            .jsonPath("$.[*].emailId")
            .value(hasItem(DEFAULT_EMAIL_ID))
            .jsonPath("$.[*].tokenString")
            .value(hasItem(DEFAULT_TOKEN_STRING));
    }

    @Test
    void getEmailConfig() {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        // Get the emailConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, emailConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(emailConfig.getId().intValue()))
            .jsonPath("$.emailId")
            .value(is(DEFAULT_EMAIL_ID))
            .jsonPath("$.tokenString")
            .value(is(DEFAULT_TOKEN_STRING));
    }

    @Test
    void getNonExistingEmailConfig() {
        // Get the emailConfig
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmailConfig() throws Exception {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig
        EmailConfig updatedEmailConfig = emailConfigRepository.findById(emailConfig.getId()).block();
        updatedEmailConfig.emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEmailConfig.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedEmailConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailConfigToMatchAllProperties(updatedEmailConfig);
    }

    @Test
    void putNonExistingEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, emailConfig.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEmailConfigWithPatch() throws Exception {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig using partial update
        EmailConfig partialUpdatedEmailConfig = new EmailConfig();
        partialUpdatedEmailConfig.setId(emailConfig.getId());

        partialUpdatedEmailConfig.emailId(UPDATED_EMAIL_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmailConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmailConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmailConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailConfig, emailConfig),
            getPersistedEmailConfig(emailConfig)
        );
    }

    @Test
    void fullUpdateEmailConfigWithPatch() throws Exception {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailConfig using partial update
        EmailConfig partialUpdatedEmailConfig = new EmailConfig();
        partialUpdatedEmailConfig.setId(emailConfig.getId());

        partialUpdatedEmailConfig.emailId(UPDATED_EMAIL_ID).tokenString(UPDATED_TOKEN_STRING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmailConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedEmailConfig))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmailConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailConfigUpdatableFieldsEquals(partialUpdatedEmailConfig, getPersistedEmailConfig(partialUpdatedEmailConfig));
    }

    @Test
    void patchNonExistingEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, emailConfig.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEmailConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailConfig.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(emailConfig))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmailConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEmailConfig() {
        // Initialize the database
        emailConfigRepository.save(emailConfig).block();

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailConfig
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, emailConfig.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailConfigRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected EmailConfig getPersistedEmailConfig(EmailConfig emailConfig) {
        return emailConfigRepository.findById(emailConfig.getId()).block();
    }

    protected void assertPersistedEmailConfigToMatchAllProperties(EmailConfig expectedEmailConfig) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmailConfigAllPropertiesEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
        assertEmailConfigUpdatableFieldsEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
    }

    protected void assertPersistedEmailConfigToMatchUpdatableProperties(EmailConfig expectedEmailConfig) {
        // Test fails because reactive api returns an empty object instead of null
        // assertEmailConfigAllUpdatablePropertiesEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
        assertEmailConfigUpdatableFieldsEquals(expectedEmailConfig, getPersistedEmailConfig(expectedEmailConfig));
    }
}
