package quartz;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAutoConfiguration
@Slf4j
public class SpringQuartzSchedulerConfiguration {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    @PostConstruct
    public void init() {
        log.info("Spring Quartz scheduler configured...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        log.debug("Configuring Job factory");
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(
        JobDetail[] jobDetails, @Qualifier("quartzDataSource") DataSource quartzDataSource,
        SpringBeanJobFactory springBeanJobFactory, PlatformTransactionManager transactionManager)
    {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));
        //
        //        Properties quartzProperties = new Properties();
        //        quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.URL", url);
        //        quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.user", username);
        //        quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.password", password);
        //        quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.driver", "com.mysql.cj.jdbc.Driver");
        //        quartzProperties.setProperty("org.quartz.dataSource.quartzDataSource.provider", "hikaricp");
        //        quartzProperties.setProperty("org.quartz.jobStore.dataSource", "quartzDataSource");
        //        //org.quartz.jobStore.dataSource = quartzDataSource
        //        schedulerFactory.setQuartzProperties(quartzProperties);

        log.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory);
        schedulerFactory.setJobDetails(jobDetails);
        schedulerFactory.setDataSource(quartzDataSource);
        schedulerFactory.setTransactionManager(transactionManager);
        return schedulerFactory;
    }

    @Bean("quartzDataSource")
    @QuartzDataSource
    public DataSource quartzDataSource() {
        return DataSourceBuilder.create()
                                .driverClassName("com.mysql.cj.jdbc.Driver")
                                .url(url)
                                .username(username)
                                .password(password)
                                .type(com.zaxxer.hikari.HikariDataSource.class)
                                .build();
    }

    @Bean
    public JobDetail defaultJobDetail() {
        return createJob(
            DefaultJob.class,
            "Quartz default job",
            "Invoke Process job"
        );
    }

    private JobDetail createJob(Class<? extends Job> clazz, String identity, String description) {
        return new JobDetailImpl().getJobBuilder()
                                  .ofType(clazz)
                                  .withIdentity(identity)
                                  .withDescription(description)
                                  .requestRecovery()
                                  .storeDurably()
                                  .build();
    }
}
