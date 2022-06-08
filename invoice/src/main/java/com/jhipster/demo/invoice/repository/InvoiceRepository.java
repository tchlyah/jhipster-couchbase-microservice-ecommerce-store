package com.jhipster.demo.invoice.repository;

import static com.jhipster.demo.invoice.domain.Invoice.TYPE_NAME;
import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.pageableStatement;

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
        ", ARRAY OBJECT_ADD(item, 'id', meta(item).id) FOR item IN `shipments` END AS `shipments`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT NEST `shipment` `shipments` ON KEYS b.`shipments`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Page<Invoice> findAll(Pageable pageable) {
        return new PageImpl<>(findAll(pageableStatement(pageable, "b")), pageable, count());
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Invoice> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    List<Invoice> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Optional<Invoice> findById(String id);
}
