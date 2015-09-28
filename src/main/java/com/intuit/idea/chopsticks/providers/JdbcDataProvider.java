package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Copyright 2015
 *
 * @author albert
 */
abstract public class JdbcDataProvider implements DataProvider {
    protected final VendorType vendor;
    protected final String host;
    protected final String port;
    protected final String url;
    protected final String user;
    protected final String password;
    protected final String database;
    protected final String hivePrincipal;
    protected final String name;
    protected final List<? extends JdbcDataProvider> shards;
    protected List<Connection> connections;
    private Logger logger = LoggerFactory.getLogger(JdbcDataProvider.class);

    public JdbcDataProvider(VendorType vendor, String host, String port, String url, String user, String password, String database, String hivePrincipal, String name, List<? extends JdbcDataProvider> shards) {
        this.vendor = vendor;
        this.host = host;
        this.port = port;
        this.url = url;
        this.user = user;
        this.password = password;
        this.database = database;
        this.hivePrincipal = hivePrincipal;
        this.name = name;
        this.shards = shards;
        connections = null;
    }

    @Override
    public void openConnections() throws DataProviderException {
        if (connections == null) {
            connections = new ArrayList<>();
        }

        if (shards != null && !shards.isEmpty()) {
            shards.stream()
                    .flatMap(shard -> {
                        try {
                            shard.openConnections();
                            return shard.getConnections().stream();
                        } catch (DataProviderException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .forEach(connections::add);
        } else {
            Connection connection;
            try {
                connection = DriverManager.getConnection(url, user, password);
                logger.info("Connected to " + url);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("SqlException. Could not connect to " + url + " because: " + e.getMessage() + ". Here is dump: " + this.toString());
                throw new DataProviderException("SqlException. Could not connect to " + url + " because: " + e.getMessage() + ". Here is dump: " + this.toString());
            }
            connections.add(connection);
        }
    }

    @Override
    public void closeConnections() {
        if (connections != null) {
            connections.stream()
                    .filter(Objects::nonNull)
                    .forEach((connection) -> {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        }
        connections = null;
    }

    public List<Connection> getConnections() throws DataProviderException {
        if (connections == null) {
            throw new DataProviderException("Have not opened up connections yet.");
        }
        return connections;
    }

    @Override
    public void close() {
        logger.info("Closing Connections.");
        closeConnections();
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final VendorType getVendorType() {
        return vendor;
    }

    @Override
    public String toString() {
        return "JdbcDataProvider{" +
                "vendor='" + vendor + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", hivePrincipal='" + hivePrincipal + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
