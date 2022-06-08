package com.jhipster.demo.notification.domain;

import static com.jhipster.demo.notification.config.Constants.ID_DELIMITER;
import static com.jhipster.demo.notification.domain.Notification.TYPE_NAME;
import static org.springframework.data.couchbase.core.mapping.id.GenerationStrategy.UNIQUE;

import com.jhipster.demo.notification.domain.enumeration.NotificationType;
import java.io.Serializable;
import java.time.Instant;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.repository.Collection;

/**
 * A Notification.
 */
@Document
@TypeAlias(TYPE_NAME)
@Collection(TYPE_NAME)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_NAME = "notification";

    @Id
    @GeneratedValue(strategy = UNIQUE, delimiter = ID_DELIMITER)
    private String id;

    @NotNull
    @Field
    private Instant date;

    @Field
    private String details;

    @NotNull
    @Field
    private Instant sentDate;

    @NotNull
    @Field
    private NotificationType format;

    @NotNull
    @Field
    private Long userId;

    @NotNull
    @Field
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Notification id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Notification date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDetails() {
        return this.details;
    }

    public Notification details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Instant getSentDate() {
        return this.sentDate;
    }

    public Notification sentDate(Instant sentDate) {
        this.setSentDate(sentDate);
        return this;
    }

    public void setSentDate(Instant sentDate) {
        this.sentDate = sentDate;
    }

    public NotificationType getFormat() {
        return this.format;
    }

    public Notification format(NotificationType format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(NotificationType format) {
        this.format = format;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Notification userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Notification productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", details='" + getDetails() + "'" +
            ", sentDate='" + getSentDate() + "'" +
            ", format='" + getFormat() + "'" +
            ", userId=" + getUserId() +
            ", productId=" + getProductId() +
            "}";
    }
}
