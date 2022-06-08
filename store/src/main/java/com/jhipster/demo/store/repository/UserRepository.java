package com.jhipster.demo.store.repository;

import com.jhipster.demo.store.domain.User;
import java.time.Instant;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data Couchbase repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JHipsterCouchbaseRepository<User, String>, CouchbaseSearchRepository<User, String> {
    default Mono<User> findOneByActivationKey(String activationKey) {
        return findIdByActivationKey(activationKey).map(User::getId).flatMap(this::findById);
    }

    @Query(FIND_IDS_QUERY + " AND activationKey = $1")
    Mono<User> findIdByActivationKey(String activationKey);

    default Flux<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
        return findAllById(toIds(findAllIdsByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(dateTime)));
    }

    @Query(FIND_IDS_QUERY + " AND activated = false AND activationKey IS NOT NULL AND createdDate < $1")
    Flux<User> findAllIdsByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    default Mono<User> findOneByResetKey(String resetKey) {
        return findIdByResetKey(resetKey).map(User::getId).flatMap(this::findById);
    }

    @Query(FIND_IDS_QUERY + " AND resetKey = $1")
    Mono<User> findIdByResetKey(String resetKey);

    default Mono<User> findOneByEmailIgnoreCase(String email) {
        return findIdByEmailIgnoreCase(email).map(User::getId).flatMap(this::findById);
    }

    @Query(FIND_IDS_QUERY + " AND LOWER(email) = LOWER($1)")
    Mono<User> findIdByEmailIgnoreCase(String email);

    default Mono<User> findOneByLogin(String login) {
        return findById(login);
    }

    default Flux<User> findAllByActivatedIsTrue(Pageable pageable) {
        return findAllById(toIds(findAllIdsByActivatedIsTrue(pageable)));
    }

    @Query(FIND_IDS_QUERY + " AND activated = true")
    Flux<User> findAllIdsByActivatedIsTrue(Pageable pageable);

    Mono<Long> count();
}
