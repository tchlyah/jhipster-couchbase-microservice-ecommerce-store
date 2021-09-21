package com.jhipster.demo.store.repository;

import static com.jhipster.demo.store.config.Constants.ID_DELIMITER;
import static com.jhipster.demo.store.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.store.repository.JHipsterCouchbaseRepository.searchQuery;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.store.domain.User;
import java.time.Instant;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JHipsterCouchbaseRepository<User, String> {
    // @ScanConsistency is to fix index issues with Spring Data Couchbase
    // https://github.com/spring-projects/spring-data-couchbase/issues/897

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Mono<User> findOneByActivationKey(String activationKey);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Mono<User> findOneByResetKey(String resetKey);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    @Query("#{#n1ql.selectEntity} WHERE LOWER(email) = LOWER($1) AND #{#n1ql.filter}")
    Mono<User> findOneByEmailIgnoreCase(String email);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    default Mono<User> findOneByLogin(String login) {
        return findById(User.PREFIX + ID_DELIMITER + login);
    }

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<User> findAllByIdNotNull(Pageable pageable);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Flux<User> findAllByActivatedIsTrue(Pageable pageable);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Mono<Long> count();

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} AND SEARCH(#{n1ql.bucket}, #{[0]) #{[1]}")
    Flux<User> search(String queryString, String pageableStatement);

    default Flux<User> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Flux<User> search(String queryString, Pageable pageable) {
        return search(searchQuery(queryString).toString(), pageableStatement(pageable));
    }
}
