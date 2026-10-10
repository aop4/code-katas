package com.andrewpuglionesi.leetcode.solvetheequation;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a first-order polynomial of the form <i>Ax + b</i>.
 */
public class FirstOrderPolynomial {
    /**
     * The coefficient for the variable of the polynomial. e.g., <i>A</i> in the equation <i>Ax + b</i>.
     */
    public double xCoefficient;
    /**
     * The constant term in the polynomial, e.g. <i>b</i> in the equation <i>Ax + b</i>.
     */
    public double constant;

    /**
     * Attempts to build a {@link FirstOrderPolynomial} object from a string-encoded representation of the polynomial.
     * See below for guidance on acceptable input.
     * @param polynomialString should consist of constant integers and variable terms with a variable name equal to
     *                         {@code variableName}. Constants and coefficients must be whole integers, not decimal
     *                         numbers. Plus and minus signs are allowed as long as they precede a constant or variable.
     *                         <br>Example of valid input: {@code "-x + 2 + 4x - 3"}
     * @param variableName the name of the variable that will be mapped to {@code xCoefficient} in the resulting
     *                     polynomial. Must contain only alphabetical characters.
     * @return a {@link FirstOrderPolynomial} that is a simplified representation of the terms in
     * {@code polynomialString}. All like terms will be summed and grouped together in the output.
     * @throws IllegalArgumentException if either of the arguments are not formatted as expected.
     */
    public static FirstOrderPolynomial buildFromString(final String polynomialString, final String variableName) {
        validateVariableName(variableName);
        // remove whitespace from expression
        final String trimmedPolynomial = polynomialString.replaceAll("\s", "");

        validatePolynomialString(trimmedPolynomial, variableName);

        FirstOrderPolynomial polynomial = new FirstOrderPolynomial();

        // sum up all first-order terms in the polynomial
        Pattern.compile(String.format("[\\+\\-]?[\\d]*%s", variableName))
                .matcher(trimmedPolynomial)
                .results()
                .map(MatchResult::group)
                .forEach(term -> {
                    if (variableName.equals(term) || String.format("+%s", variableName).equals(term)) {
                        polynomial.xCoefficient += 1;
                    } else if (String.format("-%s", variableName).equals(term)) {
                        polynomial.xCoefficient -= 1;
                    } else {
                        term = term.replace(variableName, "");
                        polynomial.xCoefficient += Integer.parseInt(term);
                    }
                });

        // sum up all zero-order terms (constants) in the polynomial
        Pattern.compile(String.format("[\\+\\-]?[\\d]+(%s)?", variableName))
                .matcher(trimmedPolynomial)
                .results()
                .map(MatchResult::group)
                .forEach(term -> {
                    if (!term.contains(variableName)) {
                        polynomial.constant += Integer.parseInt(term);
                    }
                });

        return polynomial;
    }

    private static void validateVariableName(final String variableName) {
        if (!StringUtils.isAlpha(variableName)) {
            throw new IllegalArgumentException("variableName must consist of only alphabetical characters.");
        }
    }

    /**
     * Validates a string-encoded polynomial according to the following rules:
     * <ul>
     *     <li>{@code polynomialString} must contain only the variable name and '+', '-', and integer characters.</li>
     *     <li>The variable name may not be immediately followed by itself or an integer.</li>
     *     <li>A plus or minus sign cannot be followed by another plus or minus sign, even if mathematically valid.</li>
     * </ul>
     * @param polynomialString a string-encoded representation of the polynomial.
     * @param variableName the variable name in the polynomial.
     * @throws IllegalArgumentException if the polynomial is not in the expected format.
     */
    private static void validatePolynomialString(final String polynomialString, final String variableName) {
        final Pattern acceptedChars = Pattern.compile(String.format("([\\+\\-\\d]*(%s)*)*", variableName));
        final boolean matches = acceptedChars.matcher(polynomialString).matches()
                && !polynomialString.contains(variableName + variableName)
                && !Pattern.compile(String.format("%s\\d", variableName)).matcher(polynomialString).find()
                && !Pattern.compile("[+-]{2}").matcher(polynomialString).find();
        if (!matches) {
            throw new IllegalArgumentException("Invalid polynomial equation detected: " + polynomialString);
        }
    }
}
