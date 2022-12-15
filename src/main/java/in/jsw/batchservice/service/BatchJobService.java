package in.jsw.batchservice.service;

import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;
import in.jsw.batchservice.model.batch.BatchJobInstance;

public interface BatchJobService {
    BatchJobInstance createJobInstance(BatchJob batchJob, Long triggeredBy);
    void updateJobInstance(
            BatchJobInstance batchJobInstance,
            BatchJobStatus status,
            Long executionTime,
            long notifyDeleteUpdateCount,
            String description);
}
