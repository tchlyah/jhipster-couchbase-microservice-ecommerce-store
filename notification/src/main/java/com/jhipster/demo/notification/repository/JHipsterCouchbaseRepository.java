package com.jhipster.demo.notification.repository;

import static java.lang.String.format;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.search.SearchQuery;
import com.couchbase.client.java.search.queries.DocIdQuery;
import com.couchbase.client.java.search.queries.QueryStringQuery;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Couchbase specific {@link org.springframework.data.repository.Repository} interface uses N1QL for all requests.
 */
@NoRepositoryBean
public interface JHipsterCouchbaseRepository<T, ID> extends CouchbaseRepository<T, ID> {
    static String pageableStatement(Pageable pageable) {
        return pageableStatement(pageable, "");
    }

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
        return new Query().limit(pageable.getPageSize()).skip(pageable.getOffset()).with(sort).export();
    }

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

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<T> findAll();

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<T> findAll(Sort sort);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Page<T> findAll(Pageable pageable);

    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    void deleteAll();
}
