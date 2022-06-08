package com.jhipster.demo.product.repository;

import static com.jhipster.demo.product.domain.ProductOrder.TYPE_NAME;
import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.pageableStatement;

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
        ", ARRAY OBJECT_ADD(item, 'id', meta(item).id) FOR item IN `orderItems` END AS `orderItems`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT NEST `orderItem` `orderItems` ON KEYS b.`orderItems`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Flux<ProductOrder> findAll(Pageable pageable) {
        return findAll(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<ProductOrder> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    Flux<ProductOrder> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Mono<ProductOrder> findById(String id);
}
