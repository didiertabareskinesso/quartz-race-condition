package quartz;

import java.util.UUID;
import javax.transaction.Transactional;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultJob implements Job {

    @Autowired
    private JobSchedulerService schedulerService;

    @Transactional
    public void execute(JobExecutionContext context) {
        log.info("Executing job DefaultJob...");
        Assert.state(context.getMergedJobDataMap().containsKey("id"),
                     "Unable to locate id in job execution context");
        UUID id = (UUID) context.getMergedJobDataMap().get("id");
        log.info("Job DefaultJob executed. {}", id);
        // This re-schedule produces the error after a given time
        // org.quartz.JobPersistenceException: Couldn't acquire next trigger: Couldn't retrieve trigger: No record found for selection of Trigger with key: 'DEFAULT.3020bda7-be51-4736-b0aa-5cb400706b75' and statement: SELECT * FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = 'scheduler' AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ?
        schedulerService.schedule(new JobMetadata(id, JobMetadata.Status.CREATED));
    }
}
