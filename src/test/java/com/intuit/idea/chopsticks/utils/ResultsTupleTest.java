package com.intuit.idea.chopsticks.utils;

/**
 * Default Tuple test case
 */

import com.intuit.idea.chopsticks.utils.functional.ResultsTuple;
import com.intuit.idea.chopsticks.utils.functional.Tuple;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.intuit.idea.chopsticks.utils.functional.ResultsTuple.tCons;
import static com.intuit.idea.chopsticks.utils.functional.ResultsTuple.tuple;
import static org.testng.Assert.*;

public class ResultsTupleTest {

    @Test
    public void testConstructor() throws Exception {
        new ResultsTuple(tCons("one", "hello"), tCons("two", "world"));
        tuple(tCons("one", "hello"), tCons("two", "world"));
        //nothing to assert
    }

    @Test
    public void testValWithType() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "hello"), tCons("two", "world"));
        Optional<String> s = tuple.val("one", String.class);
        assertTrue(s.isPresent());
        assertEquals("hello", s.get());
    }

    @Test
    public void testValWithNoType() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "hello"), tCons("two", "world"));
        Optional<String> s = tuple.val("two");
        assertTrue(s.isPresent());
        assertEquals("world", s.get());
    }


    @Test
    public void testToString() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "hello"), tCons("two", "world"));
        System.out.println(tuple.toString());
    }

    @Test
    public void testAsInt() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1"));
        assertEquals(1, tuple.asInt("one"));
        assertEquals(1, tuple.asInt(0));
    }

    @Test
    public void testAsBigDecimal() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1"));
        assertEquals(new BigDecimal("1"), tuple.asBigDecimal("one"));
        assertEquals(new BigDecimal("1"), tuple.asBigDecimal(0));
    }

    @Test
    public void testAsFloat() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1.0"));
        assertEquals(1.0, tuple.asFloat("one"), 0.0);
        assertEquals(1.0, tuple.asFloat(0), 0.0);
    }

    @Test
    public void testAsDouble() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1.0"));
        assertEquals(1.0, tuple.asDouble("one"), 0.0);
        assertEquals(1.0, tuple.asDouble(0), 0.0);
    }

    @Test
    public void testAsChar() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "a"));
        assertEquals('a', tuple.asChar("one"));
        assertEquals('a', tuple.asChar(0));
    }

    @Test
    public void testAsShort() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1"));
        assertEquals(1, tuple.asShort("one"));
        assertEquals(1, tuple.asShort(0));
    }

    @Test
    public void testAsByte() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "1"));
        assertEquals(1, tuple.asByte("one"));
        assertEquals(1, tuple.asByte(0));
    }

    @Test
    public void testAsBoolean() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "yes"), tCons("two", 'y'), tCons("three", true));
        assertEquals(true, tuple.asBoolean("one"));
        assertEquals(true, tuple.asBoolean(0));
        assertEquals(true, tuple.asBoolean("two"));
        assertEquals(true, tuple.asBoolean(1));
        assertEquals(true, tuple.asBoolean("three"));
        assertEquals(true, tuple.asBoolean(2));
    }

    @Test
    public void testSubSet() throws Exception {
        Tuple tuple = new ResultsTuple(tCons("one", "yes"), tCons("two", 'y'), tCons("three", true), tCons("four", "test"));
        Tuple subTuple = tuple.subTuple("two", "three");
        assertTrue(subTuple.val("two").isPresent());
        assertTrue(subTuple.val("three").isPresent());
        assertFalse(subTuple.val("one").isPresent());
        assertFalse(subTuple.val("four").isPresent());
    }

    @Test
    public void testMerge() throws Exception {
        Tuple first = new ResultsTuple(tCons("one", "yes"), tCons("two", 'y'), tCons("three", true), tCons("four", "test"));
        Tuple second = new ResultsTuple(tCons("three", "yes"), tCons("four", 'y'), tCons("five", true), tCons("six", "test"));
        Tuple merged = first.merge(second);
        assertNotNull(merged);
        assertTrue(merged.hasVal("one"));
        assertTrue(merged.hasVal("two"));
        assertTrue(merged.hasVal("three"));
        assertTrue(merged.hasVal("four"));
        assertTrue(merged.hasVal("five"));
        assertTrue(merged.hasVal("six"));
    }

    @Test
    public void testReduce() throws Exception {
        Tuple first = new ResultsTuple(tCons("test.one", "yes"), tCons("test.two", 'y'), tCons("two.three", true), tCons("two.four", "test"));
        Tuple reduce = first.reduce("test.");
        assertTrue(reduce.hasVal("test.one"));
        assertTrue(reduce.hasVal("test.two"));
        assertFalse(reduce.hasVal("two.three"));
        assertFalse(reduce.hasVal("two.four"));
        System.out.println(reduce);
    }

}
