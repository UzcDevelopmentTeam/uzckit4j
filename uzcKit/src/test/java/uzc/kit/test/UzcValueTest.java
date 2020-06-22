package uzc.kit.test;

import uzc.kit.entity.UzcValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class UzcValueTest {
    @Test
    public void testConstructors() {
        assertEquals("123456789", UzcValue.fromUzc("1.23456789").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromUzc("1.23456789 uzc").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromUzc("1.23456789 UZC").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromUzc(1.23456789).toPlanck().toString());
        assertEquals("123456789", UzcValue.fromUzc(new BigDecimal("1.23456789")).toPlanck().toString());
        assertEquals("123456789", UzcValue.fromPlanck("123456789").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromPlanck("123456789 planck").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromPlanck("123456789 PLANCK").toPlanck().toString());
        assertEquals("123456789", UzcValue.fromPlanck(123456789).toPlanck().toString());
        assertEquals("123456789", UzcValue.fromPlanck(new BigInteger("123456789")).toPlanck().toString());

        // Test null -> 0
        assertEquals(UzcValue.ZERO, UzcValue.fromPlanck((String) null));
        assertEquals(UzcValue.ZERO, UzcValue.fromPlanck((BigInteger) null));
        assertEquals(UzcValue.ZERO, UzcValue.fromUzc((String) null));
        assertEquals(UzcValue.ZERO, UzcValue.fromUzc((BigDecimal) null));
    }

    @Test
    public void testToString() {
        // Positive
        assertEquals("1 UZC", UzcValue.fromUzc(1).toString());
        assertEquals("1 UZC", UzcValue.fromUzc(1).toFormattedString());
        assertEquals("1", UzcValue.fromUzc(1).toUnformattedString());
        assertEquals("1 UZC", UzcValue.fromUzc(1.00000001).toString());
        assertEquals("1 UZC", UzcValue.fromUzc(1.00000001).toFormattedString());
        assertEquals("1.00000001", UzcValue.fromUzc(1.00000001).toUnformattedString());
        assertEquals("1.235 UZC", UzcValue.fromPlanck(123456789).toString());
        // Negative
        assertEquals("-1 UZC", UzcValue.fromUzc(-1).toString());
        assertEquals("-1 UZC", UzcValue.fromUzc(-1).toFormattedString());
        assertEquals("-1", UzcValue.fromUzc(-1).toUnformattedString());
        assertEquals("-1 UZC", UzcValue.fromUzc(-1.00000001).toString());
        assertEquals("-1 UZC", UzcValue.fromUzc(-1.00000001).toFormattedString());
        assertEquals("-1.00000001", UzcValue.fromUzc(-1.00000001).toUnformattedString());
        assertEquals("-1.235 UZC", UzcValue.fromPlanck(-123456789).toString());
    }

    @Test
    public void testToUzc() {
        assertEquals(BigDecimal.valueOf(100000000, 8), UzcValue.fromUzc(1).toUzc());
        assertEquals(BigDecimal.valueOf(-100000000, 8), UzcValue.fromUzc(-1).toUzc());
    }

    @Test
    public void testAdd() {
        assertEquals(UzcValue.fromUzc(1), UzcValue.fromUzc(0.5).add(UzcValue.fromUzc(0.5)));
        assertEquals(UzcValue.fromUzc(0), UzcValue.fromUzc(-0.5).add(UzcValue.fromUzc(0.5)));
        assertEquals(UzcValue.fromUzc(-1), UzcValue.fromUzc(-0.5).add(UzcValue.fromUzc(-0.5)));
    }

    @Test
    public void testSubtract() {
        assertEquals(UzcValue.fromUzc(1), UzcValue.fromUzc(1.5).subtract(UzcValue.fromUzc(0.5)));
        assertEquals(UzcValue.fromUzc(0), UzcValue.fromUzc(0.5).subtract(UzcValue.fromUzc(0.5)));
        assertEquals(UzcValue.fromUzc(-1), UzcValue.fromUzc(-0.5).subtract(UzcValue.fromUzc(0.5)));
    }

    @Test
    public void testMultiply() {
        // Positive + positive
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(2).multiply(5));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(4).multiply(2.5));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(2).multiply(BigInteger.valueOf(5)));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(4).multiply(BigDecimal.valueOf(2.5)));

        // Positive + negative
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(2).multiply(-5));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(4).multiply(-2.5));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(2).multiply(BigInteger.valueOf(-5)));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(4).multiply(BigDecimal.valueOf(-2.5)));

        // Negative + positive
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(-2).multiply(5));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(-4).multiply(2.5));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(-2).multiply(BigInteger.valueOf(5)));
        assertEquals(UzcValue.fromUzc(-10), UzcValue.fromUzc(-4).multiply(BigDecimal.valueOf(2.5)));

        // Negative + negative
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(-2).multiply(-5));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(-4).multiply(-2.5));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(-2).multiply(BigInteger.valueOf(-5)));
        assertEquals(UzcValue.fromUzc(10), UzcValue.fromUzc(-4).multiply(BigDecimal.valueOf(-2.5)));
    }
    
    @Test
    public void testDivide() {
        // Positive + positive
        assertEquals(UzcValue.fromUzc(0.4), UzcValue.fromUzc(2).divide(5));
        assertEquals(UzcValue.fromUzc(1.6), UzcValue.fromUzc(4).divide(2.5));
        assertEquals(UzcValue.fromUzc(0.4), UzcValue.fromUzc(2).divide(BigInteger.valueOf(5)));
        assertEquals(UzcValue.fromUzc(1.6), UzcValue.fromUzc(4).divide(BigDecimal.valueOf(2.5)));

        // Positive + negative
        assertEquals(UzcValue.fromUzc(-0.4), UzcValue.fromUzc(2).divide(-5));
        assertEquals(UzcValue.fromUzc(-1.6), UzcValue.fromUzc(4).divide(-2.5));
        assertEquals(UzcValue.fromUzc(-0.4), UzcValue.fromUzc(2).divide(BigInteger.valueOf(-5)));
        assertEquals(UzcValue.fromUzc(-1.6), UzcValue.fromUzc(4).divide(BigDecimal.valueOf(-2.5)));

        // Negative + positive
        assertEquals(UzcValue.fromUzc(-0.4), UzcValue.fromUzc(-2).divide(5));
        assertEquals(UzcValue.fromUzc(-1.6), UzcValue.fromUzc(-4).divide(2.5));
        assertEquals(UzcValue.fromUzc(-0.4), UzcValue.fromUzc(-2).divide(BigInteger.valueOf(5)));
        assertEquals(UzcValue.fromUzc(-1.6), UzcValue.fromUzc(-4).divide(BigDecimal.valueOf(2.5)));

        // Negative + negative
        assertEquals(UzcValue.fromUzc(0.4), UzcValue.fromUzc(-2).divide(-5));
        assertEquals(UzcValue.fromUzc(1.6), UzcValue.fromUzc(-4).divide(-2.5));
        assertEquals(UzcValue.fromUzc(0.4), UzcValue.fromUzc(-2).divide(BigInteger.valueOf(-5)));
        assertEquals(UzcValue.fromUzc(1.6), UzcValue.fromUzc(-4).divide(BigDecimal.valueOf(-2.5)));

        // Recurring divisions
        assertEquals(UzcValue.fromPlanck(33333333), UzcValue.fromUzc(1).divide(3));
        assertEquals(UzcValue.fromPlanck(66666666), UzcValue.fromUzc(2).divide(3));

        // Divisor < 1
        assertEquals(UzcValue.fromUzc(3), UzcValue.fromUzc(1).divide(1.0/3.0));
    }

    @Test
    public void testAbs() {
        assertEquals(UzcValue.fromUzc(1), UzcValue.fromUzc(1).abs());
        assertEquals(UzcValue.fromUzc(1), UzcValue.fromUzc(-1).abs());
        assertEquals(UzcValue.fromUzc(0), UzcValue.fromUzc(0).abs());
    }

    @Test
    public void testMin() {
        assertEquals(UzcValue.fromUzc(1), UzcValue.min(UzcValue.fromUzc(1), UzcValue.fromUzc(2)));
        assertEquals(UzcValue.fromUzc(-2), UzcValue.min(UzcValue.fromUzc(-1), UzcValue.fromUzc(-2)));
    }

    @Test
    public void testMax() {
        assertEquals(UzcValue.fromUzc(2), UzcValue.max(UzcValue.fromUzc(1), UzcValue.fromUzc(2)));
        assertEquals(UzcValue.fromUzc(-1), UzcValue.max(UzcValue.fromUzc(-1), UzcValue.fromUzc(-2)));
    }
}
