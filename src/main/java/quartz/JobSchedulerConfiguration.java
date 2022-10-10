package quartz;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobSchedulerConfiguration {

    private final JobSchedulerProperties properties;

    @Bean
    public JobSchedulerIntervals jobSchedulerIntervals() {
        return new JobSchedulerIntervals(properties);
    }

    public static class JobSchedulerIntervals {

        /**
         * Pattern to match configuration strings such as:
         * <ul>
         *     <li><code>DIRECT-MX-FACEBOOK:3</code>
         *     <li><code>DIRECT-MX-FACEBOOK:3,DIRECT-US-FACEBOOK:45,DIRECT-US-TRADE_DESK:12</code>
         *     <li><code>DIRECT-MX-FACEBOOK:30,DIRECT-US-TRADE_DESK:120</code>
         * </ul>
         */
        private static final Pattern STATUS_INTERVAL_CONFIGURATION_STRING =
            Pattern.compile("^(\\w)+-(\\w)+-(\\w)+:(\\d)+(,(\\w)+-(\\w)+-(\\w)+:(\\d)+)*$");

        private final Map<String, Integer>
            confirmedStatusIntervalSeconds = new HashMap<>();

        @Getter
        private final int defaultIntervalSeconds;

        @Getter
        private final int waitingDeliveryStatusIntervalSeconds;

        public JobSchedulerIntervals(JobSchedulerProperties properties) {
            defaultIntervalSeconds = properties.getDefaultIntervalSeconds();
            waitingDeliveryStatusIntervalSeconds = properties.getWaitingDeliveryStatusIntervalSeconds();
            log.info("Configured default interval-seconds :: {}", defaultIntervalSeconds);
            log.info("Configured confirmed-status interval-seconds :: {}", confirmedStatusIntervalSeconds);
        }
    }
}
