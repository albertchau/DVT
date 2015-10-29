package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.format.DateTimeFormatter;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * todo
 * sort existence metadata first
 * order by - has primary keys
 * data/existence... - combine?
 * dateformat when casting inside sql statements should be consistent with dateformatter pattern
 * ************************************
 * Author: achau1
 * Created On: 9/21/15
 * ************************************
 */
public abstract class QueryServiceBase implements QueryService {
    private static final Logger logger = LoggerFactory.getLogger(QueryServiceBase.class);
    protected final String tableName;
    protected final String schema;
    protected final List<String> includedColumns;
    protected final List<String> excludedColumns;
    protected final Integer fetchAmount;
    protected final TestType testType;
    protected final List<WhereClause> whereClauses;
    protected final OrderDirection orderDirection;
    protected final DateTimeFormatter dateTimeFormat;

    protected QueryServiceBase(String tableName,
                               String schema,
                               List<String> includedColumns,
                               List<String> excludedColumns,
                               Integer fetchAmount,
                               TestType testType,
                               List<WhereClause> whereClauses,
                               OrderDirection orderDirection,
                               DateTimeFormatter dateTimeFormat) {
        this.tableName = tableName;
        this.schema = schema;
        this.includedColumns = new ArrayList<>(includedColumns);
        this.excludedColumns = new ArrayList<>(excludedColumns);
        this.fetchAmount = fetchAmount;
        this.testType = testType;
        this.whereClauses = new ArrayList<>(whereClauses);
        this.orderDirection = orderDirection;
        this.dateTimeFormat = dateTimeFormat;
    }

    protected void addSampledWhereClauses(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        if (pksWithHeaders == null || pksWithHeaders.isEmpty()) {
            throw new QueryCreationError("Did not pass in any primary keys or values into sampling.");
        }
        List<Tuple2<Metadata, List<String>>> pkTuple2edList = metadatas.stream()
                .filter(Metadata::isPk)
                .map(md -> new Tuple2<>(md, pksWithHeaders.get(md.getColumnLabel())))
                .filter(pkTuple2 -> pkTuple2.v1 != null && pkTuple2.v2 != null)
                .collect(Collectors.toList());
        int numOfInputPks = pkTuple2edList.size();
        int numOfRows = pkTuple2edList.stream()
                .map(pkTuple2 -> pkTuple2.v2.size())
                .min(Integer::compareTo)
                .orElse(0);
        if (numOfRows < 1) {
            throw new QueryCreationError("At least one of the primary key's used to sample has zero passed in values.");
        }
        if (numOfInputPks != metadatas.stream().filter(Metadata::isPk).count()) {
            throw new QueryCreationError("The number of registered primary keys from this QueryService's creation does not match the number of primary keys to sample the data set from.");
        }
        if (numOfInputPks == 1) {
            Metadata pkMetadata = metadatas.stream().filter(Metadata::isPk).collect(Collectors.toList()).get(0);
            Class<?> type = pkMetadata.getType();
            if (type.equals(String.class)) {
                List<String> inBounds = pkTuple2edList.get(0).v2;
                whereClauses.add(WhereClause.createInSet(inBounds, pkMetadata.getColumnLabel()));
            } else if (type.equals(Long.class) || type.equals(Integer.class)) {
                List<Integer> inBounds = pkTuple2edList.get(0).v2.stream()
                        .map(stringInt -> (Integer.parseInt(stringInt)))
                        .collect(Collectors.toList());
                whereClauses.add(WhereClause.createInSet(inBounds, pkMetadata.getColumnLabel()));
            }
        } else {
            String outJoined = IntStream.range(0, numOfRows).boxed()
                    .map(i -> {
                        String inJoined = pkTuple2edList.stream()
                                .map(p -> {
                                    Class<?> type = p.v1.getType();
                                    if (type.equals(Long.class) || type.equals(Integer.class)) {
                                        return p.v1.getColumnLabel() + " = " + p.v2.get(i);
                                    } else {
                                        return p.v1.getColumnLabel() + " = '" + p.v2.get(i) + "'";
                                    }
                                })
                                .collect(Collectors.joining(" AND "));
                        return "(" + inJoined + ")";
                    })
                    .collect(Collectors.joining(" OR ")); //todo
            whereClauses.add(WhereClause.createCustom("(" + outJoined + ")"));
        }
    }

    @Override
    public String createDataQuery(List<Metadata> metadatas) throws QueryCreationError {
        Collections.sort(metadatas);
        StringBuilder query = new StringBuilder("SELECT ");
        query.append(generateSelectStatement(metadatas));
        query.append(" FROM ");
        query.append(getFrom());
        String whereClause = whereClauses.stream()
                .map(wc -> wc.constructClause(this))
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));
        if (!whereClause.isEmpty()) {
            query.append(" WHERE ").append(whereClause);
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
        /* These lines append any columns that are needed in the where clauses so that the query is valid. */
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
        if (fetchAmount > 0 && fetchAmount > 0) {
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

    protected String getOrderBy(List<Metadata> metadatas) {
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

    protected String generateSelectStatement(List<Metadata> metadatas) throws QueryCreationError {
        String collect;
        if (includedColumns.isEmpty() && excludedColumns.isEmpty()) {
            collect = metadatas.stream()
                    .map((metadata) -> metadata.getColumnName() + " AS " + metadata.getColumnLabel())
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
            throw new QueryCreationError("Select Statement was empty");
        }
        return collect;
    }
}
