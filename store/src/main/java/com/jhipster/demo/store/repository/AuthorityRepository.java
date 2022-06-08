package com.jhipster.demo.store.repository;

import com.jhipster.demo.store.domain.Authority;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the {@link Authority} entity.
 */
@Repository
public interface AuthorityRepository extends JHipsterCouchbaseRepository<Authority, String> {}
