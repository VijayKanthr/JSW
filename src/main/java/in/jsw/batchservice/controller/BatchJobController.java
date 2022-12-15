package in.jsw.batchservice.controller;

import in.jsw.batchservice.model.customer.JobParamsRequest;
import in.jsw.batchservice.service.JobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch-jobs")
public class BatchJobController {
    @Autowired
    JobService jobService;

    @Autowired
    JobOperator jobOperator;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody List<JobParamsRequest> JobParamsRequestList) throws Exception {
        jobService.startJob(jobName,JobParamsRequestList);
        return "Job Started...";
    }

    @GetMapping("/stop/{jobExecutionId}")
    public String stopJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Job Stopped...";
    }

}
