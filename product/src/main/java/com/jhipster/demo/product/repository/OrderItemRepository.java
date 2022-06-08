package com.jhipster.demo.product.repository;

import static com.jhipster.demo.product.domain.OrderItem.TYPE_NAME;
import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.pageableStatement;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.product.domain.OrderItem;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends JHipsterCouchbaseRepository<OrderItem, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", OBJECT_ADD(`product`, 'id', meta(`product`).id) AS `product`" +
        ", OBJECT_ADD(`order`, 'id', meta(`order`).id) AS `order`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN `product` `product` ON KEYS b.`product`" + " LEFT JOIN `productOrder` `order` ON KEYS b.`order`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Flux<OrderItem> findAll(Pageable pageable) {
        return findAll(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<OrderItem> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    Flux<OrderItem> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Mono<OrderItem> findById(String id);
}
