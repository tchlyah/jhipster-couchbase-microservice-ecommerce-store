package com.jhipster.demo.product.repository;

import static com.jhipster.demo.product.domain.ProductCategory.TYPE_NAME;
import static com.jhipster.demo.product.repository.JHipsterCouchbaseRepository.pageableStatement;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.product.domain.ProductCategory;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the ProductCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductCategoryRepository extends JHipsterCouchbaseRepository<ProductCategory, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", ARRAY OBJECT_ADD(item, 'id', meta(item).id) FOR item IN `products` END AS `products`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT NEST `product` `products` ON KEYS b.`products`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Flux<ProductCategory> findAll(Pageable pageable) {
        return findAll(pageableStatement(pageable, "b"));
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<ProductCategory> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    Flux<ProductCategory> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Mono<ProductCategory> findById(String id);
}
