package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.query.Metadata;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlySourceField;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlyTargetField;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataComparisonService implements ComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparisonService.class);
    private Set<ResultStore> resultStores;

    public MetadataComparisonService(Set<ResultStore> resultStores) {
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
    }

    /*
    todo move to a util class
     */
    public static <T> List<T> findLeftNotInRight(List<T> left, List<T> right, BiPredicate<T, T> equalTo) {
        return left.stream()
                .filter(l -> right.stream()
                        .noneMatch(r -> equalTo.test(r, l)))
                .collect(toList());
    }

    @Override
    public void init() {

    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        try {
            source.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not dataCompare connections to source.");
        }
        try {
            target.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not dataCompare connections to target.");
        }
        try (ResultSet sData = source.getMetadata();
             ResultSet tData = target.getMetadata()) { //todo deal with random sampling
            List<String> sPks = source.getPrimaryKeys();
            List<String> tPks = target.getPrimaryKeys();
            metadataCompare(sData, tData, sPks, tPks);
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        source.closeConnections();
        target.closeConnections();
    }

    /*
    todo throw and catch errors for something so similar where only the error messages differ
     */
    public void metadataCompare(ResultSet sData, ResultSet tData, List<String> sPks, List<String> tPks) throws ComparisonException {
        // check all source primary keys live in target primary keys
        checkPrimaryKeys(sPks, tPks);
        // make metadata with fields
        List<Metadata> srcMetadata = metadataFromResults(sData, sPks);
        List<Metadata> tarMetadata = metadataFromResults(tData, tPks);
        List<Metadata> tarMetadataOnly = findLeftNotInRight(tarMetadata, srcMetadata, Metadata::equals);
        List<Metadata> srcMetadataOnly = findLeftNotInRight(srcMetadata, tarMetadata, Metadata::equals);
        if (!tarMetadataOnly.isEmpty()) {
            String tColOnlyString = tarMetadataOnly.stream()
                    .map(col -> col.getColumn() + ":" + col.getSqlTypeName())
                    .collect(joining(", "));
            logger.error("Target contains columns [ " + tColOnlyString + "] which source does not.");
            List<ColumnComparisonResult> columnResults = tarMetadataOnly.stream()
                    .map(col -> createOnlyTargetField(col.getColumn(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
        if (!srcMetadataOnly.isEmpty()) {
            String sColOnlyString = srcMetadataOnly.stream()
                    .map(col -> col.getColumn() + ":" + col.getSqlTypeName())
                    .collect(joining(", "));
            logger.error("Source contains columns [ " + sColOnlyString + "] which target does not.");
            List<ColumnComparisonResult> columnResults = srcMetadataOnly.stream()
                    .map(col -> createOnlySourceField(col.getColumn(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
    }

    private List<Metadata> metadataFromResults(ResultSet data, List<String> pks) {
        return convert(data)
                .map(s -> new Metadata(s.asString("COLUMN_NAME"),
                        pks.stream().anyMatch(pk -> pk.equalsIgnoreCase(s.asString("COLUMN_NAME"))),
                        s.asString("TYPE_NAME"),
                        toClass(s.asInt("DATA_TYPE"))))
                .sorted()
                .collect(toList());
    }

    public void checkPrimaryKeys(List<String> srcPks, List<String> tarPks) {
        List<String> tarPksOnly = findLeftNotInRight(tarPks, srcPks, String::equalsIgnoreCase);
        List<String> srcPksOnly = findLeftNotInRight(srcPks, tarPks, String::equalsIgnoreCase);
        if (!srcPksOnly.isEmpty()) {
            String srcPksOnlyStr = srcPksOnly.stream()
                    .collect(joining(", "));
            logger.error("Source contains primary keys [" + (srcPksOnlyStr) + "] which target does not");
            List<ColumnComparisonResult> columnResults = srcPksOnly.stream()
                    .map(pk -> createOnlySourceField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
        if (!tarPksOnly.isEmpty()) {
            String tarPksOnlyStr = tarPksOnly.stream()
                    .collect(joining(", "));
            logger.error("Target contains primary keys [" + (tarPksOnlyStr) + "] which source does not");
            List<ColumnComparisonResult> columnResults = tarPksOnly.stream()
                    .map(pk -> createOnlyTargetField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
    }

    @Override
    public void finish() {

    }
}
