package com.codenjoy.dojo.services.calcutalor;


public class CalculatorFactory {
    private Calculator calculator;

    public Calculator create(char figureChar) {
        switch (figureChar) {
            case 'O':
                calculator = new OCalculator();
                break;
            case 'I':
                calculator = new ICalculator();
                break;
            case 'J':
                calculator = new JCalculator();
                break;
            case 'L':
                calculator = new LCalculator();
                break;
            case 'S':
                calculator = new SCalculator();
                break;
            case 'Z':
                calculator = new ZCalculator();
                break;
            case 'T':
                calculator = new TCalculator();
                break;
        }
        return calculator;
    }
}
