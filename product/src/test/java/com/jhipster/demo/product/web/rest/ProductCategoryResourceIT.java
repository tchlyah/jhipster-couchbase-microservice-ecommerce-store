package com.jhipster.demo.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.jhipster.demo.product.IntegrationTest;
import com.jhipster.demo.product.domain.ProductCategory;
import com.jhipster.demo.product.repository.ProductCategoryRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ProductCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ProductCategory productCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity() {
        ProductCategory productCategory = new ProductCategory().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return productCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createUpdatedEntity() {
        ProductCategory productCategory = new ProductCategory().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return productCategory;
    }

    @BeforeEach
    public void initTest() {
        productCategoryRepository.deleteAll().block();
        productCategory = createEntity();
    }

    @Test
    void createProductCategory() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().collectList().block().size();
        // Create the ProductCategory
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createProductCategoryWithExistingId() throws Exception {
        // Create the ProductCategory with an existing ID
        productCategory.setId("existing_id");

        int databaseSizeBeforeCreate = productCategoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productCategoryRepository.findAll().collectList().block().size();
        // set the field null
        productCategory.setName(null);

        // Create the ProductCategory, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductCategoriesAsStream() {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        List<ProductCategory> productCategoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ProductCategory.class)
            .getResponseBody()
            .filter(productCategory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(productCategoryList).isNotNull();
        assertThat(productCategoryList).hasSize(1);
        ProductCategory testProductCategory = productCategoryList.get(0);
        assertThat(testProductCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProductCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void getAllProductCategories() {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        // Get all the productCategoryList
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
            .value(hasItem(productCategory.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getProductCategory() {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        // Get the productCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, productCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(productCategory.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingProductCategory() {
        // Get the productCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findById(productCategory.getId()).block();
        updatedProductCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProductCategory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProductCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productCategory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProductCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().collectList().block().size();
        productCategory.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productCategory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductCategory in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductCategory() {
        // Initialize the database
        productCategoryRepository.save(productCategory).block();

        int databaseSizeBeforeDelete = productCategoryRepository.findAll().collectList().block().size();

        // Delete the productCategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, productCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll().collectList().block();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
