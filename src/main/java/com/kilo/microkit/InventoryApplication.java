package com.kilo.microkit;

import com.kilo.microkit.db.dao.CategoryDAO;
import com.kilo.microkit.db.dao.ProductDAO;
import com.kilo.microkit.db.model.Category;
import com.kilo.microkit.db.model.Product;
import com.kilo.microkit.health.RetailHealthCheck;
import com.kilo.microkit.resources.RetailResource;
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

public class InventoryApplication extends Application<InventoryConfiguration> {

    @Override
    public String getName() {
        return "Retail inventory from Inventory";
    }

    private final HibernateBundle<InventoryConfiguration> hibernate = new HibernateBundle<InventoryConfiguration>(Product.class, Category.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(InventoryConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<InventoryConfiguration> bootstrap) {

        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new ViewBundle<InventoryConfiguration>());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final InventoryConfiguration configuration,
                    final Environment environment) {

        final ProductDAO productDAO = new ProductDAO(hibernate.getSessionFactory());
        final CategoryDAO categoryDAO     = new CategoryDAO(hibernate.getSessionFactory());

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration()).build(getName());

        environment.jersey().register(new RetailResource(productDAO, categoryDAO ,client));

        final RetailHealthCheck basicHealthCheck = new RetailHealthCheck();
        environment.healthChecks().register( "basic", basicHealthCheck );

        System.out.println( " ##### System has started #####");
    }

    public static void main(final String[] args) throws Exception {
        new InventoryApplication().run(args);
    }

}
