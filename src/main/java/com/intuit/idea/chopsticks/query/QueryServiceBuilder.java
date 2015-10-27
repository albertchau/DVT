package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.providers.VendorType;
import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public class QueryServiceBuilder {
    private static Logger logger = LoggerFactory.getLogger(QueryServiceBuilder.class);
    private String schema = null;
    private List<String> includedColumns = new ArrayList<>();
    private List<String> excludedColumns = new ArrayList<>();
    private List<WhereClause> whereClauses = new ArrayList<>();
    private Integer fetchAmount = 0;
    private OrderDirection orderDirection = OrderDirection.ASCENDING;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    public QueryServiceBuilder setSchema(String schema) {
        this.schema = schema;
        return this;
    }

    public QueryServiceBuilder setIncludedColumns(List<String> includedColumns) {
        this.includedColumns = new ArrayList<>(includedColumns);
        return this;
    }

    public QueryServiceBuilder setWhereClauses(List<WhereClause> whereClauses) {
        this.whereClauses = new ArrayList<>(whereClauses);
        return this;
    }

    public QueryServiceBuilder setExcludedColumns(List<String> excludedColumns) {
        this.excludedColumns = new ArrayList<>(excludedColumns);
        return this;
    }

    public QueryServiceBuilder setFetchAmount(Integer fetchAmount) {
        this.fetchAmount = fetchAmount;
        return this;
    }

    public QueryServiceBuilder setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }

    public QueryServiceBuilder setOrderDirection(OrderDirection orderDirection) {
        this.orderDirection = orderDirection;
        return this;
    }

    public QueryService build(String tableName, VendorType vendor, TestType testType) {
        switch (testType) {
            case FULL:
                break;
            case HISTORIC:
                long count = whereClauses.stream()
                        .filter(wc -> wc.type() == DateTime.class)
                        .filter(wc -> wc.getLowerBound() != null || wc.getUpperBound() != null)
                        .count();
                if (count < 1) {
                    logger.error("Historic test case specified, however there is no where clause that specifies a matching date column.");
                    throw new QueryCreationError("Historic test case specified, however there is no where clause that specifies a matching date column.");
                }
                break;
            case INCREMENTAL:
                count = whereClauses.stream()
                        .filter(wc -> wc.type() == DateTime.class)
                        .filter(wc -> wc.getLowerBound() != null)
                        .count();
                if (count < 1) {
                    logger.error("Incremental test case specified, however there is no where clause that specifies a matching date column.");
                    throw new QueryCreationError("Incremental test case specified, however there is no where clause that specifies a matching date column.");
                }
                break;
        }
        switch (vendor) {
            case HIVE_2:
            case HIVE_1:
                return new HiveQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
            case MYSQL:
                return new MySqlQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
            case SQL_SERVER:
                return new SqlServerQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
            case ORACLE:
                return new OracleQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
            case VERTICA:
                return new VerticaQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
            case NETEZZA:
                return new NetezzaQueryService(tableName, schema, includedColumns, excludedColumns, fetchAmount, testType, whereClauses, orderDirection, dateTimeFormatter);
        }
        return null;
    }
}
