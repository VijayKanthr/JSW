package in.jsw.batchservice.service;


import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;
import in.jsw.batchservice.model.batch.BatchJobInstance;
import in.jsw.batchservice.model.customer.JobParamsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job readCSVJob;

    @Autowired
    BatchJobService batchJobService;



    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Async
    public void startJob(String jobName, List<JobParamsRequest> JobParamsRequestList) {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        JobParamsRequestList.stream().forEach(jobParamsRequest -> {
            params.put(jobParamsRequest.getParamKey(),new JobParameter(jobParamsRequest.getParamValue()));
        });
        JobParameters jobParameters = new JobParameters(params);

        BatchJobInstance batchJobInstance = null;

        long count = 0;
        StopWatch timer = new StopWatch();
        Long triggeredByOrJobId = 0L;

        try {
            JobExecution jobExecution = null;
            if(jobName.equals("readCsv")) {
                timer.start();
                triggeredByOrJobId = BatchJob.IMPORT_CUSTOMERS_CSV_DB.getId();

                // Create an instance of BatchJob for Audit
                batchJobInstance = batchJobService.createJobInstance(
                                BatchJob.IMPORT_CUSTOMERS_CSV_DB, triggeredByOrJobId);

                jobExecution = jobLauncher.run(readCSVJob, jobParameters);

                logger.info("Job Execution ID = " + jobExecution.getId());
                logger.info(
                        "BatchJob: schedulerJobName={} started on launchDate={} with parameters={}",
                        BatchJob.IMPORT_CUSTOMERS_CSV_DB.name(),
                        Instant.now().toString(),
                        jobParameters);
                timer.stop();
                batchJobService.updateJobInstance(batchJobInstance, BatchJobStatus.COMPLETED,
                        timer.getTotalTimeMillis(),count,"IMPORT CUSTOMERS FROM CSV TO DB COMPLETED.");
                logger.info(
                        "BatchJob: schedulerJobName={} completed successfully, executionTime={} seconds",
                        BatchJob.IMPORT_CUSTOMERS_CSV_DB.name(),
                        timer.getTotalTimeSeconds());
            }
            logger.info("Job Execution ID = " + jobExecution.getId());
        }catch(Exception e) {
            logger.error("Exception while starting job  :  " + e.getMessage());
        }
    }

}
