package com.codenjoy.dojo.services.calcutalor;

import com.codenjoy.dojo.tetris.model.Result;
import javafx.util.Pair;

import java.util.Collections;
import java.util.HashMap;

public class OCalculator extends AbstractCalculator {
    @Override
    public Result calculate(int[][] glass) {
        HashMap<Double, Pair<Integer, Integer>> resultMap = new HashMap<>();

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j] == 0)
                        && (glass[i - 2][j + 1] == 0)
                        && isFreeAbove(glass, (i - 2), (j + 1), (j + 1))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMap.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && isFreeAbove(glass, (i - 1), (j + 1), (j + 1))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMap.put(priority, new Pair<>(i, j));
                }

            }
        }

        System.out.println("resultMap in calcO" + resultMap);

        double maxPriority = Collections.min(resultMap.keySet());

        // координаты, на которых должен размещаться левый нижний угол фигуры
        Pair<Integer, Integer> theBestPair = resultMap.get(maxPriority);

        result.setRotation(0);
        result.setX(theBestPair.getValue());

        return result;
    }
}
