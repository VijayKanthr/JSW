package in.jsw.batchservice.repository.batch;

import in.jsw.batchservice.model.batch.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobRepository extends JpaRepository<BatchJobInstance,Long>{
}
