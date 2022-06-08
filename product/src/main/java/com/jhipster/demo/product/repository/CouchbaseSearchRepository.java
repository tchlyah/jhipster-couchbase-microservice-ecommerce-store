package com.jhipster.demo.product.repository;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.DocIdQuery;
import com.couchbase.client.java.search.queries.QueryStringQuery;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.*;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Couchbase specific {@link org.springframework.data.repository.Repository} interface for search queries.
 */
@NoRepositoryBean
public interface CouchbaseSearchRepository<T, ID> extends JHipsterCouchbaseRepository<T, ID> {
    String SEARCH_CONDITION = "SEARCH(b, #{T(com.jhipster.demo.product.repository.CouchbaseSearchRepository).searchQuery([0]).toString()})";

    static SearchQuery searchQuery(String queryString) {
        List<String> ids = new LinkedList<>();
        for (String r : queryString.split(" ")) {
            if (r.indexOf("id:") == 0) {
                ids.add(r.substring(3));
                queryString = queryString.replace(r, "").replaceAll("[ ]+", " ").trim();
            }
        }
        QueryStringQuery query = SearchQuery.queryString(queryString);
        if (ids.size() != 0) {
            DocIdQuery docIdQuery = SearchQuery.docId(ids.toArray(new String[0]));
            if (!queryString.isEmpty()) {
                return SearchQuery.conjuncts(query, docIdQuery);
            }
            return docIdQuery;
        }
        return query;
    }

    @ScanConsistency(query = QueryScanConsistency.NOT_BOUNDED)
    default Flux<T> search(String queryString) {
        return findAllById(toIds(searchIds(queryString)));
    }

    @ScanConsistency(query = QueryScanConsistency.NOT_BOUNDED)
    default Flux<T> search(String queryString, Pageable pageable) {
        return findAllById(toIds(searchIds(queryString, pageable)));
    }

    @Query(FIND_IDS_QUERY + " AND " + SEARCH_CONDITION)
    @ScanConsistency(query = QueryScanConsistency.NOT_BOUNDED)
    Flux<T> searchIds(String queryString);

    @Query(FIND_IDS_QUERY + " AND " + SEARCH_CONDITION)
    @ScanConsistency(query = QueryScanConsistency.NOT_BOUNDED)
    Flux<T> searchIds(String queryString, Pageable pageable);
}
