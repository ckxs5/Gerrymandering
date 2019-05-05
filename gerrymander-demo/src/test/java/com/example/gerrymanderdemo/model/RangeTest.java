package com.example.gerrymanderdemo.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {
    Range<Double> range;

    @Before
    public void setUp() throws Exception {
        range = new Range<>(20.0, 11.5);
    }

    @Test
    public void getLowerBound() {
        assertEquals(11.5, range.getLowerBound(), 0);
    }

    @Test
    public void getUpperBound() {
        assertEquals(20, range.getUpperBound(), 0);
    }

    @Test
    public void isIncluding() {
        assertTrue(range.isIncluding(15.67809));
        assertTrue(range.isIncluding(20.0));
        assertTrue(range.isIncluding(11.5));
        assertFalse(range.isIncluding(199.123124));
        assertFalse(range.isIncluding(11.499));

    }
}