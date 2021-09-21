package com.jhipster.demo.product.repository;

import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.searchQuery;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.product.domain.ProductOrder;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the ProductOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOrderRepository extends JHipsterCouchbaseRepository<ProductOrder, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", (SELECT `orderItem`.*, meta(`orderItem`).id FROM `orderItems` `orderItem`) as `orderItems`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT NEST #{#n1ql.bucket} `orderItems` ON KEYS b.`orderItems`";

    default Flux<ProductOrder> findAllBy(Pageable pageable) {
        return findAllBy(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<ProductOrder> findAllBy(String pageableStatement);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<ProductOrder> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Mono<ProductOrder> findById(String id);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} AND SEARCH(b, #{[0]}) #{[1]}")
    Flux<ProductOrder> search(String queryString, String pageableStatement);

    default Flux<ProductOrder> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Flux<ProductOrder> search(String queryString, Pageable pageable) {
        return search(searchQuery(queryString).toString(), pageableStatement(pageable, "b"));
    }
}