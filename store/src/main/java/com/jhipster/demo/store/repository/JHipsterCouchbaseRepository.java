package com.jhipster.demo.store.repository;

import static java.lang.String.format;

import com.couchbase.client.java.query.QueryScanConsistency;
import java.util.stream.Collectors;
import org.springframework.data.couchbase.repository.*;
import org.springframework.data.domain.*;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Couchbase specific {@link org.springframework.data.repository.Repository} interface that use KV first approach to optimize requests.
 */
@NoRepositoryBean
@ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
public interface JHipsterCouchbaseRepository<T, ID> extends ReactiveCouchbaseRepository<T, ID> {
    String FIND_IDS_QUERY = "SELECT meta().id as __id, 0 as __cas FROM #{#n1ql.bucket} WHERE #{#n1ql.filter}";

    static String pageableStatement(Pageable pageable, String prefix) {
        Sort sort = Sort.by(
            pageable
                .getSort()
                .stream()
                .map(order -> {
                    String property = order.getProperty();
                    if ("id".equals(property)) {
                        return order.withProperty(format("meta(%s).id", prefix));
                    }
                    if (prefix.isEmpty()) {
                        return order;
                    }
                    return order.withProperty(format("%s.%s", prefix, property));
                })
                .collect(Collectors.toList())
        );
        return new org.springframework.data.couchbase.core.query.Query()
            .limit(pageable.getPageSize())
            .skip(pageable.getOffset())
            .with(sort)
            .export();
    }

    default Flux<T> findAll() {
        return findAllById(toIds(findAllIds()));
    }

    default Flux<T> findAllBy(Pageable pageable) {
        return findAllById(toIds(findAllIds(pageable)));
    }

    default Flux<T> findAll(Sort sort) {
        return findAllById(toIds(findAllIds(sort)));
    }

    default Mono<Void> deleteAll() {
        return toIds(findAllIds()).collectList().flatMap(this::deleteAllById);
    }

    @Query(FIND_IDS_QUERY)
    Flux<T> findAllIds();

    @Query(FIND_IDS_QUERY)
    Flux<T> findAllIds(Pageable pageable);

    @Query(FIND_IDS_QUERY)
    Flux<T> findAllIds(Sort sort);

    @SuppressWarnings("unchecked")
    default Flux<ID> toIds(Flux<T> entities) {
        return entities.mapNotNull(entity -> (ID) getEntityInformation().getId(entity));
    }
}
