package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.services.ComparisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class AdhocJdbcDataProviderTest {
    private static final Logger logger = LoggerFactory.getLogger(AdhocJdbcDataProviderTest.class);
    AdhocJdbcDataProvider provider;
    private ComparisonService comparisonService;

    @BeforeMethod
    public void setUp() throws Exception {
        provider = new AdhocJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "",
                "",
                "employees",
                null,
                "Select * from test.employees"
        );
        provider.openConnections();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        provider.close();
        provider = null;
    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void testGetExistenceData() throws Exception {
//        comparisonService = new ExistenceComparisonService(null,null,null,null);
//        ResultSet data = provider.getData(ComparisonType.EXISTENCE);
//        Metadata[] metadatas = extractAllMetadata(data);
//        CombinedMetadata[] combinedMetadatas = Arrays.stream(metadatas).map(m -> new CombinedMetadata(m, m, Comparable::compareTo)).toArray(CombinedMetadata[]::new);
//        List<Comparable[]> comparables = resultSetToSortedList(data, combinedMetadatas, CombinedMetadata::getSrc);
//        Arrays.stream(metadatas)
//                .map(Metadata::toString)
//                .forEach(logger::info);
//        comparables.stream()
//                .map(Arrays::stream)
//                .map(s -> s.map(Object::toString))
//                .map(s -> s.collect(Collectors.joining(",")))
//                .forEach(logger::info);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void testGetCountData() throws Exception {
//        comparisonService = new CountComparisonService(null, null,null,null);
//        ResultSet data = provider.getData(ComparisonType.COUNT);
//        Metadata[] metadatas = extractAllMetadata(data);
//        CombinedMetadata[] combinedMetadatas = Arrays.stream(metadatas).map(m -> new CombinedMetadata(m, m, Comparable::compareTo)).toArray(CombinedMetadata[]::new);
//        List<Comparable[]> comparables = resultSetToSortedList(data, combinedMetadatas, CombinedMetadata::getSrc);
//        Arrays.stream(metadatas)
//                .map(Metadata::toString)
//                .forEach(logger::info);
//        comparables.stream()
//                .map(Arrays::stream)
//                .map(s -> s.map(Object::toString))
//                .map(s -> s.collect(Collectors.joining(",")))
//                .forEach(logger::info);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void testGetData() throws Exception {
//        comparisonService = new ExistenceComparisonService(null,null,null,null);
//        ResultSet data = provider.getData(ComparisonType.DATA);
//        Metadata[] metadatas = extractAllMetadata(data);
//        CombinedMetadata[] combinedMetadatas = Arrays.stream(metadatas).map(m -> new CombinedMetadata(m, m, Comparable::compareTo)).toArray(CombinedMetadata[]::new);
//        List<Comparable[]> comparables = resultSetToSortedList(data, combinedMetadatas, CombinedMetadata::getSrc);
//        Arrays.stream(metadatas)
//                .map(Metadata::toString)
//                .forEach(logger::info);
//        comparables.stream()
//                .map(Arrays::stream)
//                .map(s -> s.map(Object::toString))
//                .map(s -> s.collect(Collectors.joining(",")))
//                .forEach(logger::info);
//    }
}