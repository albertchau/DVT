package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.utils.containers.ResultSets;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

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
    public final void openConnections() throws DataProviderException {
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
            String connectionUrl = getConnectionUrl();
            try {
                if (isNull(user) || isNull(password)) {
                    logger.info("Trying to connect using passwordless(username and/or password not set) login to database with connection url = [" + connectionUrl + "].");
                    connection = DriverManager.getConnection(connectionUrl);
                } else {
                    logger.info("Trying to connect using username/password to login to database with connection url = [" + connectionUrl + "].");
                    connection = DriverManager.getConnection(connectionUrl, user, password);
                }
                logger.info("Successfully connected to [" + connectionUrl + "].");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("Failed to connect to " + connectionUrl + " because: " + e.getMessage() + ". Here is dump: " + this.toString());
                throw new DataProviderException("Failed to connect to " + connectionUrl + " because: " + e.getMessage() + ". Here is dump: " + this.toString());
            }
            connections.add(connection);
        }
    }

    public String getConnectionUrl() throws DataProviderException {
        if (nonNull(url)) {
            return url;
        } else {
            if (Stream.of(host, port, database).anyMatch(Objects::isNull)) {
                logger.error("URL is null and Host/Port/Database are null - Cannot construct url connection.");
                throw new DataProviderException("URL is null and Host/Port/Database are null - Cannot construct url connection.");
            }
            String rtn = "";
            switch (vendor) {
                case HIVE_2:
                    return String.format("%s%s:%s/%s", "jdbc:hive2://", host, port, database);
                case HIVE_1:
                    return String.format("%s%s:%s/%s", "jdbc:hive://", host, port, database);
                case MYSQL:
                    return String.format("%s%s:%s/%s", "jdbc:mysql://", host, port, database);
                case SQL_SERVER:
                    return String.format("%s%s:%s/%s", "jdbc:sqlserver://", host, port, database);
                case ORACLE:
                    return String.format("%s%s:%s/%s", "jdbc:oracle:thin:@//", host, port, database);
                case VERTICA:
                    return String.format("%s%s:%s/%s", "jdbc:vertica://", host, port, database);
                case NETEZZA:
                    return String.format("%s%s:%s/%s", "jdbc:netezza://", host, port, database);
            }
            return rtn;
        }
    }

    @Override
    public final void closeConnections() {
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

    public final List<Connection> getConnections() throws DataProviderException {
        if (connections == null) {
            throw new DataProviderException("Have not opened up connections yet.");
        }
        return connections;
    }


    public final ResultSet getData(String query) throws DataProviderException {
        if (connections == null) { //todo probably can call it for them though...
            logger.error("You need to openConnections() before calling this getData() method.");
            throw new DataProviderException("You need to openConnections() before calling this getData() method.");
        }
        if (connections.size() < 1) {
            logger.error("There are no active connections open.");
            throw new DataProviderException("There are no active connections open.");
        }
        List<ResultSet> resultSetList = connections.stream()
                .map(c -> {
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = c.createStatement();
                        rs = stmt.executeQuery(query);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        logger.error("SQLException: " + ex.getMessage());
                    }
                    return rs;
                })
                .filter(Objects::nonNull)
                .collect(toList());
        if (resultSetList.size() < 1) {
            logger.error("Could not get any resultSets. they were all null");
            throw new DataProviderException("Could not get any resultSets. they were all null");
        }
        return new ResultSets(resultSetList);
    }


    @Override
    public final void close() {
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
