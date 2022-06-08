package com.jhipster.demo.product.domain;

import static com.jhipster.demo.product.config.Constants.ID_DELIMITER;
import static com.jhipster.demo.product.domain.ProductOrder.TYPE_NAME;
import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jhipster.demo.product.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.repository.Collection;

/**
 * A ProductOrder.
 */
@Document
@TypeAlias(TYPE_NAME)
@Collection(TYPE_NAME)
public class ProductOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_NAME = "productOrder";

    @Id
    @GeneratedValue(strategy = UNIQUE, delimiter = ID_DELIMITER)
    private String id;

    @NotNull(message = "must not be null")
    @Field
    private Instant placedDate;

    @NotNull(message = "must not be null")
    @Field
    private OrderStatus status;

    @NotNull(message = "must not be null")
    @Field
    private String code;

    @Field
    private Long invoiceId;

    @NotNull(message = "must not be null")
    @Field
    private String customer;

    @JsonIgnoreProperties(value = { "product", "order" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @JsonIgnore
    @Field("orderItems")
    private Set<String> orderItemIds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ProductOrder id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getPlacedDate() {
        return this.placedDate;
    }

    public ProductOrder placedDate(Instant placedDate) {
        this.setPlacedDate(placedDate);
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public ProductOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public ProductOrder code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public ProductOrder invoiceId(Long invoiceId) {
        this.setInvoiceId(invoiceId);
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustomer() {
        return this.customer;
    }

    public ProductOrder customer(String customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setOrder(this));
        }
        this.orderItems = orderItems;
        this.orderItemIds = orderItems.stream().map(OrderItem::getId).collect(Collectors.toSet());
    }

    public ProductOrder orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public ProductOrder addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        this.orderItemIds.add(orderItem.getId());
        orderItem.setOrder(this);
        return this;
    }

    public ProductOrder removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        this.orderItemIds.remove(orderItem.getId());
        orderItem.setOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOrder)) {
            return false;
        }
        return id != null && id.equals(((ProductOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductOrder{" +
            "id=" + getId() +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", code='" + getCode() + "'" +
            ", invoiceId=" + getInvoiceId() +
            ", customer='" + getCustomer() + "'" +
            "}";
    }
}
