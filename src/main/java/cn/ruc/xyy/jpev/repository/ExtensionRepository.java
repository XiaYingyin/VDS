package cn.ruc.xyy.jpev.repository;

import java.util.List;
import cn.ruc.xyy.jpev.model.ExtensionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//@Repository
//public interface ExtensionRepository extends JpaRepository<ExtensionSummary, Long> {
    //@Query(value = "SELECT e.extname AS \"Name\", t.etype AS \"Type\", e.extversion AS \"Version\", n.nspname AS \"Schema\", c.description AS \"Description\" FROM pg_catalog.pg_extension e LEFT JOIN pg_catalog.pg_namespace n ON n.oid = e.extnamespace LEFT JOIN pg_catalog.pg_description c ON c.objoid = e.oid AND c.classoid = 'pg_catalog.pg_extension'::pg_catalog.regclass LEFT JOIN ext_type t ON e.extname = t.extname WHERE t.etype = ?1", nativeQuery = true)
    //public List<ExtensionSummary> findByType();
//}

public class ExtensionRepository {
    int test = 0;
}
