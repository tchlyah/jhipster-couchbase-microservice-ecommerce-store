package com.jhipster.demo.product.service;

import com.jhipster.demo.product.domain.ProductCategory;
import com.jhipster.demo.product.repository.ProductCategoryRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
public class ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryService.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductCategory> save(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Update a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProductCategory> update(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    /**
     * Partially update a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProductCategory> partialUpdate(ProductCategory productCategory) {
        log.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                if (productCategory.getName() != null) {
                    existingProductCategory.setName(productCategory.getName());
                }
                if (productCategory.getDescription() != null) {
                    existingProductCategory.setDescription(productCategory.getDescription());
                }

                return existingProductCategory;
            })
            .flatMap(productCategoryRepository::save);
    }

    /**
     * Get all the productCategories.
     *
     * @return the list of entities.
     */
    public Flux<ProductCategory> findAll() {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll();
    }

    /**
     * Returns the number of productCategories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return productCategoryRepository.count();
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<ProductCategory> findOne(String id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id);
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ProductCategory : {}", id);
        return productCategoryRepository.deleteById(id);
    }
}
