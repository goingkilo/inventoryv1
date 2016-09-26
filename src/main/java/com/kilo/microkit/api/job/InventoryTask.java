package com.kilo.microkit.api.job;

import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;

/**
 * Created by kraghunathan on 9/17/16.
 */
public class InventoryTask extends Task {

    public static final String LOADER   = "loader";
    public static final String START    = "start";
    public static final String STOP     = "stop";

    InventoryLoaderJob job;

    public InventoryTask() {
        super("loader");
    }

    public InventoryTask(String name, InventoryLoaderJob job) {

        super(name);
        this.job = job;
    }

    public void start() throws Exception {
        job.run("job1", "jobGroup1", "trigger1", "triggerGroup1", 20);
    }

    public void stop() throws Exception {
        job.getScheduler().shutdown();
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
