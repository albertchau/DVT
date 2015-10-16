package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
public final class HiveQueryService extends QueryServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(HiveQueryService.class);

    public HiveQueryService(String tableName,
                            String schema,
                            List<String> includedColumns,
                            List<String> excludedColumns,
                            Integer fetchAmount,
                            TestType testType,
                            List<WhereClause> whereClauses,
                            OrderDirection orderDirection,
                            DateTimeFormatter dateTimeFormat) {
        super(tableName,
                schema,
                includedColumns,
                excludedColumns,
                fetchAmount,
                testType,
                whereClauses,
                orderDirection,
                dateTimeFormat);
    }

    @Override
    public String createDataQuery(List<Metadata> metadatas) throws QueryCreationError {
        Collections.sort(metadatas);
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(generateSelectStatement(metadatas));
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
            query.append(" ORDER BY ").append(getOrderBy(metadatas));
        }
        if (fetchAmount > 0) {
            query.append(" LIMIT ").append(fetchAmount);
        }
        return query.toString();
    }

    @Override
    public String createExistenceQuery(List<Metadata> metadatas) throws QueryCreationError {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(metadatas.stream()
                .filter(Metadata::isPk)
                .map(Metadata::getColumnSelectStr)
                .collect(Collectors.joining(",")));
        if (!testType.equals(TestType.FULL)) {
            String whereClauseColumns = whereClauses.stream()
                    .map(WhereClause::getColumn)
                    .filter(Objects::nonNull)
                    .filter(wc -> metadatas.stream()
                            .anyMatch(md -> md.getColumnLabel().equalsIgnoreCase(wc) && md.isPk()))
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
            query.append(" ORDER BY ").append(getOrderBy(metadatas));
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
    public String createDataQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        addSampledWhereClauses(metadatas, pksWithHeaders);
        return createDataQuery(metadatas);
    }

    @Override
    public String createExistenceQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        addSampledWhereClauses(metadatas, pksWithHeaders);
        return createExistenceQuery(metadatas);
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

    private String getOrderBy(List<Metadata> metadatas) {
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
                .map(Metadata::getColumnLabel)
                .map(column -> column + " " + finalOrdering)
                .collect(Collectors.joining(", "));
    }

    protected String getFrom() {
        if (schema == null || schema.isEmpty()) {
            return tableName;
        } else {
            return schema + "." + tableName;
        }
    }

    private String generateSelectStatement(List<Metadata> metadatas) throws QueryCreationError {
        String collect;
        if (includedColumns.isEmpty() && excludedColumns.isEmpty()) {
            collect = metadatas.stream()
                    .map(Metadata::getColumnLabel)
                    .collect(Collectors.joining(","));
        } else if (!includedColumns.isEmpty()) {
            collect = metadatas.stream()
                    .filter(md -> {
                        String column = md.getColumnLabel();
                        return (includedColumns.contains(column) && !excludedColumns.contains(column)) || md.isPk();
                    })
                    .map(Metadata::getColumnLabel)
                    .collect(Collectors.joining(","));
        } else {
            collect = metadatas.stream()
                    .filter(md -> !excludedColumns.contains(md.getColumnLabel()) || md.isPk())
                    .map(Metadata::getColumnLabel)
                    .collect(Collectors.joining(","));
        }
        if (collect.isEmpty()) {
            logger.error("Select Statement was empty");
            throw new QueryCreationError("Select Statement was empty");
        }
        return collect;
    }

}
