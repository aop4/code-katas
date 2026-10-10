package com.andrewpuglionesi.leetcode.solvetheequation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EquationSolverTest {
    private EquationSolver equationSolver;
    private static final double EPSILON = 0.000001d;

    @BeforeEach
    void setup() {
        this.equationSolver = new EquationSolver();
    }

    @Test
    void solveEquationNoEqualsSign() {
        assertThrows(IllegalArgumentException.class, () -> equationSolver.solveFirstOrderPolynomialEquation("x", "x"));
    }

    @Test
    void solveEquationTwoEqualsSigns() {
        assertThrows(IllegalArgumentException.class, () -> equationSolver.solveFirstOrderPolynomialEquation("x=1=1", "x"));
    }

    @Test
    void solveEquationBasicEqualityStatement() {
        String output = equationSolver.solveFirstOrderPolynomialEquation("x=5", "x");
        assertEquals(5, Double.parseDouble(output), EPSILON);
    }

    @Test
    void solveEquationIdentityFunction() {
        assertEquals(EquationSolver.INFINITE_SOLUTIONS,
                equationSolver.solveFirstOrderPolynomialEquation("x=x", "x"));
        assertEquals(EquationSolver.INFINITE_SOLUTIONS,
                equationSolver.solveFirstOrderPolynomialEquation("2x + 5 = 5 + 2x", "x"));
    }

    @Test
    void solveEquationImpossibleEquality() {
        assertEquals(EquationSolver.NO_SOLUTION,
                equationSolver.solveFirstOrderPolynomialEquation("2=5", "x"));
        assertEquals(EquationSolver.NO_SOLUTION,
                equationSolver.solveFirstOrderPolynomialEquation("2 + x = 5 + x", "x"));
    }

    @Test
    void solveEquationXHasMultiplier() {
        String output = equationSolver.solveFirstOrderPolynomialEquation("2x=5", "x");
        assertEquals(5.0 / 2.0, Double.parseDouble(output), EPSILON);
    }

    @Test
    void solveEquationXEqualsZero() {
        String output = equationSolver.solveFirstOrderPolynomialEquation("2x+5=5", "x");
        assertEquals(0, Double.parseDouble(output));
    }

    @Test
    void solveEquationMixedTerms() {
        String output = equationSolver.solveFirstOrderPolynomialEquation("-3 + 4x + 5 -2x = -x + 1", "x");
        // Proof:
        //   -3 + 4x + 5 -2x = -x + 1
        //   2x + 2 = -x + 1
        //   3x = -1
        //   x = -1/3
        assertEquals(-1.0 / 3.0, Double.parseDouble(output), EPSILON);
    }
}
