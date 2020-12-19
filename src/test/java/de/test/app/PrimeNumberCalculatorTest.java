package de.test.app;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class PrimeNumberCalculatorTest {
    public static final String EXPECTED_EXCEPTION_MESSAGE = "start needs to be >=1 and end > start";
    private PrimeNumberCalculator testee = new PrimeNumberCalculator();

    @Test
    public void calculatePrime_returnNonEmptyList() {
        List<Integer> listOfPrimes = testee.calculatePrime(1, 10);
        assertFalse(listOfPrimes.isEmpty());
    }

    @Test
    public void calculatePrime_returnListOfNumbersBetweenParams() {
        List<Integer> integers = testee.calculatePrime(1, 10);
        assertTrue(integers.size() > 1);
        assertTrue(integers.size() < 10);
    }

    @Test
    public void calculatePrime_noNegativeStart() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> testee.calculatePrime(-1, 10));
        assertEquals(illegalArgumentException.getMessage(), EXPECTED_EXCEPTION_MESSAGE);

    }

    @Test
    public void calculatePrime_StartBiggerThenEnd() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> testee.calculatePrime(100, 10));
        assertEquals(illegalArgumentException.getMessage(), EXPECTED_EXCEPTION_MESSAGE);
    }

    @Test
    public void calculatePrime_OrderOfFirstPrimes() {
        List<Integer> integers = testee.calculatePrime(1, 10);
        assertEquals(Integer.valueOf(2), integers.get(0));
        assertEquals(Integer.valueOf(3), integers.get(1));
        assertEquals(Integer.valueOf(5), integers.get(2));
        assertEquals(Integer.valueOf(7), integers.get(3));


    }

    // Example of Method Source parameterized Test

    @Tag("parameterized")
    @ParameterizedTest
    @MethodSource
    public void calculatePrime_ParameterizedTests_staticMethodSource(int start, int end, List<Integer> solution) {
        List<Integer> integers = testee.calculatePrime(start, end);
        assertTrue(integers.containsAll(solution));
    }

    public static Stream<Arguments> calculatePrime_ParameterizedTests_staticMethodSource() {
        return Stream.of(Arguments.arguments(1, 10, Arrays.asList(2, 3, 5, 7)),
                         Arguments.arguments(4, 10, Arrays.asList(5, 7)),
                         Arguments.arguments(10, 20, Arrays.asList(11, 13, 17, 19)));
    }

    @Tag("parameterized")
    @ParameterizedTest
    @CsvFileSource(resources = "/primeNumberStarterArguments.csv")
    public void calculatePrime_ParameterizedTests_CsvFileWithConverter(int start, int end,
                               @ConvertWith(ToPrimeNumberSolutionConverter.class) List<Integer> solution) {
        List<Integer> integers = testee.calculatePrime(start, end);
        assertTrue(integers.containsAll(solution));
    }

    @Tag("parameterized")
    @DisplayName("Automated Parameterized Prime Number Test")
    @ParameterizedTest(name = "{index} Test with start {0} and end {1} and the solution {2}")
    @CsvFileSource(resources = "/primeNumberStarterArguments.csv")
    public void calculatePrime_ParameterizedTests_CsvFileWithArgumentAccessor(ArgumentsAccessor arguments,
                                    @AggregateWith(ToPrimeNumberSolutionAggregator.class) List<Integer> solution) {
        List<Integer> integers = testee.calculatePrime(arguments.getInteger(0), arguments.get(1, Integer.class));
        assertTrue(integers.containsAll(solution));
    }

}
