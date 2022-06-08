package com.jhipster.demo.product.repository;

import static com.jhipster.demo.product.domain.Product.TYPE_NAME;
import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.pageableStatement;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.product.domain.Product;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JHipsterCouchbaseRepository<Product, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", OBJECT_ADD(`productCategory`, 'id', meta(`productCategory`).id) AS `productCategory`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN `productCategory` `productCategory` ON KEYS b.`productCategory`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Flux<Product> findAll(Pageable pageable) {
        return findAll(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<Product> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    Flux<Product> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Mono<Product> findById(String id);
}
