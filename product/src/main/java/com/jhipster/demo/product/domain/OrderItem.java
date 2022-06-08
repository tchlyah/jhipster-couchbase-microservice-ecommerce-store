package com.jhipster.demo.product.domain;

import static com.jhipster.demo.product.config.Constants.ID_DELIMITER;
import static com.jhipster.demo.product.domain.OrderItem.TYPE_NAME;
import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jhipster.demo.product.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.stream.Collectors;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.repository.Collection;

/**
 * A OrderItem.
 */
@Document
@TypeAlias(TYPE_NAME)
@Collection(TYPE_NAME)
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_NAME = "orderItem";

    @Id
    @GeneratedValue(strategy = UNIQUE, delimiter = ID_DELIMITER)
    private String id;

    @NotNull(message = "must not be null")
    @Min(value = 0)
    @Field
    private Integer quantity;

    @NotNull(message = "must not be null")
    @DecimalMin(value = "0")
    @Field
    private BigDecimal totalPrice;

    @NotNull(message = "must not be null")
    @Field
    private OrderItemStatus status;

    @JsonIgnoreProperties(value = { "productCategory" }, allowSetters = true)
    private Product product;

    @JsonIgnoreProperties(value = { "orderItems" }, allowSetters = true)
    private ProductOrder order;

    @JsonIgnore
    @Field("product")
    private String productId;

    @JsonIgnore
    @Field("order")
    private String orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public OrderItem id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public OrderItem totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderItemStatus getStatus() {
        return this.status;
    }

    public OrderItem status(OrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public ProductOrder getOrder() {
        return this.order;
    }

    public void setOrder(ProductOrder productOrder) {
        this.order = productOrder;
        this.orderId = productOrder != null ? productOrder.getId() : null;
    }

    public OrderItem order(ProductOrder productOrder) {
        this.setOrder(productOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
