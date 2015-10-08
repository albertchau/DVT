package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.format.DateTimeFormatter;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
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
    //    protected final List<Metadata> metadatas;
    protected final Integer fetchAmount;
    protected final TestType testType;
    protected final List<WhereClause> whereClauses;
    protected final OrderDirection orderDirection;
    protected final DateTimeFormatter dateTimeFormat;

    protected QueryServiceBase(String tableName,
                               String schema,
                               List<String> includedColumns,
                               List<String> excludedColumns,
//                               List<Metadata> metadatas,
                               Integer fetchAmount,
                               TestType testType,
                               List<WhereClause> whereClauses,
                               OrderDirection orderDirection,
                               DateTimeFormatter dateTimeFormat) {
        this.tableName = tableName;
        this.schema = schema;
        this.includedColumns = new ArrayList<>(includedColumns);
        this.excludedColumns = new ArrayList<>(excludedColumns);
//        this.metadatas = metadatas == null ? new ArrayList<>() : metadatas;
//        sort(this.metadatas);
        this.fetchAmount = fetchAmount;
        this.testType = testType;
        this.whereClauses = new ArrayList<>(whereClauses);
        this.orderDirection = orderDirection;
        this.dateTimeFormat = dateTimeFormat;
    }

    protected void addSampledWhereClauses(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError {
        if (pksWithHeaders == null || pksWithHeaders.isEmpty()) {
            logger.error("Did not pass in any primary keys or values into sampling.");
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
            logger.error("At least one of the primary key's used to sample has zero passed in values.");
            throw new QueryCreationError("At least one of the primary key's used to sample has zero passed in values.");
        }
        if (numOfInputPks != metadatas.stream().filter(Metadata::isPk).count()) {
            logger.error("The number of registered primary keys from this QueryService's creation does not match the number of primary keys to sample the data set from.");
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
}
