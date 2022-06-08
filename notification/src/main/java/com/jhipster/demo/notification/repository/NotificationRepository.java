package com.jhipster.demo.notification.repository;

import static com.jhipster.demo.notification.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.notification.repository.JHipsterCouchbaseRepository.searchQuery;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.notification.domain.Notification;
import java.util.List;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JHipsterCouchbaseRepository<Notification, String> {
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND SEARCH(#{#n1ql.bucket}, #{[0]}) #{[1]}")
    List<Notification> search(String queryString, String pageableStatement);

    default List<Notification> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Page<Notification> search(String queryString, Pageable pageable) {
        return new PageImpl<>(search(searchQuery(queryString).toString(), pageableStatement(pageable)));
    }
}
