package com.jhipster.demo.product.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.jhipster.demo.product.IntegrationTest;
import com.jhipster.demo.product.domain.ProductOrder;
import com.jhipster.demo.product.domain.enumeration.OrderStatus;
import com.jhipster.demo.product.repository.ProductOrderRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ProductOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ProductOrderResourceIT {

    private static final Instant DEFAULT_PLACED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PLACED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.COMPLETED;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PENDING;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_INVOICE_ID = 1L;
    private static final Long UPDATED_INVOICE_ID = 2L;

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private WebTestClient webTestClient;

    private ProductOrder productOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createEntity() {
        ProductOrder productOrder = new ProductOrder()
            .placedDate(DEFAULT_PLACED_DATE)
            .status(DEFAULT_STATUS)
            .code(DEFAULT_CODE)
            .invoiceId(DEFAULT_INVOICE_ID)
            .customer(DEFAULT_CUSTOMER);
        return productOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOrder createUpdatedEntity() {
        ProductOrder productOrder = new ProductOrder()
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);
        return productOrder;
    }

    @BeforeEach
    public void initTest() {
        productOrderRepository.deleteAll().block();
        productOrder = createEntity();
    }

    @Test
    void createProductOrder() throws Exception {
        int databaseSizeBeforeCreate = productOrderRepository.findAll().collectList().block().size();
        // Create the ProductOrder
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(DEFAULT_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(DEFAULT_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
    }

    @Test
    void createProductOrderWithExistingId() throws Exception {
        // Create the ProductOrder with an existing ID
        productOrder.setId("existing_id");

        int databaseSizeBeforeCreate = productOrderRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPlacedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().collectList().block().size();
        // set the field null
        productOrder.setPlacedDate(null);

        // Create the ProductOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().collectList().block().size();
        // set the field null
        productOrder.setStatus(null);

        // Create the ProductOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().collectList().block().size();
        // set the field null
        productOrder.setCode(null);

        // Create the ProductOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustomerIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOrderRepository.findAll().collectList().block().size();
        // set the field null
        productOrder.setCustomer(null);

        // Create the ProductOrder, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProductOrders() {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        // Get all the productOrderList
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
            .value(hasItem(productOrder.getId()))
            .jsonPath("$.[*].placedDate")
            .value(hasItem(DEFAULT_PLACED_DATE.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].invoiceId")
            .value(hasItem(DEFAULT_INVOICE_ID.intValue()))
            .jsonPath("$.[*].customer")
            .value(hasItem(DEFAULT_CUSTOMER));
    }

    @Test
    void getProductOrder() {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        // Get the productOrder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, productOrder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(productOrder.getId()))
            .jsonPath("$.placedDate")
            .value(is(DEFAULT_PLACED_DATE.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.invoiceId")
            .value(is(DEFAULT_INVOICE_ID.intValue()))
            .jsonPath("$.customer")
            .value(is(DEFAULT_CUSTOMER));
    }

    @Test
    void getNonExistingProductOrder() {
        // Get the productOrder
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProductOrder() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();

        // Update the productOrder
        ProductOrder updatedProductOrder = productOrderRepository.findById(productOrder.getId()).block();
        updatedProductOrder
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProductOrder.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProductOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
    }

    @Test
    void putNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, productOrder.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder.placedDate(UPDATED_PLACED_DATE).status(UPDATED_STATUS).code(UPDATED_CODE).invoiceId(UPDATED_INVOICE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
    }

    @Test
    void fullUpdateProductOrderWithPatch() throws Exception {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();

        // Update the productOrder using partial update
        ProductOrder partialUpdatedProductOrder = new ProductOrder();
        partialUpdatedProductOrder.setId(productOrder.getId());

        partialUpdatedProductOrder
            .placedDate(UPDATED_PLACED_DATE)
            .status(UPDATED_STATUS)
            .code(UPDATED_CODE)
            .invoiceId(UPDATED_INVOICE_ID)
            .customer(UPDATED_CUSTOMER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProductOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProductOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
        ProductOrder testProductOrder = productOrderList.get(productOrderList.size() - 1);
        assertThat(testProductOrder.getPlacedDate()).isEqualTo(UPDATED_PLACED_DATE);
        assertThat(testProductOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProductOrder.getInvoiceId()).isEqualTo(UPDATED_INVOICE_ID);
        assertThat(testProductOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
    }

    @Test
    void patchNonExistingProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, productOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProductOrder() throws Exception {
        int databaseSizeBeforeUpdate = productOrderRepository.findAll().collectList().block().size();
        productOrder.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(productOrder))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ProductOrder in the database
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProductOrder() {
        // Initialize the database
        productOrderRepository.save(productOrder).block();

        int databaseSizeBeforeDelete = productOrderRepository.findAll().collectList().block().size();

        // Delete the productOrder
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, productOrder.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        List<ProductOrder> productOrderList = productOrderRepository.findAll().collectList().block();
        assertThat(productOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
