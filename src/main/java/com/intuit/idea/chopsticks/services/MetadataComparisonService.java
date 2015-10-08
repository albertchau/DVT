package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlySourceField;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlyTargetField;
import static com.intuit.idea.chopsticks.services.ComparisonUtils.findLeftNotInRight;
import static com.intuit.idea.chopsticks.utils.CollectionUtils.isNullOrEmpty;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;
import static com.intuit.idea.chopsticks.utils.containers.Metadata.createWithNoAliasing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataComparisonService extends ComparisonServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparisonService.class);

    public MetadataComparisonService(Set<ResultStore> resultStores) {
        super(resultStores);
    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        initializeConnections(source, target);
        try {
            List<Metadata> srcMetadata = source.getMetadata();
            List<Metadata> tarMetadata = target.getMetadata();
            if (isNullOrEmpty(srcMetadata)) {
                logger.error("Could not find source metadata.;");
                throw new ComparisonException("Could not find source metadata.");
            }
            if (isNullOrEmpty(tarMetadata)) {
                logger.error("Could not find source metadata.;");
                throw new ComparisonException("Could not find source metadata.");
            }
            metadataCompare(srcMetadata, tarMetadata);
        } catch (DataProviderException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        closeConnections(source, target);
    }

    private void metadataCompare(List<Metadata> srcMetadata, List<Metadata> tarMetadata) throws ComparisonException {
        comparePrimaryKeys(srcMetadata, tarMetadata);
        List<Metadata> tarMetadataOnly = findLeftNotInRight(tarMetadata, srcMetadata, Metadata::equals);
        List<Metadata> srcMetadataOnly = findLeftNotInRight(srcMetadata, tarMetadata, Metadata::equals);
        if (!tarMetadataOnly.isEmpty()) {
            String tColOnlyString = tarMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":"/* + col.getSqlTypeName()*/)
                    .collect(joining(", "));
            logger.error("Target contains columns [ " + tColOnlyString + "] which source does not.");
            List<ColumnComparisonResult> columnResults = tarMetadataOnly.stream()
                    .map(col -> createOnlyTargetField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
        if (!srcMetadataOnly.isEmpty()) {
            String sColOnlyString = srcMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":"/* + col.getSqlTypeName()*/)
                    .collect(joining(", "));
            logger.error("Source contains columns [ " + sColOnlyString + "] which target does not.");
            List<ColumnComparisonResult> columnResults = srcMetadataOnly.stream()
                    .map(col -> createOnlySourceField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
    }

    /*
    todo throw and catch errors for something so similar where only the error messages differ
     */
    public void metadataCompare(ResultSet sData, ResultSet tData, List<String> sPks, List<String> tPks) throws ComparisonException {
        // check all source primary keys live in target primary keys
//        comparePrimaryKeys(sPks, tPks);
        // make metadata with fields
        List<Metadata> srcMetadata = metadataFromResults(sData, sPks);
        List<Metadata> tarMetadata = metadataFromResults(tData, tPks);
        List<Metadata> tarMetadataOnly = findLeftNotInRight(tarMetadata, srcMetadata, Metadata::equals);
        List<Metadata> srcMetadataOnly = findLeftNotInRight(srcMetadata, tarMetadata, Metadata::equals);
        if (!tarMetadataOnly.isEmpty()) {
            String tColOnlyString = tarMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":"/* + col.getSqlTypeName()*/)
                    .collect(joining(", "));
            logger.error("Target contains columns [ " + tColOnlyString + "] which source does not.");
            List<ColumnComparisonResult> columnResults = tarMetadataOnly.stream()
                    .map(col -> createOnlyTargetField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
        if (!srcMetadataOnly.isEmpty()) {
            String sColOnlyString = srcMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":"/* + col.getSqlTypeName()*/)
                    .collect(joining(", "));
            logger.error("Source contains columns [ " + sColOnlyString + "] which target does not.");
            List<ColumnComparisonResult> columnResults = srcMetadataOnly.stream()
                    .map(col -> createOnlySourceField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
    }

    private List<Metadata> metadataFromResults(ResultSet data, List<String> pks) {
        return convert(data)
                .map(s -> createWithNoAliasing(s.asString("COLUMN_NAME"),
                        pks.stream().anyMatch(pk -> pk.equalsIgnoreCase(s.asString("COLUMN_NAME"))),
                        toClass(s.asInt("DATA_TYPE"))))
                .sorted()
                .collect(toList());
    }

    public void comparePrimaryKeys(List<Metadata> srcMetadata, List<Metadata> tarMetadata) {
        List<Metadata> srcPrimaryKeyMetadata = srcMetadata.stream()
                .filter(Metadata::isPk)
                .collect(toList());
        List<Metadata> tarPrimaryKeyMetadata = tarMetadata.stream()
                .filter(Metadata::isPk)
                .collect(toList());
        List<Metadata> tarPksOnly = findLeftNotInRight(tarPrimaryKeyMetadata, srcPrimaryKeyMetadata, Metadata::equals);
        List<Metadata> srcPksOnly = findLeftNotInRight(srcPrimaryKeyMetadata, tarPrimaryKeyMetadata, Metadata::equals);
        if (!isNullOrEmpty(srcPksOnly)) {
            String srcPksOnlyStr = srcPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .collect(joining(", "));
            logger.error("Source contains primary keys [" + (srcPksOnlyStr) + "] which target does not");
            List<ColumnComparisonResult> columnResults = srcPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .map(pk -> createOnlySourceField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
        if (!isNullOrEmpty(tarPksOnly)) {
            String tarPksOnlyStr = tarPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .collect(joining(", "));
            logger.error("Target contains primary keys [" + (tarPksOnlyStr) + "] which source does not");
            List<ColumnComparisonResult> columnResults = tarPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .map(pk -> createOnlyTargetField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(this, columnResults));
        }
    }
}
