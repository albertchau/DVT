//package com.intuit.idea.chopsticks.query;
//
//import com.intuit.idea.chopsticks.utils.Metadata;
//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormatter;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * ************************************
// * Author: achau1
// * Created On: 9/20/15
// * ************************************
// */
//public class VerticaQueryService extends QueryServiceBase {
//
//    public VerticaQueryService(String tableName,
//                               String schema,
//                               List<String> includedColumns,
//                               List<String> excludedColumns,
//                               List<Metadata> metadatas,
//                               Integer fetchAmount,
//                               TestType testType,
//                               List<WhereClause> whereClauses,
//                               OrderDirection orderDirection,
//                               DateTimeFormatter dateTimeFormat) {
//        super(tableName,
//                schema,
//                includedColumns,
//                excludedColumns,
//                metadatas,
//                fetchAmount,
//                testType,
//                whereClauses,
//                orderDirection,
//                dateTimeFormat);
//    }
//
//    @Override
//    public String createDataQuery(List<Metadata> metadatas) {
//        StringBuilder query = new StringBuilder("SELECT ");
//        query.append(selectClause());
//        query.append(" FROM ");
//        query.append(getFrom());
//        if (whereClauses != null) {
//            String whereClause = whereClauses.stream()
//                    .map(wc -> wc.constructClause(this))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.joining(" AND "));
//            if (!whereClause.isEmpty()) {
//                query.append(" WHERE ").append(whereClause);
//            }
//        }
//        if (orderDirection != null) {
//            query.append(" ORDER BY ").append(getOrderBy());
//        }
//        if (fetchAmount > 0) {
//            query.append(" LIMIT ").append(fetchAmount);
//        }
//        return query.toString();
//    }
//
//    @Override
//    public String createExistenceQuery(List<Metadata> metadatas) {
//        StringBuilder query = new StringBuilder("SELECT ");
//        query.append(this.metadatas.stream()
//                .filter(Metadata::isPk)
//                .map(Metadata::getColumn)
//                .collect(Collectors.joining(",")));
//        if (!testType.equals(TestType.FULL)) {
//            String whereClauseColumns = whereClauses.stream()
//                    .map(WhereClause::getColumn)
//                    .filter(Objects::nonNull)
//                    .filter(s -> !this.metadatas.stream()
//                            .filter(md -> md.getColumn().equals(s))
//                            .map(Metadata::isPk)
//                            .findAny()
//                            .orElse(true))
//                    .collect(Collectors.joining(","));
//            if (!whereClauseColumns.isEmpty()) {
//                query.append(",").append(whereClauseColumns);
//            }
//        }
//        query.append(" FROM ");
//        query.append(getFrom());
//        if (!testType.equals(TestType.FULL)) {
//            String whereClause = whereClauses.stream()
//                    .map(wc -> wc.constructClause(this))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.joining(" AND "));
//            if (!whereClause.isEmpty()) {
//                query.append(" WHERE ").append(whereClause);
//            }
//        }
//        if (orderDirection != null) {
//            query.append(" ORDER BY ").append(getOrderBy());
//        }
//        if (fetchAmount > 0) {
//            query.append(" LIMIT ").append(fetchAmount);
//        }
//        return query.toString();
//    }
//
//    @Override
//    public String createCountQuery() {
//        StringBuilder query = new StringBuilder("SELECT ");
//        query.append("count(*)");
//        query.append(" FROM ");
//        query.append(getFrom());
//        if (!testType.equals(TestType.FULL)) {
//            String whereClause = whereClauses.stream()
//                    .map(wc -> wc.constructClause(this))
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.joining(" AND "));
//            if (!whereClause.isEmpty()) {
//                query.append(" WHERE ").append(whereClause);
//            }
//        }
//        return query.toString();
//    }
//
//    @Override
//    public String createDataQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) {
//        addSampledWhereClauses(pksWithHeaders);
//        return createDataQuery(this.metadatas);
//    }
//
//    @Override
//    public String createExistenceQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) {
//        addSampledWhereClauses(pksWithHeaders);
//        return createExistenceQuery(this.metadatas);
//    }
//
//    @Override
//    public String getDateRange(String dateColumn, DateTime startDate, DateTime endDate) {
//        if (testType.equals(TestType.FULL)) {
//            return null;
//        }
//        if (startDate == null && endDate == null) {
//            return null;
//        }
//        if (endDate == null) {
//            String startStr = dateTimeFormat.print(startDate);
//            return String.format("date_column(%s) >= timestamp(%s)"
//                    , dateColumn
//                    , startStr);
//        }
//        if (startDate == null) {
//            String endStr = dateTimeFormat.print(endDate);
//            return String.format("date_column(%s) <= timestamp(%s)"
//                    , dateColumn
//                    , endStr);
//        }
//        String startStr = dateTimeFormat.print(startDate);
//        String endStr = dateTimeFormat.print(endDate);
//        return String.format("date_column(%s) between timestamp(%s) and timestamp(%s)"
//                , dateColumn
//                , startStr
//                , endStr);
//    }
//
//    private String getOrderBy() {
//        String ordering = "ASC";
//        switch (orderDirection) {
//            case ASCENDING:
//                ordering = "ASC";
//                break;
//            case DESCENDING:
//                ordering = "DESC";
//                break;
//            case SQL_RANDOM:
//                return "RAND()";
//            case RESERVOIR_RANDOM:
//                return "null";
//        }
//        final String finalOrdering = ordering;
//        return metadatas.stream()
//                .filter(Metadata::isPk)
//                .map(m -> m.getColumn() + " " + finalOrdering)
//                .collect(Collectors.joining(", "));
//    }
//
//    protected String getFrom() {
//        if (schema == null || schema.isEmpty()) {
//            return tableName;
//        } else {
//            return schema + "." + tableName;
//        }
//    }
//
//    private String selectClause() {
//        if (includedColumns.isEmpty() && excludedColumns.isEmpty()) {
//            return metadatas.stream()
//                    .map(Metadata::getColumn)
//                    .collect(Collectors.joining(","));
//        }
//        if (!includedColumns.isEmpty()) {
//            return metadatas.stream()
//                    .filter(md -> {
//                        String column = md.getColumn();
//                        return (includedColumns.contains(column) && !excludedColumns.contains(column)) || md.isPk();
//                    })
//                    .map(Metadata::getColumn)
//                    .collect(Collectors.joining(","));
//        } else {
//            return metadatas.stream()
//                    .filter(md -> !excludedColumns.contains(md.getColumn()) || md.isPk())
//                    .map(Metadata::getColumn)
//                    .collect(Collectors.joining(","));
//
//        }
//    }
//
//}
