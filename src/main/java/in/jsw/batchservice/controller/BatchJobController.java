package in.jsw.batchservice.controller;

import in.jsw.batchservice.model.customer.JobParamsRequest;
import in.jsw.batchservice.service.JobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch-service")
public class BatchJobController {
    @Autowired
    JobService jobService;

    @Autowired
    JobOperator jobOperator;

    @PostMapping("/start/{jobName}")
    public ResponseEntity<String> startJob(@PathVariable String jobName,
                                           @RequestBody List<JobParamsRequest> JobParamsRequestList) throws Exception {
        jobService.startJob(jobName,JobParamsRequestList);
        return ResponseEntity.ok("Job Started..");
    }

    @GetMapping("/stop/{jobExecutionId}")
    public ResponseEntity<String> stopJob(@PathVariable long jobExecutionId) {
        try {
            jobOperator.stop(jobExecutionId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Job Stopped..");
    }

}
