package in.jsw.batchservice.service;

import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;

import in.jsw.batchservice.model.batch.BatchJobInstance;
import in.jsw.batchservice.repository.batch.BatchJobRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Setter
@Service
public class BatchJobServiceImpl implements BatchJobService{

    @Autowired
    BatchJobRepository batchJobRepository;

    public BatchJobServiceImpl() {
    }

    @Override
    @Transactional
    public BatchJobInstance createJobInstance(BatchJob batchJob, Long triggeredBy) {

        BatchJobInstance batchJobInstance =  BatchJobInstance.builder()
                        .schedulerJobId(batchJob.getId())
                        .schedulerJobName((BatchJob.ADD_CUSTOMERS_JOB))
                        .status(BatchJobStatus.EXECUTING)
                        .startTime(Instant.now())
                         .launchDate(LocalDateTime.now().toString() )
                        .triggeredBy(triggeredBy)
                        .build();

        return batchJobRepository.save(batchJobInstance);
    }

    @Override
    public void updateJobInstance(BatchJobInstance batchJobInstance, BatchJobStatus status, Long executionTime, long notifyDeleteUpdateCount, String description) {
        if (batchJobInstance != null) {
            batchJobInstance.setEndTime(Instant.now());
            batchJobInstance.setStatus(status);
            batchJobInstance.setExecutionTimeMillis(executionTime);
            batchJobInstance.setCount(notifyDeleteUpdateCount);
            batchJobInstance.setDescription(description);

            batchJobRepository.save(batchJobInstance);

        }
    }
}
