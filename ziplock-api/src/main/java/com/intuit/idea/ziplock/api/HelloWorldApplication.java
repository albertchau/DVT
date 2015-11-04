package com.intuit.idea.ziplock.api;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */

import com.intuit.idea.ziplock.api.core.*;
import com.intuit.idea.ziplock.api.db.*;
import com.intuit.idea.ziplock.api.health.TemplateHealthCheck;
import com.intuit.idea.ziplock.api.resources.*;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    private final HibernateBundle<HelloWorldConfiguration> hibernate = new HibernateBundle<HelloWorldConfiguration>(
            Person.class,
            Job.class,
            Datasource.class,
            Relation.class,
            Field.class,
            Dataset.class,
            Run.class,
            Reporter.class,
            Config.class,
            RelationMap.class,
            RelationMapConfig.class,
            Comparison.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };


    public static void main(String[] args) throws Exception {
        String [] ar = new String[] {"server", "/Users/albert/Developing/Quant/Chopsticks/ziplock-api/example.yaml"};
        new HelloWorldApplication().run(ar);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment) {
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        final PersonDAO personDAO = new PersonDAO(hibernate.getSessionFactory());
        final JobDAO jobDAO = new JobDAO(hibernate.getSessionFactory());
        final DatasourceDAO datasourceDAO = new DatasourceDAO(hibernate.getSessionFactory());
        final DatasetDAO datasetDAO = new DatasetDAO(hibernate.getSessionFactory());
        final FieldDAO fieldDAO = new FieldDAO(hibernate.getSessionFactory());
        final RelationDAO relationDAO = new RelationDAO(hibernate.getSessionFactory());
        final ReporterDAO reporterDAO = new ReporterDAO(hibernate.getSessionFactory());
        final RunDAO runDAO = new RunDAO(hibernate.getSessionFactory());
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(new PersonResource(personDAO));
        environment.jersey().register(new JobResource(jobDAO));
        environment.jersey().register(new DatasourceResource(datasourceDAO));
        environment.jersey().register(new DatasetResource(datasetDAO));
        environment.jersey().register(new FieldResource(fieldDAO));
        environment.jersey().register(new RelationResource(relationDAO));
        environment.jersey().register(new ReporterResource(reporterDAO));
        environment.jersey().register(new RunResource(runDAO));
        environment.jersey().register(new DatasourceResource(datasourceDAO));
        environment.jersey().register(new DatasourceResource(datasourceDAO));
    }
}