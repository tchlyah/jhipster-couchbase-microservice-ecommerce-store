package com.jhipster.demo.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.couchbase.client.java.search.SearchQuery;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JHipsterCouchbaseRepositoryTest {

    @MethodSource
    @ParameterizedTest
    void queryString(String query, SearchQuery ftsQuery) {
        assertThat(JHipsterCouchbaseRepository.searchQuery(query).toString()).isEqualTo(ftsQuery.toString());
    }

    @SuppressWarnings("unused")
    private static Stream<Arguments> queryString() {
        return Stream.of(
            Arguments.of("id:A", SearchQuery.docId("A")),
            Arguments.of("id:A id:B", SearchQuery.docId("A", "B")),
            Arguments.of("hello id:A", SearchQuery.conjuncts(SearchQuery.queryString("hello"), SearchQuery.docId("A"))),
            Arguments.of(
                "hello id:A kitty id:B",
                SearchQuery.conjuncts(SearchQuery.queryString("hello kitty"), SearchQuery.docId("A", "B"))
            ),
            Arguments.of("hello kitty", SearchQuery.queryString("hello kitty"))
        );
    }
}
