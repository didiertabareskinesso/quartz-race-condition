package quartz;

import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/scheduler")
@AllArgsConstructor
public class SchedulerController {

    private final JobSchedulerService jobSchedulerService;

    @PostMapping("/")
    public void schedule() {
        jobSchedulerService.schedule(new JobMetadata(UUID.randomUUID(), JobMetadata.Status.CREATED));
    }
}
