package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.services.transforms.Loaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.utils.ComparisonUtils.findLeftNotInRight;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/1/15
 * ************************************
 */
@Test(groups = "comparison")
public class MetadataComparisonServiceTest extends ComparisonTestBase {
    private static final Logger logger = LoggerFactory.getLogger(MetadataComparisonServiceTest.class);

    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        comparisonService = ComparisonService.createForMetadata(null);
    }

    @Test
    public void testMetadataCompare() throws Exception {
        Loaded loaded = new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        comparisonService.startComparison(loaded);
    }

    @Test
    public void testFindLeftNotInRight() throws Exception {
        List<Integer> left = IntStream.range(1, 20).boxed().collect(Collectors.toList());
        List<Integer> right = IntStream.range(10, 30).boxed().collect(Collectors.toList());
        List<Integer> leftNotInRight = findLeftNotInRight(left, right, Integer::equals);
        List<Integer> rightNotInLeft = findLeftNotInRight(right, left, Integer::equals);
        logger.info("[" + leftNotInRight.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");
        logger.info("[" + rightNotInLeft.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");

    }
}