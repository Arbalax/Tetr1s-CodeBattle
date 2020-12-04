/*

    НУЖНО ОПРЕДЕЛИТЬ МЕСТОПОЛОЖЕНИЕ КООРДИНАТЫ CurrentFigurePoint ДЛЯ КАЖДОЙ ФИГУРЫ

changePosition = CurrentFigurePoint - SelectedPoint;
if (changePosition < 0) {
    Math.abs(changePosition) * "RIGHT";
} else if (changePosition > 0) {
    Math.abs(changePosition) * "LEFT";
} else "";
*/

package com.codenjoy.dojo.bot;

import com.codenjoy.dojo.tetris.model.Glass;
import com.codenjoy.dojo.tetris.model.Layer;
import com.codenjoy.dojo.tetris.model.Result;
import javafx.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AI {

//    private int [][] glass;
    private String figure;

//    public void setGlass(int [][] glass) {
//        this.glass = glass;
//    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public int[][] glassToArray(Glass glass) {
        int[][] array = new int[18][18];
        for (int i = 0; i < array.length; i++) {
            Layer layer = glass.getLayers().get(i);
            for (int j = 0; j < array[i].length; j++){
                if (layer.getCells().get(j).isFilled())
                array[i][j] = 1;
                else array[i][j] = 0;
            }
        }
        return array;
    }

    private double calcPriority(int[][] newGlass) {

        final int HEIGHT_MULTIPLIER = 10;
        final int HOLES_MULTIPLIER = 20;
        final int WELLS_MULTIPLIER = 5;
        final int LINES_MULTIPLIER = 1;

        int fullLines = 0;
        int maxHeight = 0;
        int numberOfHoles = 0;
        int numberOfWells = 0;


        double priority;

        for (int i = newGlass.length - 1; i > 0; i--) {

            List<Integer> lineChecker = new ArrayList<>();

            for (int j = 0; j < newGlass[i].length; j++) {

                if (newGlass[i][j] == 1) {
                    lineChecker.add(1);
                    maxHeight = newGlass.length - i;
                } else lineChecker.add(0);

                if (newGlass[i][j] == 0 && newGlass[i - 1][j] == 1) {
                    numberOfHoles++;
                }

                if (j > 0 && j < (newGlass[i].length - 1)
                        && newGlass[i][j] == 0
                        && newGlass[i - 1][j] == 0
                        && newGlass[i][j - 1] == 1
                        && newGlass[i][j + 1] == 1
                )
                    numberOfWells++;

                if (j == 0 && newGlass[i][j] == 0
                        && newGlass[i - 1][j] == 0
                        && newGlass[i][j + 1] == 1
                )
                    numberOfWells++;

                if (j == (newGlass[i].length - 1) && newGlass[i][j] == 0
                        && newGlass[i - 1][j] == 0
                        && newGlass[i][j - 1] == 1
                )
                    numberOfWells++;
            }

            if (Collections.min(lineChecker) == 1) {
                fullLines++;
            }

        }

        priority = maxHeight * HEIGHT_MULTIPLIER +
                numberOfHoles * HOLES_MULTIPLIER +
                numberOfWells * WELLS_MULTIPLIER +
                fullLines * LINES_MULTIPLIER;

        System.out.println(priority);

        return priority;
    }

    public Result calcO(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMap = new HashMap<>();

        Result result = new Result();

//        for(int m=0; m<glass.length; m++) {
//            for(int n=0; n<glass[m].length; n++)
//                System.out.print(glass[m][n] + "\t");
//            System.out.println();
//        }

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j] == 0)
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMap.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                                               && (glass[i][j + 1] == 0)
                                               && (glass[i - 1][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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

    public Result calcI(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();

        Result result = new Result();

        // перебор для вертикального положения фигуры I

        for (int j = 0; j < glass.length; j++) {

            for (int i = 4; i < glass[j].length; i++) {

                if (glass[i][j] == 1) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 4][j] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        // перебор для горизонтального положения фигуры I

        for (int j = 0; j < glass.length - 3; j++) {


            for (int i = 4; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)
                        && (glass[i - 1][j + 3] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;
                    newArray[i - 1][j + 3] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)
                        && (glass[i][j + 3] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;
                    newArray[i][j + 3] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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
            result.setX(theBestPair.getValue()); // координата X
        }

        System.out.println(result);
        return result;
    }

    public Result calcJ(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation2 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation3 = new HashMap<>();

        Result result = new Result();

        // перебор для  |
        //             _|

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j + 1] == 0)
                        && (glass[i - 3][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j + 1] = 1;
                    newArray[i - 3][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        // перебор для |
        //             |________

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                }
            }
        }

        //              __
        // перебор для |
        //             |

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 3][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 3][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                }
            }
        }

        //              ________
        // перебор для          |
        //                      |

        for (int j = 2; j < glass.length; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j - 1] == 0)
                        && (glass[i - 2][j - 2] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;
                    newArray[i - 2][j - 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)
                        && (glass[i - 1][j - 2] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j - 1] = 1;
                    newArray[i - 1][j - 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                }
            }
        }

        double maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
        double maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());
        double maxPriorityRotation2 = Collections.min(resultMapRotation2.keySet());
        double maxPriorityRotation3 = Collections.min(resultMapRotation3.keySet());

        List <Double> maxPriorityArray = new ArrayList<>();
        maxPriorityArray.add(maxPriorityRotation0);
        maxPriorityArray.add(maxPriorityRotation1);
        maxPriorityArray.add(maxPriorityRotation2);
        maxPriorityArray.add(maxPriorityRotation3);
        double maxPriority = Collections.min(maxPriorityArray);


        // координаты, на которых должен размещаться нижний по возможности левый угол фигуры
        if (maxPriority == maxPriorityRotation0) {
            Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
            result.setRotation(0);
            result.setX(theBestPair.getValue());
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

    public Result calcL(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation2 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation3 = new HashMap<>();

        Result result = new Result();

        // перебор для  |
        //              |_

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 1][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        //              ________
        // перебор для |
        //             |

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j + 1] == 0)
                        && (glass[i - 2][j + 2] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j + 1] = 1;
                    newArray[i - 2][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                }
            }
        }

        //             __
        // перебор для   |
        //               |

        for (int j = 1; j < glass.length; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 3][j - 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 3][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 2][j - 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для          |
        //              ________|

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)
                        && (glass[i - 2][j + 2] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;
                    newArray[i - 2][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)
                        && (glass[i - 1][j + 2] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;
                    newArray[i - 1][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                }
            }
        }

        double maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
        double maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());
        double maxPriorityRotation2 = Collections.min(resultMapRotation2.keySet());
        double maxPriorityRotation3 = Collections.min(resultMapRotation3.keySet());

        List <Double> maxPriorityArray = new ArrayList<>();
        maxPriorityArray.add(maxPriorityRotation0);
        maxPriorityArray.add(maxPriorityRotation1);
        maxPriorityArray.add(maxPriorityRotation2);
        maxPriorityArray.add(maxPriorityRotation3);
        double maxPriority = Collections.min(maxPriorityArray);


        // координаты, на которых должен размещаться нижний по возможности левый угол фигуры
        if (maxPriority == maxPriorityRotation0) {
            Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
            result.setRotation(0);
            result.setX(theBestPair.getValue());
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

    public Result calcS(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();

        Result result = new Result();

        //                  __
        // перебор для   __|
        //

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j + 1] == 0)
                        && (glass[i - 2][j + 2] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j + 1] = 1;
                    newArray[i - 2][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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
                        && (glass[i - 3][j - 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;
                    newArray[i - 3][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)
                        && (glass[i - 2][j - 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j - 1] = 1;
                    newArray[i - 2][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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
            result.setX(theBestPair.getValue()); // координата X
        } else {
            Pair<Integer, Integer> theBestPair = resultMapRotation1.get(maxPriorityRotation1);
            result.setRotation(1); // один поворот на 90
            result.setX(theBestPair.getValue()); // координата X
        }

        System.out.println(result);
        return result;
    }

    public Result calcZ(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();

        Result result = new Result();

        //               __
        // перебор для     |__
        //

        for (int j = 1; j < glass.length - 1; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j - 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i - 1][j - 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для   _|
        //              |

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j + 1] == 0)
                        && (glass[i - 3][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j + 1] = 1;
                    newArray[i - 3][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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
            result.setX(theBestPair.getValue()); // координата X
        } else {
            Pair<Integer, Integer> theBestPair = resultMapRotation1.get(maxPriorityRotation1);
            result.setRotation(1); // один поворот на 90
            result.setX(theBestPair.getValue()); // координата X
        }

        System.out.println(result);
        return result;
    }

    public Result calcT(int[][] glass) throws InterruptedException {

        HashMap<Double, Pair<Integer, Integer>> resultMapRotation0 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation1 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation2 = new HashMap<>();
        HashMap<Double, Pair<Integer, Integer>> resultMapRotation3 = new HashMap<>();

        Result result = new Result();

        // перебор для  _|_
        //

        for (int j = 0; j < glass.length - 2; j++) {

            for (int i = 2; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 1][j + 1] == 0)
                        && (glass[i - 1][j + 2] == 0)
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j + 1] = 1;
                    newArray[i - 1][j + 2] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i][j + 1] == 0)
                        && (glass[i][j + 2] == 0)
                        && (glass[i - 1][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i][j + 1] = 1;
                    newArray[i][j + 2] = 1;
                    newArray[i - 1][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation0.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для |_
        //             |

        for (int j = 0; j < glass.length - 1; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation1.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 1][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

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
                        && (glass[i - 2][j + 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 2][j - 1] = 1;
                    newArray[i - 2][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)
                        && (glass[i - 1][j + 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 1][j - 1] = 1;
                    newArray[i - 1][j + 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation2.put(priority, new Pair<>(i, j));

                }
            }
        }

        //
        // перебор для  _|
        //               |

        for (int j = 1; j < glass.length; j++) {

            for (int i = 3; i < glass[j].length; i++) {

                if ((glass[i][j] == 1) && (glass[i - 2][j - 1] == 0)) {

                    System.out.println("Into first if");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++){
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 3][j] = 1;
                    newArray[i - 2][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                    break;
                }

                if ((i == glass[j].length - 1) && (glass[i][j] == 0)
                        && (glass[i - 1][j - 1] == 0)) {

                    System.out.println("Into second if ");

                    int [][] newArray = new int[glass[j].length][glass.length];

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            newArray[k][l]=glass[k][l];
                        }
                    }

                    newArray[i][j] = 1;
                    newArray[i - 1][j] = 1;
                    newArray[i - 2][j] = 1;
                    newArray[i - 1][j - 1] = 1;

                    for(int k=0; k<glass.length; k++) {
                        for(int l=0; l<glass[k].length; l++) {
                            System.out.print(newArray[k][l] + "\t");
                        }
                        System.out.println();
                    }

                    double priority = calcPriority(newArray);

                    TimeUnit.SECONDS.sleep(1);

                    resultMapRotation3.put(priority, new Pair<>(i, j));

                }
            }
        }

        double maxPriorityRotation0 = Collections.min(resultMapRotation0.keySet());
        double maxPriorityRotation1 = Collections.min(resultMapRotation1.keySet());
        double maxPriorityRotation2 = Collections.min(resultMapRotation2.keySet());
        double maxPriorityRotation3 = Collections.min(resultMapRotation3.keySet());

        List <Double> maxPriorityArray = new ArrayList<>();
        maxPriorityArray.add(maxPriorityRotation0);
        maxPriorityArray.add(maxPriorityRotation1);
        maxPriorityArray.add(maxPriorityRotation2);
        maxPriorityArray.add(maxPriorityRotation3);
        double maxPriority = Collections.min(maxPriorityArray);


        // координаты, на которых должен размещаться нижний по возможности левый угол фигуры
        if (maxPriority == maxPriorityRotation0) {
            Pair<Integer, Integer> theBestPair = resultMapRotation0.get(maxPriorityRotation0);
            result.setRotation(0);
            result.setX(theBestPair.getValue());
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



