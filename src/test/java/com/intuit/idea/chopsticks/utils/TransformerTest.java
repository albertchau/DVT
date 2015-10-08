package com.intuit.idea.chopsticks.utils;

import com.intuit.idea.chopsticks.utils.functional.Transformations;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Test for Transformer class
 */
public class TransformerTest {

    @Test
    public void testConvertToBoolean() throws Exception {
        boolean result = Transformations.toBoolean(Optional.of((Object) true));
        assertTrue("boolean true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 'y'));
        assertTrue("char true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 'Y'));
        assertTrue("char true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) "yes"));
        assertTrue("string true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) "YES"));
        assertTrue("string true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 1));
        assertTrue("integer true failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 1L));
        assertTrue("long true failed", result);

        result = Transformations.toBoolean(Optional.of((Object) false));
        assertFalse("boolean false failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 'n'));
        assertFalse("char false failed", result);
        result = Transformations.toBoolean(Optional.of((Object) "no"));
        assertFalse("string false failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 0));
        assertFalse("int false failed", result);
        result = Transformations.toBoolean(Optional.of((Object) 0L));
        assertFalse("long false failed", result);


    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidType() throws Exception {
        Transformations.toBoolean(Optional.of((Object) new ArrayList<>()));
    }

}
