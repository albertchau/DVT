package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.query.Metadata;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.Pair;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createMatesFromMeta;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlySource;
import static com.intuit.idea.chopsticks.services.CombinedMetadata.combineMetadata;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;

/**
 * Copyright 2015
 * 1) get metadata map to comparable Java
 * 2) get data
 * 3) use metadata to cast each data column when converting to int
 * 4) find colcompareto
 * 5) create combined metadata from both metadatas and colcompareto
 * 6)
 *
 * @author albert
 */
public class ExistenceComparisonService implements ComparisonService {

    public static final List<String> METADATA_COL_HEADERS = Arrays.asList("COLUMN_NAME", "DATA_TYPE");
    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonService.class);
    private Set<ResultStore> resultStores;
    private BiFunction<Comparable, Comparable, Integer> comparer = Comparable::compareTo;

    public ExistenceComparisonService(Set<ResultStore> resultStores) {
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
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
        try (ResultSets sData = source.getData(this);
             ResultSets tData = target.getData(this)) { //todo deal with random sampling
            existenceCompare(sData, tData, source.getPrimaryKeys(), target.getPrimaryKeys());
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        source.closeConnections();
        target.closeConnections();
    }

    @Override
    public void finish() {

    }

    /**
     * Exposed for testing purposes. Used for when the result sets are not column ordered
     * and need some work done using defined metadata before comparing.
     * Will do a sort on significance using the primary key's lexigraphical ordering.
     *
     * @param sData Source result set that is to be compared with target.
     * @param tData Target result set that is to be compared with source.
     * @param sPks  Source primary key columns. Must exist in source result set columns. Order nor case matter.
     * @param tPks  Target primary key columns. Must exist in target result set columns. Order nor case matter.
     * @throws ComparisonException sdf
     */
    public void existenceCompare(ResultSet sData, ResultSet tData, List<String> sPks, List<String> tPks) throws ComparisonException {
        long start = System.nanoTime();
        Metadata[] sMetadata;
        Metadata[] tMetadata;
        try {
            sMetadata = metadataFromResultSet(sData, tPks);
            tMetadata = metadataFromResultSet(tData, sPks);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not connect to or get metadata from source or target databases.");
        }
        yo(sData, tData, sMetadata, tMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    /**
     * Exposed for testing purposes. Used for when the result sets are column ordered and ready to be compared as is.
     * Will do a sort from left to right most columns before comparing the result sets.
     * Summary - treats every column like a primary key column with most significant on left.
     *
     * @param sData Source result set that is to be compared with target.
     * @param tData Target result set that is to be compared with source.
     */
    public void existenceCompare(ResultSet sData, ResultSet tData) throws ComparisonException {
        long start = System.nanoTime();
        Metadata[] sMetadata;
        Metadata[] tMetadata;
        try {
            sMetadata = metadataFromResultSet(sData);
            tMetadata = metadataFromResultSet(tData);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not connect to or get metadata from source or target databases.");
        }
        yo(sData, tData, sMetadata, tMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private void yo(ResultSet sData, ResultSet tData, Metadata[] sMetadata, Metadata[] tMetadata) throws ComparisonException {
        CombinedMetadata[] bothMetadata = mergeMetadata(sMetadata, tMetadata);
        List<Comparable[]> sRowList;
        try {
            sRowList = rowsToLists(sData, sMetadata);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        List<Comparable[]> tRowList;
        try {
            tRowList = rowsToLists(tData, tMetadata);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        comparisonStrategy(sRowList, tRowList, bothMetadata);
    }

    private CombinedMetadata[] mergeMetadata(Metadata[] sMetadata, Metadata[] tMetadata) throws ComparisonException {
        if (sMetadata.length != tMetadata.length) {
            throw new ComparisonException("Metadata is not same length");
        }
        Arrays.sort(sMetadata);
        Arrays.sort(tMetadata);
        CombinedMetadata[] combinedMetadatas = IntStream.range(0, sMetadata.length)
                .boxed()
                .filter(i -> tMetadata[i].equals(sMetadata[i]))
                .map(i -> combineMetadata(sMetadata[i], comparer, tMetadata[i]))
                .toArray(CombinedMetadata[]::new);
        if (combinedMetadatas.length != sMetadata.length) {
            logger.error("Metadatas do not contain same content!");
            throw new ComparisonException("Metadatas do not contain same content!");
        }
        return combinedMetadatas;
    }

    private void comparisonStrategy(List<Comparable[]> sRowList, List<Comparable[]> tRowList, final CombinedMetadata[] metadatas) {
        Iterator<Comparable[]> sRowIter = sRowList.iterator();
        Iterator<Comparable[]> tRowIter = tRowList.iterator();
        Comparable[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        Comparable[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        BiFunction[] comparers = Arrays.stream(metadatas).map(CombinedMetadata::getComparer).toArray(BiFunction[]::new);
        while (sRow != null && tRow != null) {
            Integer compareInt = compareColumns(sRow, tRow, comparers, 0, metadatas.length);
            if (compareInt < 0) {
//                onlyFoundIn(sRow, metadatas); // todo handle both cases
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                logger.warn("Source row less than target. Incrementing source.");
            } else if (compareInt > 0) {
//                onlyFoundIn(sRow, metadatas); // todo handle both cases
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                logger.warn("Target row less than target. Incrementing target.");
            } else {
                final Object[] finalSRow = sRow;
                final Object[] finalTRow = tRow;
                List<ColumnComparisonResult> results = IntStream.range(0, metadatas.length).boxed()
                        .map(i -> createMatesFromMeta(true, metadatas[i], finalSRow[i], finalTRow[i]))
                        .collect(Collectors.toList());
                resultStores.stream().forEach(rs -> rs.storeRowResults(this, results));
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
            }
        }
        while (sRow != null) {
//                onlyFoundIn(sRow, metadatas); // todo handle both cases
            logger.info("There are rows only existing in source...");
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
//                onlyFoundIn(sRow, metadatas); // todo handle both cases
            logger.info("There are rows only existing in target...");
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }

    }
//
//    private void comparisonStrategy(List<Comparable[]> sRowList, List<Comparable[]> tRowList, final Metadata[] sMetadata, final Metadata[] tMetadata) {
//        sortObjectList(sRowList);
//        sortObjectList(tRowList);
//        Iterator<Comparable[]> sRowIter = sRowList.iterator();
//        Iterator<Comparable[]> tRowIter = tRowList.iterator();
//        Comparable[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
//        Comparable[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
//        while (sRow != null && tRow != null) {
//            try {
//                Integer compareInt = compareColumns(sRow, tRow, null /*metadatas*/);
//                if (compareInt < 0) {
////                    onlyFoundIn(sRow, sMetadata);
//                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
//                    logger.warn("Source row less than target. Incrementing source.");
//                } else if (compareInt > 0) {
//                    onlyFoundIn(tRow, tMetadata);
//                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
//                    logger.warn("Target row less than target. Incrementing target.");
//                } else {
//                    final Object[] finalSRow = sRow;
//                    final Object[] finalTRow = tRow;
//                    List<ColumnComparisonResult> tmp = IntStream.range(0, sMetadata.length).boxed()
//                            .map(i -> createMatesFromMeta(true, sMetadata[i], finalSRow[i], finalTRow[i]))
//                            .collect(Collectors.toList());
//                    resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
//                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
//                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
//                }
//            } catch (ComparisonException e) {
//                e.printStackTrace();
//                logger.warn("Could not compare row: " + e.getMessage());
//            }
//        }
//        while (sRow != null) {
////            onlyFoundIn(sRow, sMetadata);
//            logger.info("There are rows only existing in source...");
//            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
//        }
//        while (tRow != null) {
//            onlyFoundIn(tRow, tMetadata);
//            logger.info("There are rows only existing in target...");
//            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
//        }
//    }
//
//    private CombinedMetadata[] createMetadataForPrimaryKeys(List<String> sPks, Metadata[] sMd, List<String> tPks, Metadata[] tMd) throws ComparisonException {
//        boolean allSrcPkExistInTar = sPks.stream()
//                .allMatch(sPk -> tPks.stream()
//                        .anyMatch(tPk -> tPk.equalsIgnoreCase(sPk))
//                );
//        boolean allTarPkExistInSrc = sPks.stream()
//                .allMatch(sPk -> tPks.stream()
//                        .anyMatch(tPk -> tPk.equalsIgnoreCase(sPk))
//                );
//        if (!allSrcPkExistInTar || !allTarPkExistInSrc) {
//            logger.error("Primary keys don't match from the different result sets! Source Columns: "
//                    + (sPks) + "; Target Columns: "
//                    + (tPks) + ".");
//            throw new ComparisonException("Primary keys don't match from the different result sets! Source Columns: "
//                    + (sPks) + "; Target Columns: "
//                    + (tPks) + ".");
//        }
//        List<String> sortedPks = sPks.stream()
//                .sorted(String::compareToIgnoreCase)
//                .collect(Collectors.toList());
//        List<Comparable[]> srcMetadataAsArrays = new ArrayList<>();
//        List<Comparable[]> tarMetadataAsArrays = new ArrayList<>();
////        try {
//////            srcMetadataAsArrays = rowsToLists(sMd, METADATA_COL_HEADERS);
//////            tarMetadataAsArrays = rowsToLists(tMd, METADATA_COL_HEADERS);
////        } catch (SQLException e) {
////            e.printStackTrace();
////            logger.error("Could not get metadata for the primary key columns.");
////            throw new ComparisonException("Could not get metadata for the primary key columns.");
////        }
//        List<Metadata> srcMetadata = srcMetadataAsArrays.stream()
//                .map(md -> new Metadata((String) md[0],
//                        sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase((String) md[0])),
//                        toClass((Integer) md[1])))
//                .filter(Metadata::isPk)
//                .sorted()
//                .collect(Collectors.toList());
//        List<Pair<String, Class<?>>> tPairs = tarMetadataAsArrays.stream()
//                .filter(x -> x.length == 2)
//                .filter(x -> sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase((String) x[0])))
//                .map(x -> new Pair<String, Class<?>>(x[0].toString(), toClass(((Integer) x[1]))))
//                .collect(Collectors.toList());
//
////        List<Metadata> intersect = sPairs.stream()
////                .filter(sp -> tPairs.stream().anyMatch(tp -> tp.getCar().equalsIgnoreCase(sp.getCar())))
////                .map(sp -> {
////                    Pair<String, Class<?>> t = tPairs.stream().filter(tp -> tp.getCar().equals(sp.getCar())).findAny().get();
////                    return new Pair<Pair<String, Class<?>>, Pair<String, Class<?>>>(sp, t);
////                })
////                .map(st -> {
////                    if (st.getCar().getCdr().equals(st.getCdr().getCdr())) {
////                        return Metadata.createWithComparer(st.getCar().getCar(), true, Comparable::comparer);
////                    } else {
////                        return Metadata.createWithComparer(st.getCar().getCar(), true, findComparer(st.getCar().getCdr(), st.getCdr().getCdr()));
////                    }
////                })
////                .collect(Collectors.toList());
////        if (intersect.size() != sPairs.size() || intersect.size() != tPairs.size()) {
////            String sColsOnly = sPairs.stream()
////                    .filter(col -> intersect.stream()
////                            .noneMatch(in -> in.getColumn().equals(col.getCar())))
////                    .map(Pair::getCar)
////                    .collect(Collectors.joining(", "));
////            String tColsOnly = tPairs.stream()
////                    .filter(col -> intersect.stream()
////                            .noneMatch(in -> in.getColumn().equals(col.getCar())))
////                    .map(Pair::getCar)
////                    .collect(Collectors.joining(", "));
////            logger.error(String.format("Mismatched result set size after filtering by primary keys. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
////                    intersect.size(), sPairs.size(), sColsOnly, tPairs.size(), tColsOnly));
////            throw new ComparisonException(String.format("Mismatched result set size after filtering by primary keys. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
////                    intersect.size(), sPairs.size(), sColsOnly, tPairs.size(), tColsOnly));
////        }
////        return intersect.stream().toArray(Metadata[]::new);
//        return new CombinedMetadata[3];
//    }

    private BiFunction<Comparable, Comparable, Integer> findComparer(Class<?> sType, Class<?> tType) {
        Map<Pair<Class<?>, Class<?>>, BiFunction<Comparable, Comparable, Integer>> comp = new HashMap<>();
        comp.put(new Pair<>(String.class, Integer.class), (a, b) -> ((String) a).compareTo(b.toString()));
        BiFunction<Comparable, Comparable, Integer> r;
        if (comp.containsKey(new Pair<Class<?>, Class<?>>(sType, tType))) {
            r = comp.get(new Pair<Class<?>, Class<?>>(sType, tType));
        } else if (comp.containsKey(new Pair<Class<?>, Class<?>>(tType, sType))) {
            r = (t, v) -> comp.get(new Pair<Class<?>, Class<?>>(tType, sType)).apply(v, t);
        } else {
            r = (d, f) -> null;
        }
        return r;
    }

    public Metadata[] metadataFromResultSet(ResultSet data, List<String> pks) throws SQLException {
        ResultSetMetaData metaData = data.getMetaData();
        return IntStream.range(1, metaData.getColumnCount() + 1).boxed()
                .map(i -> {
                    try {
                        String columnLabel = metaData.getColumnLabel(i);
                        if (pks == null || pks.stream().anyMatch(pk -> pk.equalsIgnoreCase(columnLabel))) {
                            return new Metadata(columnLabel,
                                    true,
                                    toClass(metaData.getColumnType(i)));
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Metadata[]::new);
    }

    private Metadata[] metadataFromResultSet(ResultSet data) throws SQLException {
        return metadataFromResultSet(data, null);
    }

//
//    public List<Object[]> rowsToList(ResultSet resultSet, List<String> columnNames) throws SQLException {
//        List<Object[]> listOfRows = new ArrayList<>();
//        while (resultSet.next()) {
//            Object[] tmp = columnNames.stream()
//                    .map(valueOfColumn(resultSet))
//                    .toArray(Object[]::new);
//            listOfRows.add(tmp);
//        }
//
//        return listOfRows;
//    }
//
//    private Function<String, Object> valueOfColumn(ResultSet rs) {
//        return (columnName) -> {
//            try {
//                Object object = rs.getObject(columnName);
//                return object;
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return null;
//            }
//        };
//    }

    public List<Comparable[]> rowsToLists(ResultSet resultSet, Metadata[] columns) throws SQLException {
        List<Comparable[]> listOfRows = new ArrayList<>();
        while (resultSet.next()) {
            Comparable[] tmp = Arrays.stream(columns)
                    .map(columnValue(resultSet))
                    .toArray(Comparable[]::new);
            listOfRows.add(tmp);
        }
        return listOfRows.stream()
                .sorted(this::compareColumns)
                .collect(Collectors.toList());
    }

    private Integer compareColumns(Comparable[] source, Comparable[] target) {
        BiFunction[] comparers = new BiFunction[source.length];
        Arrays.fill(comparers, comparer);
        return compareColumns(source, target, comparers, 0, source.length);
    }

    private Function<Metadata, Comparable> columnValue(ResultSet rs) {
        return (columnName) -> {
            try {
                return columnName.getType().cast(rs.getObject(columnName.getColumn(), columnName.getType()));
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

//    private void sortObjectList(List<Comparable[]> listOfRows, CombinedMetadata[] metadatas) {
//        listOfRows.sort((s, t) -> compareColumns(s, t, metadatas));
//    }

    private Integer compareColumns(Comparable[] sRow, Comparable[] tRow, BiFunction<Comparable, Comparable, Integer>[] comparers, int startFrom, int until) {
        for (int i = startFrom; i < until; i++) {
            int compareVal = comparers[i].apply(sRow[i], tRow[i]); //todo make sure they are comparable at beginning. SLA type thing
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }
//
//    private void onlyFoundIn(final Object[] sRow, CombinedMetadata[] metadatas) {
//        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length).boxed()
//                .map(i -> createOnlySource(metadatas[i], sRow[i]))
//                .collect(Collectors.toList());
//        resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
//    }

}
