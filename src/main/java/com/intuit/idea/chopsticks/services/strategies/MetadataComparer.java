package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlySourceField;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlyTargetField;
import static com.intuit.idea.chopsticks.utils.CollectionUtils.isNullOrEmpty;
import static com.intuit.idea.chopsticks.utils.ComparisonUtils.findLeftNotInRight;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataComparer implements Comparer {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparer.class);
    private Set<ResultStore> resultStores;

    @Override
    public void compare(Extracted extracted, Set<ResultStore> resultStores) {
        this.resultStores = resultStores;
        List<Comparable[]> sRowList = extracted.srcList;
        List<Comparable[]> tRowList = extracted.tarList;
        List<Metadata> srcMetadata = listToUnaryMetadata(sRowList);
        List<Metadata> tarMetadata = listToUnaryMetadata(tRowList);
        comparePrimaryKeys(srcMetadata, tarMetadata);
        List<Metadata> tarMetadataOnly = findLeftNotInRight(tarMetadata, srcMetadata, Metadata::equals);
        List<Metadata> srcMetadataOnly = findLeftNotInRight(srcMetadata, tarMetadata, Metadata::equals);
        if (!tarMetadataOnly.isEmpty()) {
            String tColOnlyString = tarMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":" + col.getType().toString())
                    .collect(joining(", "));
            logger.error("Target contains columns [ " + tColOnlyString + "] which source does not.");
            List<ColumnComparisonResult> columnResults = tarMetadataOnly.stream()
                    .map(col -> createOnlyTargetField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
        if (!srcMetadataOnly.isEmpty()) {
            String sColOnlyString = srcMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":" + col.getType().toString())
                    .collect(joining(", "));
            logger.error("Source contains columns [ " + sColOnlyString + "] which target does not.");
            List<ColumnComparisonResult> columnResults = srcMetadataOnly.stream()
                    .map(col -> createOnlySourceField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
    }

    private List<Metadata> listToUnaryMetadata(List<Comparable[]> metadataAsList) {
        return metadataAsList.stream()
                .map(md -> (Metadata) md[0])
                .collect(toList());
    }

    private void comparePrimaryKeys(List<Metadata> srcMetadata, List<Metadata> tarMetadata) {
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
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
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
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
    }
}
