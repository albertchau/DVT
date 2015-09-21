package com.intuit.idea.chopsticks.utils;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TransformerServiceTest {

    @Test
    public void testTransform() throws Exception {
        assertEquals("1", TransformerService.convert(1, String.class));
        assertEquals(1.0, TransformerService.convert(1, Double.class));
        assertEquals(new Float(1.0), TransformerService.convert(1, Float.class));
        assertEquals(new Short((short) 1), TransformerService.convert(1, Short.class));
        assertEquals(new Byte((byte) 1), TransformerService.convert(1, Byte.class));
        assertEquals("1", TransformerService.convert(1, String.class));
        assertEquals("1.0", TransformerService.convert(1.0, String.class));
        assertEquals("1.0", TransformerService.convert(1.0f, String.class));
        assertEquals("1", TransformerService.convert(1, String.class));
        assertEquals(true, TransformerService.convert(1, Boolean.class).booleanValue());
        assertEquals(false, TransformerService.convert(0, Boolean.class).booleanValue());
        assertEquals(true, TransformerService.convert('1', Boolean.class).booleanValue());
        assertEquals(false, TransformerService.convert('0', Boolean.class).booleanValue());
        assertEquals(true, TransformerService.convert('y', Boolean.class).booleanValue());
        assertEquals(false, TransformerService.convert('n', Boolean.class).booleanValue());
        assertEquals(true, TransformerService.convert("yes", Boolean.class).booleanValue());
        assertEquals(false, TransformerService.convert("no", Boolean.class).booleanValue());
    }
}
