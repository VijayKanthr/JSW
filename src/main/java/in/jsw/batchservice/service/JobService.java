package in.jsw.batchservice.service;


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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job readCSVJob;



    private static Logger logger = LoggerFactory.getLogger(JobService.class);

    @Async
    public void startJob(String jobName, List<JobParamsRequest> JobParamsRequestList) {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("currentTime", new JobParameter(System.currentTimeMillis()));

        JobParamsRequestList.stream().forEach(jobParamsRequest -> {
            params.put(jobParamsRequest.getParamKey(),new JobParameter(jobParamsRequest.getParamValue()));
        });
        JobParameters jobParameters = new JobParameters(params);

        try {
            JobExecution jobExecution = null;
            if(jobName.equals("readCSV Job")) {
                jobExecution = jobLauncher.run(readCSVJob, jobParameters);
            }


            logger.info("Job Execution ID = " + jobExecution.getId());
        }catch(Exception e) {
            logger.error("Exception while starting job");
        }
    }

}
