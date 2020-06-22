package uzc.kit.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class UzcValue implements Comparable<UzcValue> {
    private static final int decimals = 8;

    public static final UzcValue ZERO = UzcValue.fromPlanck(0);
    
    private final BigInteger planck;

    private UzcValue(BigInteger planck) {
        this.planck = planck;
    }

    /**
     * @param planck The number of planck
     * @return The UzcValue representing this number of planck, or a UzcValue representing 0 Uzc if the string could not be parsed
     */
    public static UzcValue fromPlanck(String planck) {
        if (planck == null) return ZERO;
        if (planck.toLowerCase(Locale.ENGLISH).endsWith(" planck")) {
            planck = planck.substring(0, planck.length() - 7);
        }
        try {
            return fromPlanck(new BigInteger(planck));
        } catch (NumberFormatException e) {
            return fromPlanck(BigInteger.ZERO);
        }
    }

    /**
     * @param planck The number of planck
     * @return The UzcValue representing this number of planck
     */
    public static UzcValue fromPlanck(long planck) {
        return fromPlanck(BigInteger.valueOf(planck));
    }

    public static UzcValue fromPlanck(BigInteger planck) {
        if (planck == null) return ZERO;
        return new UzcValue(planck);
    }

    /**
     * @param uzc The number of uzc
     * @return The UzcValue representing this number of uzc, or a UzcValue representing 0 Uzc if the string could not be parsed
     */
    public static UzcValue fromUzc(String uzc) {
        if (uzc == null) return ZERO;
        if (uzc.toLowerCase(Locale.ENGLISH).endsWith(" uzc")) {
            uzc = uzc.substring(0, uzc.length() - 6);
        }
        try {
            return fromUzc(new BigDecimal(uzc));
        } catch (NumberFormatException e) {
            return fromPlanck(BigInteger.ZERO);
        }
    }

    /**
     * @param uzc The number of uzc
     * @return The UzcValue representing this number of uzc
     */
    public static UzcValue fromUzc(double uzc) {
        return fromUzc(BigDecimal.valueOf(uzc));
    }

    public static UzcValue fromUzc(BigDecimal uzc) {
        if (uzc == null) return ZERO;
        return new UzcValue(uzc.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
    }

    private static BigDecimal roundToThreeDP(BigDecimal in) {
        if (in.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return in.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros();
        }
    }

    @Override
    public String toString() {
        return toFormattedString();
    }

    /**
     * @return The value with the "UZC" suffix and rounded to 3 decimal places
     */
    public String toFormattedString() {
        return roundToThreeDP(toUzc()).toPlainString() + " UZC";
    }

    /**
     * @return The value without the "UZC" suffix and without rounding
     */
    public String toUnformattedString() {
        return toUzc().stripTrailingZeros().toPlainString();
    }

    /**
     * @return A BigInteger representing the number of planck
     */
    public BigInteger toPlanck() {
        return planck;
    }

    public BigDecimal toUzc() {
        return new BigDecimal(planck, decimals);
    }

    public UzcValue add(UzcValue other) {
        return fromPlanck(planck.add(other.planck));
    }

    public UzcValue subtract(UzcValue other) {
        return fromPlanck(planck.subtract(other.planck));
    }

    public UzcValue multiply(long multiplicand) {
        return fromPlanck(planck.multiply(BigInteger.valueOf(multiplicand)));
    }

    public UzcValue multiply(double multiplicand) {
        return fromUzc(toUzc().multiply(BigDecimal.valueOf(multiplicand)));
    }

    public UzcValue multiply(BigInteger multiplicand) {
        return fromPlanck(planck.multiply(multiplicand));
    }

    public UzcValue multiply(BigDecimal multiplicand) {
        return fromUzc(toUzc().multiply(multiplicand));
    }

    public UzcValue divide(long divisor) {
        return fromPlanck(planck.divide(BigInteger.valueOf(divisor)));
    }

    public UzcValue divide(double divisor) {
        return fromUzc(toUzc().divide(BigDecimal.valueOf(divisor), decimals, RoundingMode.HALF_UP));
    }
    
    public UzcValue divide(BigInteger divisor) {
        return fromPlanck(planck.divide(divisor));
    }

    public UzcValue divide(BigDecimal divisor) {
        return fromUzc(toUzc().divide(divisor, decimals, RoundingMode.HALF_UP));
    }

    public UzcValue abs() {
        return fromPlanck(planck.abs());
    }

    @Override
    public int compareTo(UzcValue other) {
        if (other == null) return 1;
        return planck.compareTo(other.planck);
    }

    public static UzcValue min(UzcValue a, UzcValue b) {
        return (a.compareTo(b) <= 0) ? a : b;
    }

    public static UzcValue max(UzcValue a, UzcValue b) {
        return (a.compareTo(b) >= 0) ? a : b;
    }

    /**
     * @return The number of Uzc as a double
     */
    public double doubleValue() { // TODO test
        return toUzc().doubleValue();
    }

    /**
     * @return The number of planck as a long
     */
    public long longValue() { // TODO test
        return toPlanck().longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UzcValue that = (UzcValue) o;

        return planck != null ? planck.equals(that.planck) : that.planck == null;
    }

    @Override
    public int hashCode() {
        return planck != null ? planck.hashCode() : 0;
    }
}
