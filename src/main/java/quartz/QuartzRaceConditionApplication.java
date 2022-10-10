package quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableRetry
//@EnableIntegration
@EnableScheduling
public class QuartzRaceConditionApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzRaceConditionApplication.class, args);
    }
}
