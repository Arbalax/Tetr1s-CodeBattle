package com.codenjoy.dojo.services.calcutalor;

import com.codenjoy.dojo.tetris.model.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCalculator implements Calculator {
    protected Result result;

    private final double HEIGHT_MULTIPLIER = 10.0;  //10
    private final double HOLES_MULTIPLIER = 20.0; //20
    private final double WELLS_MULTIPLIER = 3.0;  //5
    private final double WIDE_WELLS_MULTIPLIER = 7.0;
    private final double LINE_NUMBER_MULTIPLIER = 2.1;  //1
    private final double LINES_MULTIPLIER = 0.5;

    public AbstractCalculator() {
        this.result = new Result();
    }

    protected void showCurrentArray(int[][] array) {
        for (int[] ints : array) {
            for (int anInt : ints) {
                System.out.print(anInt + "\t");
            }
            System.out.println();
        }
    }

    protected boolean isFreeAbove(int[][] glass, int figureUpperLine, int startColumn, int finishColumn) {
        boolean flag = true;

        for (int j = startColumn; j <= finishColumn; j++) {
            for (int i = figureUpperLine - 1; i > 2; i--) {
                if (glass[i][j] != 0) {
                    flag = false;

                    break;
                }
            }
        }
        return flag;
    }

    protected int[][] copyToNewArray(int[][] glass) {
        int[][] newArray = new int[18][18];

        for (int i = 0; i < glass.length; i++) {
            for (int j = 0; j < glass[i].length; j++) {
                newArray[i][j] = glass[i][j];
            }
        }
        return newArray;
    }

    protected double calculatePriority(int[][] newGlass) {
        int fullLines = 0;
        int maxHeight = 0;
        int numberOfHoles = 0;
        int numberOfWells = 0;
        int numberOfWideWells = 0;
        int numberOfCurrentLine = 0;

        double priority;

        for (int i = newGlass.length - 1; i > 3; i--) {

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

                // проверка больших колодцы

//                if (j > 0 && j < (newGlass[i].length - 2)
//                        && newGlass[i][j] == 0
//                        && newGlass[i - 1][j] == 0
//                        && newGlass[i][j - 1] == 1
//                        && newGlass[i][j + 1] == 0
//                        && newGlass[i][j + 2] == 1
//                )
//                    numberOfWideWells++;
//
//                if (j == 0 && newGlass[i][j] == 0
//                        && newGlass[i - 1][j] == 0
//                        && newGlass[i][j + 1] == 0
//                        && newGlass[i][j + 2] == 1
//                )
//                    numberOfWideWells++;
//
//                if (j == (newGlass[i].length - 1) && newGlass[i][j] == 0
//                        && newGlass[i - 1][j] == 0
//                        && newGlass[i][j - 1] == 0
//                        && newGlass[i][j - 2] == 1
//                )
//                    numberOfWideWells++;

                // конец проверки

                numberOfCurrentLine = i;
            }

            if (Collections.min(lineChecker) == 1) {
                fullLines++;
            }

        }
        if (fullLines != 0) {
            priority = (LINES_MULTIPLIER / fullLines) *
                    (maxHeight * HEIGHT_MULTIPLIER +
                            numberOfHoles * HOLES_MULTIPLIER +
                            numberOfWells * WELLS_MULTIPLIER +
//                            numberOfWideWells * WIDE_WELLS_MULTIPLIER);
                            numberOfWideWells * WIDE_WELLS_MULTIPLIER +
                            LINE_NUMBER_MULTIPLIER/numberOfCurrentLine);
        } else {
            priority = maxHeight * HEIGHT_MULTIPLIER +
                    numberOfHoles * HOLES_MULTIPLIER +
                    numberOfWells * WELLS_MULTIPLIER +
//                    numberOfWideWells * WIDE_WELLS_MULTIPLIER;
                    numberOfWideWells * WIDE_WELLS_MULTIPLIER +
                    LINE_NUMBER_MULTIPLIER/numberOfCurrentLine;
        }

        System.out.println("maxHeight = " + maxHeight + "\n" + "numberOfHoles = " + numberOfHoles + "\n" +
                        "numberOfWells = " + numberOfWells + "\n" + "numberOfCurrentLine = " + numberOfCurrentLine);

        System.out.println(priority);

        return priority;
    }
}
