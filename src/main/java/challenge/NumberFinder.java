package challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static challenge.NumberFinder.Operator.DIVISE;
import static challenge.NumberFinder.Operator.FOIS;
import static challenge.NumberFinder.Operator.MOINS;
import static challenge.NumberFinder.Operator.PLUS;

public class NumberFinder {

    public static void main(String... args) {

        for (int i = 0; i < 100; i++)
            new NumberFinder().find(args[0]);

    }

    private void find(String input) {

        long startTime = System.nanoTime();

        Pattern compile = Pattern.compile("(\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) = (\\d+)");
        Matcher matcher = compile.matcher(input);

        matcher.matches();

        List<Double> originalNumbers = extractNumbers(matcher);
        Double wantedValue = Double.parseDouble(matcher.group(7));

        int tries = 0;

        while (true) {

            List<Double> numbers = new ArrayList<>(originalNumbers);

            Collections.shuffle(numbers, new Random(System.nanoTime()));
            List<Operator> operators = generateNextOperators();

            tries++;

            Double calculate = calculate(new ArrayList<>(numbers), operators);
            if (wantedValue.equals(calculate)) {

                long stopTime = System.nanoTime();
                double timeInMillis = ((double) stopTime - (double) startTime) / 1_000_000;

                System.out.println(generateStringResult(numbers, operators, wantedValue));
                System.out.println("Tries : " + tries);
                System.out.println("Time : " + timeInMillis + "ms");
                System.out.println();

                return;
            }
        }
    }

    private String getSymboleForOperator(Operator operator) {
        switch (operator) {
            case PLUS:
                return " + ";
            case MOINS:
                return " - ";
            case FOIS:
                return " * ";
            case DIVISE:
                return " / ";
        }
        throw new IllegalStateException();
    }

    private String generateStringResult(List<Double> numbers, List<Operator> operators, Double wantedValue) {
        String result = "((((";

        Iterator<Double> numbersIterator = numbers.iterator();
        Iterator<Operator> operatorsIterator = operators.iterator();

        result += numbersIterator.next().intValue();

        while (numbersIterator.hasNext()) {
            Double number = numbersIterator.next();
            Operator operator = operatorsIterator.next();
            result += getSymboleForOperator(operator) + number.intValue();
            if (numbersIterator.hasNext()) {
                result += ")";
            }
        }

        return result + " = " + wantedValue.intValue();
    }

    private Double calculate(List<Double> originalNumbers, List<Operator> operators) {

        Double currentValue = originalNumbers.get(0);
        for (int i = 1; i <= 5; i++) {
            switch (operators.get(i - 1)) {
                case PLUS:
                    currentValue += originalNumbers.get(i);
                    continue;
                case MOINS:
                    currentValue -= originalNumbers.get(i);
                    continue;
                case FOIS:
                    currentValue *= originalNumbers.get(i);
                    continue;
                case DIVISE:
                    if (originalNumbers.get(i) == 0) {
                        return -1d;
                    } else {
                        currentValue /= originalNumbers.get(i);
                    }
                    continue;
            }
        }
        return currentValue;
    }

    private List<Double> extractNumbers(Matcher matcher) {
        List<Double> numbers = new ArrayList<>(6);

        for (int i = 0; i <= 5; i++) {
            numbers.add(Double.parseDouble(matcher.group(i + 1)));
        }

        return numbers;
    }

    private List<Operator> generateNextOperators() {
        List<Operator> operators = new ArrayList<>(6);
        for (int i = 0; i <= 4; i++) {
            operators.add(randomOperator());
        }
        return operators;
    }

    private Operator randomOperator() {
        switch (new Random().nextInt(4)) {
            case 0:
                return PLUS;
            case 1:
                return MOINS;
            case 2:
                return FOIS;
            case 3:
                return DIVISE;
        }
        throw new IllegalStateException();
    }

    enum Operator {
        PLUS, MOINS, FOIS, DIVISE;
    }
}
