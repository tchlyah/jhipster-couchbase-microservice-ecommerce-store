package com.jhipster.demo.invoice.repository;

import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.pageableStatement;
import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.searchQuery;

import com.couchbase.client.java.query.QueryScanConsistency;
import com.jhipster.demo.invoice.domain.Shipment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ScanConsistency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Couchbase repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends JHipsterCouchbaseRepository<Shipment, String> {
    String SELECT =
        "SELECT meta(b).id as __id, meta(b).cas as __cas, b.*" +
        ", (SELECT `invoice`.*, meta(`invoice`).id)[0] as `invoice`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN #{#n1ql.bucket} `invoice` ON KEYS b.`invoice`";

    default Page<Shipment> findAll(Pageable pageable) {
        return new PageImpl<>(findAllBy(pageableStatement(pageable, "b")), pageable, count());
    }

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Shipment> findAllBy(String pageableStatement);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Shipment> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    Optional<Shipment> findById(String id);

    @Query(SELECT + JOIN + " WHERE b.#{#n1ql.filter} AND SEARCH(b, #{[0]}) #{[1]}")
    List<Shipment> search(String queryString, String pageableStatement);

    default List<Shipment> search(String queryString) {
        return search(searchQuery(queryString).toString(), "");
    }

    default Page<Shipment> search(String queryString, Pageable pageable) {
        return new PageImpl<>(search(searchQuery(queryString).toString(), pageableStatement(pageable, "b")));
    }
}
