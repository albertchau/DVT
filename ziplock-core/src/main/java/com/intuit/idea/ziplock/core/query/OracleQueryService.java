package com.intuit.idea.ziplock.core.query;

import com.intuit.idea.ziplock.core.utils.containers.Metadata;
import com.intuit.idea.ziplock.core.utils.exceptions.QueryCreationError;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public final class OracleQueryService extends QueryServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(OracleQueryService.class);

    public OracleQueryService(String tableName,
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
        query.append("*");
        query.append(" FROM ");
        query.append(getNestedDataFromClause(metadatas));
        if (fetchAmount > 0) {
            query.append(" WHERE ROWNUM <= ").append(fetchAmount);
        }
        return query.toString();
    }

    private String getNestedDataFromClause(List<Metadata> metadatas) {
        StringBuilder query = new StringBuilder("(SELECT ");
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
        query.append(")");
        return query.toString();
    }

    @Override
    public String createExistenceQuery(List<Metadata> metadatas) throws QueryCreationError {
        StringBuilder query = new StringBuilder("SELECT ");
        query.append("*");
        query.append(" FROM ");
        query.append(getNestedExistenceFromClause(metadatas));
        if (fetchAmount > 0) {
            query.append(" WHERE ROWNUM <= ").append(fetchAmount);
        }
        return query.toString();
    }

    private String getNestedExistenceFromClause(List<Metadata> metadatas) {
        StringBuilder query = new StringBuilder("(SELECT ");
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
        if (orderDirection != null && fetchAmount > 0) {
            query.append(" ORDER BY ").append(getOrderBy(metadatas));
        }
        query.append(")");
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
    public String getDateRange(String dateColumn, DateTime startDate, DateTime endDate) {
        if (testType.equals(TestType.FULL)) {
            return null; // is handled when filtering whereclauses by nulls
        }
        if (endDate == null) {
            String startStr = dateTimeFormat.print(startDate);
            return String.format("%s >= to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                    , dateColumn
                    , startStr);
        }
        if (startDate == null) {
            String endStr = dateTimeFormat.print(endDate);
            return String.format("%s <= to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                    , dateColumn
                    , endStr);
        }
        String startStr = dateTimeFormat.print(startDate);
        String endStr = dateTimeFormat.print(endDate);
        return String.format("%s between to_date('%s','MM-dd-yyyy hh24:mi:ss') and to_date('%s','MM-dd-yyyy hh24:mi:ss')"
                , dateColumn
                , startStr
                , endStr);
    }

}
