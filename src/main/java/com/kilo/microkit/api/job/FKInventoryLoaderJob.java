package com.kilo.microkit.api.job;

import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by kraghunathan on 9/17/16.
     Flipkart Inventory Loader
        is a managed job
     read from flipkart site
     load into DB.


     at regular intervals

     administration mechanism : register as a task
     POST /task/loader  loader:start/stop
 *
 */
public class FKInventoryLoaderJob extends Task implements Job{

    public static final String LOADER = "loader";
    public static final String START = "start";
    private static final String STOP = "stop";


    private Scheduler scheduler;

    public FKInventoryLoaderJob() {
        this("loader");
    }
    protected FKInventoryLoaderJob(String name) {
        super(name);
    }

    public void run(String jobName, String jobGroup, String triggerName, String triggerGroup , int timeInSeconds) {

        JobDetail jobDetail = JobBuilder.newJob(FKInventoryLoaderJob.class)
                .withIdentity(jobName, jobGroup).build();

        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity( triggerName, triggerGroup)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(timeInSeconds)
                                .repeatForever())
                .build();

        scheduler = null;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob( jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

//    // lambda form is :
//    // public Job FKInventoryLoaderJob = context -> System.out.println("Hello Quartz!");
//    public Job FKInventoryLoaderJob = new Job () {
//        public void execute(JobExecutionContext context)
//                throws JobExecutionException {
//
//            System.out.println("Hello Quartz!");
//
//        }
//    };


    public void start() throws Exception {
        run("job1","jobGroup1","trigger1","triggerGroup1",20);
    }

    public void stop() throws Exception {
        scheduler.shutdown();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello Quartz!"  + (new Date()));
        // do something with db , fool !
    }

    @Override
    public void execute(ImmutableMultimap<String, String> immutableMultimap, PrintWriter printWriter) throws Exception {
        // send as x-www-form-urlencoded in postman
        if( ! immutableMultimap.get(LOADER).isEmpty()  ) {
            String loaderCommand = immutableMultimap.get(LOADER).asList().get(0);
            switch(loaderCommand) {
                case START:
                    start();
                    break;
                case STOP:
                    stop();
                    break;
                default:
                    throw new Exception("Unrecognized loader command");
            }
        }
    }
}
