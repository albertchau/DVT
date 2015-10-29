package com.intuit.idea.ziplock.core.services.strategies.comparers;

import com.intuit.idea.ziplock.core.results.ColumnComparisonResult;
import com.intuit.idea.ziplock.core.results.ResultStore;
import com.intuit.idea.ziplock.core.utils.containers.Extracted;
import com.intuit.idea.ziplock.core.utils.containers.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.intuit.idea.ziplock.core.results.ColumnComparisonResult.createOnlySourceField;
import static com.intuit.idea.ziplock.core.results.ColumnComparisonResult.createOnlyTargetField;
import static com.intuit.idea.ziplock.core.utils.CollectionUtils.isNullOrEmpty;
import static com.intuit.idea.ziplock.core.utils.ComparisonUtils.findLeftNotInRight;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 * // TODO: 10/27/15 need to add comparison for "LIKE" mappings between metadata.
 *
 * @author albert
 */
public class MetadataComparer implements Comparer {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparer.class);
    private Set<ResultStore> resultStores;
    private boolean isPassed = true;

    @Override
    public void compare(Extracted extracted, Set<ResultStore> resultStores) {
        logger.debug("Comparing metadata sets...");
        this.resultStores = resultStores;
        List<Comparable[]> sRowList = extracted.srcList;
        List<Comparable[]> tRowList = extracted.tarList;
        List<Metadata> srcMetadata = listToUnaryMetadata(sRowList);
        List<Metadata> tarMetadata = listToUnaryMetadata(tRowList);
        comparePrimaryKeys(srcMetadata, tarMetadata);
        List<Metadata> tarMetadataOnly = findLeftNotInRight(tarMetadata, srcMetadata, Metadata::equals);
        List<Metadata> srcMetadataOnly = findLeftNotInRight(srcMetadata, tarMetadata, Metadata::equals);
        if (!tarMetadataOnly.isEmpty()) {
            isPassed = false;
            String tColOnlyString = tarMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":" + col.getType().toString())
                    .collect(joining(", "));
            logger.warn("Target contains columns [ " + tColOnlyString + "] which source does not.");
            List<ColumnComparisonResult> columnResults = tarMetadataOnly.stream()
                    .map(col -> createOnlyTargetField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
        if (!srcMetadataOnly.isEmpty()) {
            isPassed = false;
            String sColOnlyString = srcMetadataOnly.stream()
                    .map(col -> col.getColumnLabel() + ":" + col.getType().toString())
                    .collect(joining(", "));
            logger.warn("Source contains columns [ " + sColOnlyString + "] which target does not.");
            List<ColumnComparisonResult> columnResults = srcMetadataOnly.stream()
                    .map(col -> createOnlySourceField(col.getColumnLabel(), col.isPk()))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
        logger.debug((isPassed ? "[PASSED]" : "[FAILED]") + " finished comparing metadata.");
    }

    @Override
    public Boolean getResult() {
        return isPassed;
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
            isPassed = false;
            String srcPksOnlyStr = srcPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .collect(joining(", "));
            logger.warn("Source contains primary keys [" + (srcPksOnlyStr) + "] which target does not");
            List<ColumnComparisonResult> columnResults = srcPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .map(pk -> createOnlySourceField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
        if (!isNullOrEmpty(tarPksOnly)) {
            isPassed = false;
            String tarPksOnlyStr = tarPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .collect(joining(", "));
            logger.warn("Target contains primary keys [" + (tarPksOnlyStr) + "] which source does not");
            List<ColumnComparisonResult> columnResults = tarPksOnly.stream()
                    .map(Metadata::getColumnLabel)
                    .map(pk -> createOnlyTargetField(pk, true))
                    .collect(toList());
            resultStores.forEach(rs -> rs.storeRowResults(columnResults));
        }
    }
}
