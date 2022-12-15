package in.jsw.batchservice.service;

import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;
import in.jsw.batchservice.model.batch.BatchJobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

//@Service
public class BatchJobScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job launchJob;

    @Autowired
    BatchJobService batchJobService;

    private static Logger logger = LoggerFactory.getLogger(BatchJobScheduler.class);

    //@Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void secondJobStarter() {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        JobParameters jobParameters = new JobParameters(params);

        BatchJobInstance batchJobInstance = null;
        long count = 0;
        StopWatch timer = new StopWatch();
        Long triggeredByOrJobId = 0L;
        try {
            timer.start();
            batchJobInstance= batchJobService.createJobInstance(BatchJob.ADD_CUSTOMERS_JOB,90L);

            JobExecution jobExecution =
                    jobLauncher.run(launchJob, jobParameters);
            logger.info("Job Execution ID = " + jobExecution.getId());
            logger.info(
                    "BatchJob: schedulerJobName={} started on launchDate={} with parameters={}",
                    BatchJob.ADD_CUSTOMERS_JOB.name(),
                    Instant.now().toString(),
                    jobParameters);
            timer.stop();
            batchJobService.updateJobInstance(batchJobInstance, BatchJobStatus.COMPLETED,
                    timer.getTotalTimeMillis(),count,"ADD CUSTOMERS FROM CSV TO DB COMPLETED.");
            logger.info(
                    "BatchJob: schedulerJobName={} completed successfully, executionTime={} seconds",
                    BatchJob.ADD_CUSTOMERS_JOB.name(),
                    timer.getTotalTimeSeconds());

        }catch(Exception e) {
            logger.error("Exception while starting job  :  " + e.getMessage());
        }
    }

}
