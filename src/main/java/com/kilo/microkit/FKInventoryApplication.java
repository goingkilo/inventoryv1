package com.kilo.microkit;

import com.kilo.microkit.api.model.InventoryItem;
import com.kilo.microkit.health.RetailHealthCheck;
import com.kilo.microkit.resources.FKResource;
import com.kilo.microkit.api.job.FKInventoryLoaderJob;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

import javax.ws.rs.client.Client;
import java.util.Map;

public class FKInventoryApplication extends Application<FKInventoryConfiguration> {

    @Override
    public String getName() {
        return "Retail inventory from Inventory";
    }

    private final HibernateBundle<FKInventoryConfiguration> hibernate = new HibernateBundle<FKInventoryConfiguration>(InventoryItem.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(FKInventoryConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<FKInventoryConfiguration> bootstrap) {

        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new ViewBundle<FKInventoryConfiguration>() {
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(FKInventoryConfiguration config) {
                return config.getViewRendererConfiguration();
            }
        });
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final FKInventoryConfiguration configuration,
                    final Environment environment) {

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .build(getName());

        environment.jersey().register(new FKResource(client));

        final RetailHealthCheck basicHealthCheck = new RetailHealthCheck();
        environment.healthChecks().register( "basic", basicHealthCheck );

        environment.admin().addTask(new FKInventoryLoaderJob());

        System.out.println( " ##### System has started #####");
    }

    public static void main(final String[] args) throws Exception {
        new FKInventoryApplication().run(args);
    }

}
