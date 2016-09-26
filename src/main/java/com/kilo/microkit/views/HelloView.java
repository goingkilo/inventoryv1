package com.kilo.microkit.views;

import io.dropwizard.views.View;

/**
 * Created by kraghunathan on 9/23/16.
 */
public class HelloView extends View {

    private String hello;

    public HelloView(String message) {
        super("hello.ftl");
        this.hello = message;
    }

    public String getHello() {

        return hello;
    }
}
