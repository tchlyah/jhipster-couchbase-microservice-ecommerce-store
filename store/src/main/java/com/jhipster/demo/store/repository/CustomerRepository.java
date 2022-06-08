package com.jhipster.demo.store.repository;

import static com.jhipster.demo.store.domain.Customer.TYPE_NAME;
import static com.jhipster.demo.store.repository.JHipsterCouchbaseRepository.pageableStatement;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.store.domain.Customer;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JHipsterCouchbaseRepository<Customer, String>, CouchbaseSearchRepository<Customer, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", OBJECT_ADD(`user`, 'id', meta(`user`).id) AS `user`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN `user` `user` ON KEYS b.`user`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Flux<Customer> findAll(Pageable pageable) {
        return findAll(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<Customer> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    Flux<Customer> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Mono<Customer> findById(String id);

    @Query(SELECT + JOIN + WHERE + " AND " + SEARCH_CONDITION)
    Flux<Customer> search(String queryString);

    default Flux<Customer> search(String queryString, Pageable pageable) {
        return search(queryString, pageableStatement(pageable, "b"));
    }

    @ScanConsistency(query = QueryScanConsistency.NOT_BOUNDED)
    @Query(SELECT + JOIN + WHERE + " AND " + SEARCH_CONDITION + " #{[1]}")
    Flux<Customer> search(String queryString, String pageableStatement);
}
