package quartz;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSchedulerService {

    private static final String SCHEDULING_JOB_WAIT_TIME_LOG_MSG = "Scheduling job {}. Wait time: {} seconds";

    private final Scheduler scheduler;
    private final JobDetail defaultJobDetail;
    private final JobSchedulerConfiguration.JobSchedulerIntervals jobSchedulerIntervals;

    @PostConstruct
    public void init() {
    }

    public void schedule(JobMetadata jobMetadata) {
        try {
            if (jobMetadata.getStatus() == JobMetadata.Status.CREATED) {
                log.info(
                    "Scheduling job {} with status {}...",
                    jobMetadata.getId(),
                    jobMetadata.getStatus()
                );
                scheduler.scheduleJob(startNowTriggerWithDelay(jobMetadata));
            }
            else {
                log.info(
                    "Scheduling not required for jobMetadata {} with status {}. Ignoring....",
                    jobMetadata.getId(),
                    jobMetadata.getStatus()
                );
            }
        } catch (Exception ex) {
            log.error("Unable to schedule jobMetadata ".concat(jobMetadata.getId().toString()), ex);
            throw new JobException("Unexpected error", ex);
        }
    }

    private Trigger startNowTriggerWithDelay(JobMetadata jobMetadata) {
        log.info(
            SCHEDULING_JOB_WAIT_TIME_LOG_MSG,
            jobMetadata.getId(),
            jobSchedulerIntervals.getDefaultIntervalSeconds()
        );
        return newTrigger(
            jobMetadata,
            String.format(
                "Trigger to start in %s seconds for jobMetadata %s",
                jobSchedulerIntervals.getDefaultIntervalSeconds(),
                jobMetadata.getId()
            ),
            Date.from(
                Instant.now().plus(jobSchedulerIntervals.getDefaultIntervalSeconds(), SECONDS)
            )
        );
    }

    private Trigger newTrigger(JobMetadata jobMetadata, String description, Date startDate) {
        Assert.notNull(
            startDate,
            "startDate cannot be null"
        );
        return TriggerBuilder.newTrigger()
                             .forJob(defaultJobDetail)
                             .withIdentity(UUID.randomUUID().toString())
                             .withDescription(description)
                             .startAt(startDate)
                             .usingJobData(
                                 new JobDataMap(
                                     Map.of("id", jobMetadata.getId())
                                 )
                             )
                             .withSchedule(
                                 simpleSchedule()
                                     .withMisfireHandlingInstructionFireNow()
                                     .withRepeatCount(0)
                             )
                             .build();
    }
}
