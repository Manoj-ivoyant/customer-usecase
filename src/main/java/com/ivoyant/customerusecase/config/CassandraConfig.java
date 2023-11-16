package com.ivoyant.customerusecase.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories (
        basePackages = "com.ivoyant.customerusecase.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "mykeyspace";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.ivoyant.customerusecase.entity"};
    }

}