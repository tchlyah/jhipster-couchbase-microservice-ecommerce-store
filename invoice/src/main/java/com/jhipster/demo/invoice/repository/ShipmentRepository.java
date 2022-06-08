package com.jhipster.demo.invoice.repository;

import static com.jhipster.demo.invoice.domain.Shipment.TYPE_NAME;
import static com.jhipster.demo.invoice.repository.JHipsterCouchbaseRepository.pageableStatement;

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
        ", OBJECT_ADD(`invoice`, 'id', meta(`invoice`).id) AS `invoice`" +
        " FROM #{#n1ql.bucket} b";

    String JOIN = " LEFT JOIN `invoice` `invoice` ON KEYS b.`invoice`";

    String WHERE = " WHERE b.type = '" + TYPE_NAME + "'";

    default Page<Shipment> findAll(Pageable pageable) {
        return new PageImpl<>(findAll(pageableStatement(pageable, "b")), pageable, count());
    }

    @Query(SELECT + JOIN + WHERE + " #{[0]}")
    @ScanConsistency(query = QueryScanConsistency.REQUEST_PLUS)
    List<Shipment> findAll(String pageableStatement);

    @Query(SELECT + JOIN + WHERE)
    List<Shipment> findAll();

    @Query(SELECT + " USE KEYS $1" + JOIN)
    Optional<Shipment> findById(String id);
}
