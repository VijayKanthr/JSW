package in.jsw.batchservice.service;

import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;
import in.jsw.batchservice.model.batch.BatchJobInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.Instant;

@Component
public class BatchJobHelper {


    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    BatchJobService batchJobService;

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    public void create_updateJobInstance(Job jobName, BatchJob batchJob, JobParameters jobParameters) {

        BatchJobInstance batchJobInstance = null;

        long count = 0;
        StopWatch timer = new StopWatch();
        Long triggeredByOrJobId = 0L;

        try {

            JobExecution jobExecution = null;

            timer.start();
            triggeredByOrJobId = batchJob.getId();

            // Create an instance of BatchJob for Audit
            batchJobInstance = batchJobService.createJobInstance(
                    batchJob, triggeredByOrJobId);

            jobExecution = jobLauncher.run(jobName, jobParameters);

            logger.info("Job Execution ID = " + jobExecution.getId());
            logger.info(
                    "BatchJob: schedulerJobName={} started on launchDate={} with parameters={}",
                    batchJob,
                    Instant.now().toString(),
                    jobParameters);
            timer.stop();

            batchJobService.updateJobInstance(batchJobInstance, BatchJobStatus.COMPLETED,
                    timer.getTotalTimeMillis(),count,"JOB COMPLETED.");
            logger.info(
                    "BatchJob: schedulerJobName={} completed successfully, executionTime={} seconds",
                   batchJob,
                    timer.getTotalTimeSeconds());

             logger.info("Job Execution ID = " + jobExecution.getId());

        }
        catch (Exception e){
            logger.error("Exception while starting job  :  " + e.getMessage());
        }

    }
}
