package com.codenjoy.dojo.services.calcutalor;

import com.codenjoy.dojo.tetris.model.Result;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TCalculator extends AbstractCalculator {
    @Override
    public Result calculate(int[][] glass) {
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation2 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation3 = new HashMap<>();

        // перебор для  _|_
        //

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)
                        && (glass[i - 2][j + 1] == 0)
                        && (glass[i - 2][j + 2] == 0
                        && isFreeAbove(glass, (i - 2), (j + 1), (j + 2)))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;
                    newArray[i - 2][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0
                        && isFreeAbove(glass, (i - 1), (j + 1), (j + 2)))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;
                    newArray[i - 1][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);


                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для |_
        //             |

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j + 1] == 0)
                        && isFreeAbove(glass, (i - 2), (j + 1), (j + 1))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 2][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && isFreeAbove(glass, (i - 1), (j + 1), (j + 1))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 1][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для ___
        //              |

        for (int j = 1; j < glass.length - 1; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j - 1] == 0)
                        && (glass[i - 2][j + 1] == 0)
                        && isFreeAbove(glass, (i - 2), (j - 1), (j + 1))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;
                    newArray[i - 2][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);


                    resultMapRotation2.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && isFreeAbove(glass, (i - 1), (j - 1), (j + 1))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j - 1] = 1;
                    newArray[i - 1][j + 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);


                    resultMapRotation2.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для  _|
        //               |

        for (int j = 1; j < glass.length; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j - 1] == 0)
                        && isFreeAbove(glass, (i - 2), (j - 1), (j - 1))) {

                    System.out.println("Into first if");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 2][j - 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);


                    resultMapRotation3.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)
                        && isFreeAbove(glass, (i - 1), (j - 1), (j - 1))) {

                    System.out.println("Into second if ");

                    int[][] newArray = copyToNewArray(glass);

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 1][j - 1] = 1;

                    showCurrentArray(newArray);

                    double priority = calculatePriority(newArray);


                    resultMapRotation3.put(priority, new Pair<>(i, j));

                }
            }
        }

        double maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
        double maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());
        double maxPriorityRotation2 = Collections.min(resultMapRotation2.keySet());
        double maxPriorityRotation3 = Collections.min(resultMapRotation3.keySet());

        List<Double> maxPriorityArray = new ArrayList<>();
        maxPriorityArray.add(maxPriorityRotation0);
        maxPriorityArray.add(maxPriorityRotation1);
        maxPriorityArray.add(maxPriorityRotation2);
        maxPriorityArray.add(maxPriorityRotation3);
        double maxPriority = Collections.min(maxPriorityArray);


        // координаты, на которых должен размещаться нижний по возможности левый угол фигуры
        if (maxPriority == maxPriorityRotation0) {
            Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
            result.setRotation(0);
            result.setX(theBestPair.getValue() + 1);
        }

        if (maxPriority == maxPriorityRotation1) {
            Pair<Integer, Integer> theBestPair = resultMapRotation1.get(maxPriorityRotation1);
            result.setRotation(1);
            result.setX(theBestPair.getValue());
        }

        if (maxPriority == maxPriorityRotation2) {
            Pair<Integer, Integer> theBestPair = resultMapRotation2.get(maxPriorityRotation2);
            result.setRotation(2);
            result.setX(theBestPair.getValue());
        }

        if (maxPriority == maxPriorityRotation3) {
            Pair<Integer, Integer> theBestPair = resultMapRotation3.get(maxPriorityRotation3);
            result.setRotation(3);
            result.setX(theBestPair.getValue());
        }

        System.out.println(result);
        return result;
    }
}
