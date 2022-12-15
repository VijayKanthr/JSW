package in.jsw.batchservice.model.batch;


import in.jsw.batchservice.batch.BatchJob;
import in.jsw.batchservice.batch.BatchJobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "batch_job")
public class BatchJobInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long batch_job_id_seq;

    @Column(name = "scheduler_job_id")
    private Long schedulerJobId;

    @Column(name = "scheduler_job_name")
    private BatchJob schedulerJobName;

    @Column(name = "status")
    private BatchJobStatus status;

    @Column(name = "triggered_by")
    private Long triggeredBy;

    @Column(name = "launch_date")
    private String launchDate;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "execution_time_millis")
    private Long executionTimeMillis;

    @Column(name = "count")
    private Long count;

    @Column(name = "description")
    private String description;
}
