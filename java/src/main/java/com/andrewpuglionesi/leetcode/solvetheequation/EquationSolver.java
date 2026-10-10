package com.andrewpuglionesi.leetcode.solvetheequation;

/**
 * Solves simple polynomial equations of the form Ax+b=Cx+d, where A, b, C, and d are integers.
 */
public class EquationSolver {
    /**
     * Indicates that an equation has no solution.
     */
    public static final String NO_SOLUTION = "No solution";
    /**
     * Indicates that an equation has infinite solutions.
     */
    public static final String INFINITE_SOLUTIONS = "Infinite solutions";

    /**
     * Solves a first-order polynomial equation of the form Ax+b=Cx+d. The provided equation does not need to be fully
     * simplified.
     * @param equation a string-encoded equation with a first-order polynomial on either side of the equals sign. All
     *                 coefficients and constants must be integers. Addition, subtraction, and multiplication by
     *                 coefficients are the only operations allowed. Adjacent '+' and '-' symbols will result in an
     *                 exception.
     * @param variableName the variable name used in the provided equation. For example, if the equation is "foo+3=7",
     *                 the variableName is "foo". This is used to interpret the equation, and a mismatched name will
     *                 result in an exception. Must be alphabetical.
     * @return {@value #NO_SOLUTION} if the equation has no solutions; {@value #INFINITE_SOLUTIONS} if the equation is
     * true for all values of x; or in the case where there's one solution, a string representing the floating point
     * value of x.
     * @throws IllegalArgumentException if {@code equation} or {@code variableName} are found to have an invalid format.
     * See {@link FirstOrderPolynomial#buildFromString(String, String)} for  more information on valid formatting.
     */
    @SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
    public String solveFirstOrderPolynomialEquation(final String equation, final String variableName) {
        final String[] bothSides = equation.split("=");
        if (bothSides.length != 2) {
            throw new IllegalArgumentException("Equation must have exactly one equals sign.");
        }
        FirstOrderPolynomial leftSide = FirstOrderPolynomial.buildFromString(bothSides[0], variableName);
        FirstOrderPolynomial rightSide = FirstOrderPolynomial.buildFromString(bothSides[1], variableName);

        // group all the variable coefficients on the left side
        leftSide.xCoefficient -= rightSide.xCoefficient;
        rightSide.xCoefficient = 0;

        // group the constants on the right side
        rightSide.constant -= leftSide.constant;
        leftSide.constant = 0;

        if (leftSide.xCoefficient == 0 && rightSide.constant != 0) {
            return NO_SOLUTION; // 0x = 0, so the equation implies that 0 equals a non-zero number. i.e., it's rubbish
        }

        if (leftSide.xCoefficient == 0 && rightSide.constant == 0) {
            return INFINITE_SOLUTIONS; // this happens when both sides are equal for all x values, e.g., 2x+1=1+2x
        }

        return String.valueOf(rightSide.constant / leftSide.xCoefficient);
    }
}
