package com.jhipster.demo.store.repository;

import static com.jhipster.demo.store.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.store.repository.JHipsterCouchbaseRepository.searchQuery;

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
public interface CustomerRepository extends JHipsterCouchbaseRepository<Customer, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", (SELECT `user`.*, meta(`user`).id)[0] as `user`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN #{#n1ql.bucket} `user` ON KEYS b.`user`";

    default Flux<Customer> findAllBy(Pageable pageable) {
        return findAllBy(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<Customer> findAllBy(String pageableStatement);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<Customer> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Mono<Customer> findById(String id);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} AND SEARCH(b, #{[0]}) #{[1]}")
    Flux<Customer> search(String queryString, String pageableStatement);

    default Flux<Customer> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Flux<Customer> search(String queryString, Pageable pageable) {
        return search(searchQuery(queryString).toString(), pageableStatement(pageable, "b"));
    }
}
