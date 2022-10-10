package quartz;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "job-scheduler.polling")
@Validated
@Getter
@Setter
@ToString
public class JobSchedulerProperties {

    private int defaultIntervalSeconds;

    private String confirmedStatusIntervalSeconds;

    private int waitingDeliveryStatusIntervalSeconds;
}
