package com.jhipster.demo.product.service;

import com.jhipster.demo.product.domain.OrderItem;
import com.jhipster.demo.product.repository.OrderItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link OrderItem}.
 */
@Service
public class OrderItemService {

    private final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    /**
     * Save a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public Mono<OrderItem> save(OrderItem orderItem) {
        log.debug("Request to save OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Update a orderItem.
     *
     * @param orderItem the entity to save.
     * @return the persisted entity.
     */
    public Mono<OrderItem> update(OrderItem orderItem) {
        log.debug("Request to save OrderItem : {}", orderItem);
        return orderItemRepository.save(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItem the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<OrderItem> partialUpdate(OrderItem orderItem) {
        log.debug("Request to partially update OrderItem : {}", orderItem);

        return orderItemRepository
            .findById(orderItem.getId())
            .map(existingOrderItem -> {
                if (orderItem.getQuantity() != null) {
                    existingOrderItem.setQuantity(orderItem.getQuantity());
                }
                if (orderItem.getTotalPrice() != null) {
                    existingOrderItem.setTotalPrice(orderItem.getTotalPrice());
                }
                if (orderItem.getStatus() != null) {
                    existingOrderItem.setStatus(orderItem.getStatus());
                }

                return existingOrderItem;
            })
            .flatMap(orderItemRepository::save);
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Flux<OrderItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of orderItems available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return orderItemRepository.count();
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Mono<OrderItem> findOne(String id) {
        log.debug("Request to get OrderItem : {}", id);
        return orderItemRepository.findById(id);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(String id) {
        log.debug("Request to delete OrderItem : {}", id);
        return orderItemRepository.deleteById(id);
    }
}
