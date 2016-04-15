package challenge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static challenge.ExecutorTest.Operator.DIVISE;
import static challenge.ExecutorTest.Operator.FOIS;
import static challenge.ExecutorTest.Operator.MOINS;
import static challenge.ExecutorTest.Operator.PLUS;

public class ExecutorTest {

    @Test
    public void executorTest() throws Exception {

        String input = "8 50 10 7 2 3 = 556";

        Pattern compile = Pattern.compile("(\\d+) (\\d+) (\\d+) (\\d+) (\\d+) (\\d+) = (\\d+)");
        Matcher matcher = compile.matcher(input);

        matcher.matches();

        List<Double> originalNumbers = extractNumbers(matcher);
        Double wantedValue = Double.parseDouble(matcher.group(7));

        System.out.println(originalNumbers);
        System.out.println(wantedValue);

        int tries = 0;

        while (true) {

            List<Double> numbers = new ArrayList<>(originalNumbers);

            Collections.shuffle(numbers, new Random(System.nanoTime()));
            List<Operator> operators = generateNextOperators();

            tries++;

            Double calculate = calculate(new ArrayList<>(numbers), operators);
            if (wantedValue.equals(calculate)) {

                System.out.println(numbers);
                System.out.println(operators);
                System.out.println("Tries : " + tries);

                return;
            }
        }
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

    public List<Operator> generateNextOperators() {
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
