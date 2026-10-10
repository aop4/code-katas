package com.andrewpuglionesi.leetcode.solvetheequation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FirstOrderPolynomialTest {
    private static final String FOO = "foo";

    @Test
    void buildFromStringVariableWithoutMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString(FOO, FOO);
        assertEquals(1, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithNegativeSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-foo", FOO);
        assertEquals(-1, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithPlusSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("+foo", FOO);
        assertEquals(1, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithPositiveMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("3foo", FOO);
        assertEquals(3, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithNegativeMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-3foo", FOO);
        assertEquals(-3, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithZeroMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("0foo", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithPlusZeroMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("+0foo", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringVariableWithMinusZeroMultiplier() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-0foo", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringConstant() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("3", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(3, polynomial.constant);
    }

    @Test
    void buildFromStringConstantWithPlusSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("+3", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(3, polynomial.constant);
    }

    @Test
    void buildFromStringConstantWithMinusSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-3", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(-3, polynomial.constant);
    }

    @Test
    void buildFromStringZeroConstant() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("0", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringZeroConstantPlusSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("+0", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringZeroConstantMinusSign() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-0", FOO);
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringAddition() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("+3foo + 7", FOO);
        assertEquals(3, polynomial.xCoefficient);
        assertEquals(7, polynomial.constant);
    }

    @Test
    void buildFromStringSubtraction() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-3foo - 7", FOO);
        assertEquals(-3, polynomial.xCoefficient);
        assertEquals(-7, polynomial.constant);
    }

    @Test
    void buildFromStringManyTerms() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("-60 + 12foo - foo + 1 - 999foo + 0 - 0foo", FOO);
        assertEquals(-988, polynomial.xCoefficient);
        assertEquals(-59, polynomial.constant);
    }

    @Test
    void buildFromStringEmptyPolynomial() {
        FirstOrderPolynomial polynomial = FirstOrderPolynomial.buildFromString("", "x");
        assertEquals(0, polynomial.xCoefficient);
        assertEquals(0, polynomial.constant);
    }

    @Test
    void buildFromStringInvalidVariableName() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("+21", "+21"));
    }

    @Test
    void buildFromStringPolynomialUnexpectedVariable() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo+bar", FOO));
    }

    @Test
    void buildFromStringPolynomialPlusPlus() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo++21", FOO));
    }

    @Test
    void buildFromStringPolynomialPlusMinus() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo+-21", FOO));
    }

    @Test
    void buildFromStringPolynomialMinusMinus() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo--21", FOO));
    }

    @Test
    void buildFromStringPolynomialMinusPlus() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo-+21", FOO));
    }

    @Test
    void buildFromStringDuplicatedVariable() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foofoo", FOO));
    }

    @Test
    void buildFromStringNumberAfterVariable() {
        assertThrows(IllegalArgumentException.class, () -> FirstOrderPolynomial.buildFromString("foo3", FOO));
    }
}
