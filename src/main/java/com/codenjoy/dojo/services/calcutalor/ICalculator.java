package com.codenjoy.dojo.services.calcutalor;

import com.codenjoy.dojo.tetris.model.Result;
import javafx.util.Pair;

import java.util.Collections;
import java.util.HashMap;

public class ICalculator extends AbstractCalculator {
    @Override
    public Result calculate(int[][] glass) {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();

        // перебор для вертикального положения фигуры I

        for (int j = 0; j < glass.length; j++) {

            for (int i = 4; i < glass[j].length; i++) {

                if (glass[i][j] == 1) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 4][j] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation0.put(priority, new Pair<>(i, j));
                }
            }
        }

        // перебор для горизонтального положения фигуры I

        for (int j = 0; j < glass.length - 3; j++) {


            for (int i = 4; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)
                        && (glass[i - 1][j + 3] == 0)
                        && isFreeAbove(glass, (i - 1), (j + 1), (j + 3))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;
                    newArray[i - 1][j + 3] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)
                        && (glass[i][j + 3] == 0)
                        && isFreeAbove(glass, (i), (j + 1), (j + 3))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;
                    newArray[i][j + 3] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation1.put(priority, new Pair<>(i, j));
                }
            }
        }

        double maxPriorityRotation0 = 1000;

        if (!resultMapRotation0.isEmpty()) {
            System.out.println("Into first check");
            maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
            System.out.println(maxPriorityRotation0);
        }

        double maxPriorityRotation1 = 1000;

        if (!resultMapRotation1.isEmpty()) {
            System.out.println("Into second check");
            maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());
            System.out.println(maxPriorityRotation1);
        }


        // координаты, на которых должен размещаться левый нижний угол фигуры
        if (maxPriorityRotation0 < maxPriorityRotation1) {
            Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
            result.setRotation(0); // без поворота
            result.setX(theBestPair.getValue()); // координата X
        } else {
            Pair<Integer, Integer> theBestPair = resultMapRotation1.get(maxPriorityRotation1);
            result.setRotation(1); // один поворот на 90
            result.setX(theBestPair.getValue() + 2); // координата X
        }

        System.out.println(result);
        return result;
    }
}
