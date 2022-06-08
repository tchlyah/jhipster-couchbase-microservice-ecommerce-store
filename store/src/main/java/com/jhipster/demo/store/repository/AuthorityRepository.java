package com.jhipster.demo.store.repository;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.store.domain.Authority;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Couchbase repository for the {@link Authority} entity.
 */
@Repository
public interface AuthorityRepository extends JHipsterCouchbaseRepository<Authority, String> {
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<Authority> findAll();
}
