package com.intuit.idea.ziplock.core.query;

import com.intuit.idea.ziplock.core.providers.VendorType;

public class MySqlQueryServiceTest extends QueryServiceBaseTest {
    private static final VendorType VENDOR_TYPE = VendorType.MYSQL;
//
//    @DataProvider
//    public Object[][] happyCompositeQueryServices() {
//        List<Metadata> metadatas = COMPOSITE_PK_METADATA;
//        List<Object[]> collected = TEST_TYPES.stream()
//                .flatMap(testType -> {
//                    List<QueryService> qss = getQueryServices(testType, VENDOR_TYPE);
//                    return qss.stream()
//                            .filter(Objects::nonNull);
//                })
//                .map(qs -> new Object[]{qs})
//                .collect(Collectors.toList());
//        return collected
//                .toArray(new Object[][]{{}});
//    }
//
//    @DataProvider
//    public Object[][] happySingleIntQueryServices() {
//        List<TestType> testTypes = Arrays.asList(TestType.FULL, TestType.HISTORIC, TestType.INCREMENTAL);
//        List<Object[]> collected = testTypes.stream().flatMap(testType -> {
//            List<QueryService> qss = new ArrayList<>();
//            QueryServiceBuilder queryServiceBuilder = new QueryServiceBuilder();
//            QueryService singleIntPkQs = queryServiceBuilder
//                    .setSchema("test")
//                    .setFetchAmount(10)
//                    .setWhereClauses(Collections.singletonList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                    .setOrderDirection(OrderDirection.DESCENDING)
//                    .build("employees", VendorType.MYSQL, testType);
//            qss.add(singleIntPkQs);
//            return qss.stream().filter(Objects::nonNull);
//        }).map(qs -> new Object[]{qs})
//                .collect(Collectors.toList());
//        return collected.toArray(new Object[][]{{}});
//    }
//
//    @DataProvider
//    public Object[][] happySingleStrQueryServices() {
//        List<TestType> testTypes = Arrays.asList(TestType.FULL, TestType.HISTORIC, TestType.INCREMENTAL);
//        List<Object[]> collected = testTypes.stream().flatMap(testType -> {
//            List<QueryService> qss = new ArrayList<>();
//            QueryServiceBuilder queryServiceBuilder = new QueryServiceBuilder();
//            QueryService singleStrPkQs = queryServiceBuilder
//                    .setSchema("test")
//                    .setFetchAmount(10)
//                    .setWhereClauses(Collections.singletonList(WhereClause.createBounded(DateTime.now(), DateTime.now(), "createDate")))
//                    .setOrderDirection(OrderDirection.DESCENDING)
//                    .build("employees", VendorType.MYSQL, testType);
//            qss.add(singleStrPkQs);
//            return qss.stream().filter(Objects::nonNull);
//        }).map(qs -> new Object[]{qs})
//                .collect(Collectors.toList());
//        return collected.toArray(new Object[][]{{}});
//    }
//
//    @Test(dataProvider = "happyCompositeQueryServices")
//    public void testHappyCompositeCreateDataQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createDataQuery(null));
//        logger.info(mqs.createDataQueryWithInputSamples(null, SAMPLED_MAP_COMPOSITE));
//    }
//
//    @Test(dataProvider = "happyCompositeQueryServices")
//    public void testHappyCompositeCreateExistenceQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createExistenceQuery(null));
//        logger.info(mqs.createExistenceQueryWithInputSamples(null, SAMPLED_MAP_COMPOSITE));
//    }
//
//    @Test(dataProvider = "happyCompositeQueryServices")
//    public void testHappyCompositeCreateCountQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createCountQuery());
//    }
//
//    @Test(dataProvider = "happySingleIntQueryServices")
//    public void testHappySingleCreateDataQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createDataQuery(null));
//        logger.info(mqs.createDataQueryWithInputSamples(null, SAMPLED_MAP_SINGLE_INT));
//    }
//
//    @Test(dataProvider = "happySingleIntQueryServices")
//    public void testHappySingleCreateExistenceQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createExistenceQuery(null));
//        logger.info(mqs.createExistenceQueryWithInputSamples(null, SAMPLED_MAP_SINGLE_INT));
//    }
//
//    @Test(dataProvider = "happySingleIntQueryServices")
//    public void testHappySingleCreateCountQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createCountQuery());
//    }
//
//    @Test(dataProvider = "happySingleStrQueryServices")
//    public void testHappySingleStrCreateDataQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createDataQuery(null));
//        logger.info(mqs.createDataQueryWithInputSamples(null, SAMPLED_MAP_SINGLE_STR));
//    }
//
//    @Test(dataProvider = "happySingleStrQueryServices")
//    public void testHappySingleStrCreateExistenceQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createExistenceQuery(null));
//        logger.info(mqs.createExistenceQueryWithInputSamples(null, SAMPLED_MAP_SINGLE_STR));
//    }
//
//    @Test(dataProvider = "happySingleStrQueryServices")
//    public void testHappySingleStrCreateCountQuery(QueryService mqs) throws Exception {
//        logger.info(mqs.createCountQuery());
//    }
}