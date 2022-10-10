Start the services in different ports (8080, 8081). 

Schedule a job in the first instance:
````shell
curl --location --request POST 'http://localhost:8080/scheduler/'
````
Schedule a job in the second instance:
````shell
curl --location --request POST 'http://localhost:8081/scheduler/'
````
After a while the error will appear:
````java
org.quartz.JobPersistenceException: Couldn't acquire next trigger: Couldn't retrieve trigger: No record found for selection of Trigger with key: 'DEFAULT.42f2dc40-7462-426d-bc2c-4e9e3a0da4d4' and statement: SELECT * FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = 'scheduler' AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ?
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.acquireNextTrigger(JobStoreSupport.java:2923) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport$41.execute(JobStoreSupport.java:2805) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport$41.execute(JobStoreSupport.java:2803) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.executeInNonManagedTXLock(JobStoreSupport.java:3864) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.acquireNextTriggers(JobStoreSupport.java:2802) ~[quartz-2.3.2.jar:na]
	at org.quartz.core.QuartzSchedulerThread.run(QuartzSchedulerThread.java:287) ~[quartz-2.3.2.jar:na]
Caused by: org.quartz.JobPersistenceException: Couldn't retrieve trigger: No record found for selection of Trigger with key: 'DEFAULT.42f2dc40-7462-426d-bc2c-4e9e3a0da4d4' and statement: SELECT * FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = 'scheduler' AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ?
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.retrieveTrigger(JobStoreSupport.java:1538) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.acquireNextTrigger(JobStoreSupport.java:2854) ~[quartz-2.3.2.jar:na]
	... 5 common frames omitted
Caused by: java.lang.IllegalStateException: No record found for selection of Trigger with key: 'DEFAULT.42f2dc40-7462-426d-bc2c-4e9e3a0da4d4' and statement: SELECT * FROM QRTZ_SIMPLE_TRIGGERS WHERE SCHED_NAME = 'scheduler' AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ?
	at org.quartz.impl.jdbcjobstore.SimpleTriggerPersistenceDelegate.loadExtendedTriggerProperties(SimpleTriggerPersistenceDelegate.java:110) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.StdJDBCDelegate.selectTrigger(StdJDBCDelegate.java:1819) ~[quartz-2.3.2.jar:na]
	at org.quartz.impl.jdbcjobstore.JobStoreSupport.retrieveTrigger(JobStoreSupport.java:1536) ~[quartz-2.3.2.jar:na]
	... 6 common frames omitted

````