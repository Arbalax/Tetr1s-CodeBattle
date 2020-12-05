package com.codenjoy.dojo.services.calcutalor;

import com.codenjoy.dojo.tetris.model.Result;
import javafx.util.Pair;

import java.util.Collections;
import java.util.HashMap;

public class SCalculator extends AbstractCalculator {
    @Override
    public Result calculate(int[][] glass) {
            HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
            HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();

            //                  __
            // перебор для   __|
            //

            for (int j = 0; j < glass.length - 2; j++) {

                for (int i = 2; i < glass[j].length; i++) {

                    if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                            && (glass[i - 2][j + 1] == 0)
                            && (glass[i - 2][j + 2] == 0)
                            && isFreeAbove(glass, (i - 2),(j + 1),(j + 2) )) {

                        System.out.println("Into first if");

                        int[][] newArray = copyToNewArray(glass);

                        newArray[i - 1][j] = 1;
                        newArray[i - 1][j + 1] = 1;
                        newArray[i - 2][j + 1] = 1;
                        newArray[i - 2][j + 2] = 1;

                        showCurrentArray(newArray);

                        double priority = calculatePriority(newArray);

                        resultMapRotation0.put(priority, new Pair<>(i, j));

                        break;
                    }

                    if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                            && (glass[i][j + 1] == 0)
                            && (glass[i - 1][j + 1] == 0)
                            && (glass[i - 1][j + 2] == 0)
                            && isFreeAbove(glass, (i - 1),(j + 1),(j + 2) )) {

                        System.out.println("Into second if ");

                        int[][] newArray = copyToNewArray(glass);

                        newArray[i][j] = 1;
                        newArray[i][j + 1] = 1;
                        newArray[i - 1][j + 1] = 1;
                        newArray[i - 1][j + 2] = 1;

                        showCurrentArray(newArray);

                        double priority = calculatePriority(newArray);

                        resultMapRotation0.put(priority, new Pair<>(i, j));

                    }
                }
            }

            //
            // перебор для |_
            //               |

            for (int j = 1; j < glass.length; j++) {

                for (int i = 3; i < glass[j].length; i++) {

                    if ((glass[i][j] == 1) && (glass[i - 2][j - 1] == 0)
                            && (glass[i - 3][j - 1] == 0)
                            && isFreeAbove(glass, (i - 3),(j - 1),(j - 1) )) {

                        System.out.println("Into first if");

                        int[][] newArray = copyToNewArray(glass);

                        newArray[i - 1][j] = 1;
                        newArray[i - 2][j] = 1;
                        newArray[i - 2][j - 1] = 1;
                        newArray[i - 3][j - 1] = 1;

                        showCurrentArray(newArray);

                        double priority = calculatePriority(newArray);


                        resultMapRotation1.put(priority, new Pair<>(i, j));

                        break;
                    }

                    if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                            && (glass[i - 1][j - 1] == 0)
                            && (glass[i - 2][j - 1] == 0)
                            && isFreeAbove(glass, (i - 2),(j - 1),(j - 1) )) {

                        System.out.println("Into second if ");

                        int[][] newArray = copyToNewArray(glass);

                        newArray[i][j] = 1;
                        newArray[i - 1][j] = 1;
                        newArray[i - 1][j - 1] = 1;
                        newArray[i - 2][j - 1] = 1;

                        showCurrentArray(newArray);

                        double priority = calculatePriority(newArray);

                        resultMapRotation1.put(priority, new Pair<>(i, j));

                    }
                }
            }

            double maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
            double maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());

            // координаты, на которых должен размещаться левый нижний угол фигуры
            if (maxPriorityRotation0 < maxPriorityRotation1) {
                Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
                result.setRotation(0); // без поворота
                result.setX(theBestPair.getValue() + 1); // координата X
            } else {
                Pair<Integer, Integer> theBestPair = resultMapRotation1.get(maxPriorityRotation1);
                result.setRotation(1); // один поворот на 90
                result.setX(theBestPair.getValue() - 1); // координата X
            }

            System.out.println(result);
            return result;
    }
}
