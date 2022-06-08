package com.jhipster.demo.notification.repository;

import static com.jhipster.demo.notification.domain.Notification.TYPE_NAME;

import com.jhipster.demo.notification.domain.Notification;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JHipsterCouchbaseRepository<Notification, String> {}
