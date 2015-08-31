package com.intuit.idea.chopsticks.stuff;
import java.sql.*;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class JdbcDataProvider implements DataProvider {

    private String vendor;
    private String host;
    private String port;
    private String url;
    private String user;
    private String password;
    private String database;
    private String hivePrincipal;

    private Connection connection;


    public JdbcDataProvider(String vendor, String host, String port, String url, String user, String password, String database, String hivePrincipal) {
        this.vendor = vendor;
        this.host = host;
        this.port = port;
        this.url = url;
        this.user = user;
        this.password = password;
        this.database = database;
        this.hivePrincipal = hivePrincipal;
    }


    @Override
    public List getData() {
        initializeConnection();
        return null;
    }

    private void initializeConnection() {
        try {
            connection = DriverManager.getConnection("");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List getExistenceData() {
        return null;
    }

    @Override
    public List getPrimaryKeys() {
        return null;
    }

    @Override
    public Integer getCountData() {
        return null;
    }

    @Override
    public DataProviderType getType() {
        return null;
    }

    @Override
    public String getDataQuery() {
        return null;
    }

    @Override
    public String getExistenceQuery() {
        return null;
    }

    @Override
    public String getCountQuery() {
        return null;
    }

    @Override
    public List getMetadata() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
