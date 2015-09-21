package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.Pair;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intuit.idea.chopsticks.utils.StreamUtils.zip;
import static java.util.Collections.sort;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/21/15
 * ************************************
 */
public abstract class QueryServiceBase implements QueryService {

    protected final String tableName;
    protected final String schema;
    protected final List<String> includedColumns;
    protected final List<String> excludedColumns;
    protected final List<Metadata> metadatas;
    protected final Integer fetchAmount;
    protected final TestType testType;
    protected final List<WhereClause> whereClauses;
    protected final OrderDirection orderDirection;
    protected final DateTimeFormatter dateTimeFormat;

    protected QueryServiceBase(String tableName,
                               String schema,
                               List<String> includedColumns,
                               List<String> excludedColumns,
                               List<Metadata> metadatas,
                               Integer fetchAmount,
                               TestType testType,
                               List<WhereClause> whereClauses,
                               OrderDirection orderDirection,
                               DateTimeFormatter dateTimeFormat) {
        this.tableName = tableName;
        this.schema = schema;
        this.includedColumns = includedColumns;
        this.excludedColumns = excludedColumns;
        sort(metadatas); //todo override the equals
        this.metadatas = metadatas;
        this.fetchAmount = fetchAmount;
        this.testType = testType;
        this.whereClauses = whereClauses;
        this.orderDirection = orderDirection;
        this.dateTimeFormat = dateTimeFormat;
    }

    protected void addSampledWhereClauses(List<List<String>> pksToInclude, List<String> columns) {
        if (pksToInclude.isEmpty()) {
            return; //todo prob throw error
        }
        int numOfInputPks = pksToInclude.get(0).size();
        if (numOfInputPks < 1) {
            return; //todo prob throw error...
        }
        if (numOfInputPks != metadatas.stream().filter(Metadata::isPk).count()) {
            return; //todo prob throw error...
        }
        if (numOfInputPks == 1) {
            Metadata pkMetadata = metadatas.stream().filter(Metadata::isPk).collect(Collectors.toList()).get(0);
            Class<?> type = pkMetadata.getType();
            if (type.equals(String.class)) {
                List<String> inBounds = pksToInclude.stream()
                        .map(ls -> ls.get(0))
                        .collect(Collectors.toList());
                whereClauses.add(WhereClause.createInBounded(inBounds, pkMetadata.getColumn()));
            } else if (type.equals(Long.class) || type.equals(Integer.class)) {
                List<Integer> inBounds = pksToInclude.stream()
                        .map(ls -> (Integer.parseInt(ls.get(0))))
                        .collect(Collectors.toList());
                whereClauses.add(WhereClause.createInBounded(inBounds, pkMetadata.getColumn()));
            }
        } else {
            List<Metadata> pkStream = columns.stream()
                    .flatMap((c -> metadatas.stream()
                            .filter(md -> md.getColumn().equalsIgnoreCase(c) && md.isPk())
                            .collect(Collectors.toList())
                            .stream()))
                    .collect(Collectors.toList());
            String outJoined = pksToInclude.stream().map(ls -> {
                Stream<Pair<Metadata, String>> pkZipVal = zip(pkStream.stream(), ls.stream(), Pair::new);
                String inJoined = pkZipVal.map(p -> {
                    Class<?> type = p.getCar().getType();
                    if (type.equals(Long.class) || type.equals(Integer.class)) {
                        return p.getCar().getColumn() + " = " + p.getCdr();
                    } else {
                        return p.getCar().getColumn() + " = '" + p.getCdr() + "'";
                    }
                }).collect(Collectors.joining(" AND "));
                return "(" + inJoined + ")";
            }).collect(Collectors.joining(" OR "));
            whereClauses.add(WhereClause.createCustom("(" + outJoined + ")"));
        }
    }
}
