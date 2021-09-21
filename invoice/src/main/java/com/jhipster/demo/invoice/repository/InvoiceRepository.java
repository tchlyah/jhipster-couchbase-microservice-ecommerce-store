package com.jhipster.demo.invoice.repository;

import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.searchQuery;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.invoice.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends JHipsterCouchbaseRepository<Invoice, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", (SELECT `shipment`.*, meta(`shipment`).id FROM `shipments` `shipment`) as `shipments`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT NEST #{#n1ql.bucket} `shipments` ON KEYS b.`shipments`";

    default Page<Invoice> findAll(Pageable pageable) {
        return new PageImpl<>(findAllBy(pageableStatement(pageable, "b")), pageable, count());
    }

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Invoice> findAllBy(String pageableStatement);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Invoice> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Optional<Invoice> findById(String id);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} AND SEARCH(b, #{[0]}) #{[1]}")
    List<Invoice> search(String queryString, String pageableStatement);

    default List<Invoice> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Page<Invoice> search(String queryString, Pageable pageable) {
        return new PageImpl<>(search(searchQuery(queryString).toString(), pageableStatement(pageable, "b")));
    }
}
