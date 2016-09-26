package com.kilo.microkit.api.job;

import com.kilo.microkit.api.dao.InventoryDAO;
import com.kilo.microkit.api.model.InventoryItem;
import com.kilo.microkit.api.util.FlipKart;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.ws.rs.client.Client;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

/**
 * Created by kraghunathan on 9/17/16.
 */
public class InventoryLoaderJob implements Job{

    private Client client;
    private InventoryDAO inventoryDAO;

    private Scheduler scheduler;

    public InventoryLoaderJob(){}

    public InventoryLoaderJob(InventoryDAO inventoryDAO, Client client) {
        this.inventoryDAO = inventoryDAO;
        this.client = client;
    }

    public void run(String jobName, String jobGroup, String triggerName, String triggerGroup , int timeInSeconds) {

        JobDetail jobDetail = JobBuilder.newJob(InventoryLoaderJob.class)
                .withIdentity(jobName, jobGroup).build();

        jobDetail.getJobDataMap().put( "X", client);
        jobDetail.getJobDataMap().put( "Y", inventoryDAO);

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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Hello Quartz!"  + (new Date()));

        client = (Client) jobExecutionContext.getJobDetail().getJobDataMap().get("X");
        inventoryDAO = (InventoryDAO) jobExecutionContext.getJobDetail().getJobDataMap().get("Y");

        List<InventoryItem> ret = null;
        try {
            ret = FlipKart.search(client, "radio", 10);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        inventoryDAO.saveMany(ret);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
