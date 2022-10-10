package quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;
    private static ApplicationContext context;

    public static <T> T getBean(final Class<T> clazz) {
        return (T) context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        AutowiringSpringBeanJobFactory.context = context;
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected @NonNull Object createJobInstance(final @NonNull TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
