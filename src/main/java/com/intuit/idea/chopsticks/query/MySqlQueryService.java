package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public class MySqlQueryService extends QueryServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(MySqlQueryService.class);

    public MySqlQueryService(String tableName,
                             String schema,
                             List<String> includedColumns,
                             List<String> excludedColumns,
                             List<Metadata> metadatas,
                             Integer fetchAmount,
                             TestType testType,
                             List<WhereClause> whereClauses,
                             OrderDirection orderDirection,
                             DateTimeFormatter dateTimeFormat) {
        super(tableName,
                schema,
                includedColumns,
                excludedColumns,
                metadatas,
                fetchAmount,
                testType,
                whereClauses,
                orderDirection,
                dateTimeFormat);
    }

    @Override
    public String createDataQuery() throws QueryCreationError {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(generateSelectStatement());
        query.append(" FROM ");
        query.append(getFrom());
        if (whereClauses != null) {
            String whereClause = whereClauses.stream()
                    .map(wc -> wc.constructClause(this))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" AND "));
            if (!whereClause.isEmpty()) {
                query.append(" WHERE ").append(whereClause);
            }
        }
        if (orderDirection != null && fetchAmount > 0) {
            query.append(" ORDER BY ").append(getOrderBy());
        }
        if (fetchAmount > 0) {
            query.append(" LIMIT ").append(fetchAmount);
        }
        return query.toString();
    }

    @Override
    public String createExistenceQuery() throws QueryCreationError {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(metadatas.stream()
                .filter(Metadata::isPk)
                .map(Metadata::getColumn)
                .collect(Collectors.joining(",")));
        if (!testType.equals(TestType.FULL)) {
            String whereClauseColumns = whereClauses.stream()
                    .map(WhereClause::getColumn)
                    .filter(Objects::nonNull)
                    .filter(s -> !metadatas.stream()
                            .filter(md -> md.getColumn().equals(s))
                            .map(Metadata::isPk)
                            .findAny()
                            .orElse(true))
                    .collect(Collectors.joining(","));
            if (!whereClauseColumns.isEmpty()) {
                query.append(",").append(whereClauseColumns);
            }
        }
        query.append(" FROM ");
        query.append(getFrom());
        if (!testType.equals(TestType.FULL)) {
            String whereClause = whereClauses.stream()
                    .map(wc -> wc.constructClause(this))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" AND "));
            if (!whereClause.isEmpty()) {
                query.append(" WHERE ").append(whereClause);
            }
        }
        if (orderDirection != null) {
            query.append(" ORDER BY ").append(getOrderBy());
        }
        if (fetchAmount > 0) {
            query.append(" LIMIT ").append(fetchAmount);
        }
        return query.toString();
    }

    @Override
    public String createCountQuery() throws QueryCreationError {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append("count(*)");
        query.append(" FROM ");
        query.append(getFrom());
        if (!testType.equals(TestType.FULL)) {
            String whereClause = whereClauses.stream()
                    .map(wc -> wc.constructClause(this))
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" AND "));
            if (!whereClause.isEmpty()) {
                query.append(" WHERE ").append(whereClause);
            }
        }
        return query.toString();
    }

    @Override
    public String createDataQuery(Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        addSampledWhereClauses(pksWithHeaders);
        return createDataQuery();
    }

    @Override
    public String createExistenceQuery(Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        addSampledWhereClauses(pksWithHeaders);
        return createExistenceQuery();
    }

    @Override
    public String getDateRange(String dateColumn, DateTime startDate, DateTime endDate) {
        if (testType.equals(TestType.FULL)) {
            return null; // is handled when filtering whereclauses by nulls
        }
        if (endDate == null) {
            String startStr = dateTimeFormat.print(startDate);
            return String.format("%s >= \"%s\""
                    , dateColumn
                    , startStr);
        }
        if (startDate == null) {
            String endStr = dateTimeFormat.print(endDate);
            return String.format("%s <= \"%s\""
                    , dateColumn
                    , endStr);
        }
        String startStr = dateTimeFormat.print(startDate);
        String endStr = dateTimeFormat.print(endDate);
        return String.format("%s between \"%s\" and \"%s\""
                , dateColumn
                , startStr
                , endStr);
    }

    private String getOrderBy() {
        String ordering = "ASC";
        switch (orderDirection) {
            case ASCENDING:
                ordering = "ASC";
                break;
            case DESCENDING:
                ordering = "DESC";
                break;
            case SQL_RANDOM:
                return "RAND()";
            case RESERVOIR_RANDOM:
                return "null";
        }
        final String finalOrdering = ordering;
        return metadatas.stream()
                .filter(Metadata::isPk)
                .map(m -> m.getColumn() + " " + finalOrdering)
                .collect(Collectors.joining(", "));
    }

    protected String getFrom() {
        if (schema == null || schema.isEmpty()) {
            return tableName;
        } else {
            return schema + "." + tableName;
        }
    }

    private String generateSelectStatement() throws QueryCreationError {
        String collect;
        if (includedColumns.isEmpty() && excludedColumns.isEmpty()) {
            collect = metadatas.stream()
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(","));
        } else if (!includedColumns.isEmpty()) {
            collect = metadatas.stream()
                    .filter(md -> {
                        String column = md.getColumn();
                        return (includedColumns.contains(column) && !excludedColumns.contains(column)) || md.isPk();
                    })
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(","));
        } else {
            collect = metadatas.stream()
                    .filter(md -> !excludedColumns.contains(md.getColumn()) || md.isPk())
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(","));
        }
        if (collect.isEmpty()) {
            logger.error("Select Statement was empty");
            throw new QueryCreationError("Select Statement was empty");
        }
        return collect;
    }

}
