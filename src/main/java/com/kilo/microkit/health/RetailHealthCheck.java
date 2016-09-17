package com.kilo.microkit.health;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by kraghunathan on 9/17/16.
 */
public class RetailHealthCheck extends HealthCheck {

    public RetailHealthCheck() {

    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
