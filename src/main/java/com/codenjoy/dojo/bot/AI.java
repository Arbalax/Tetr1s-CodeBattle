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

import com.codenjoy.dojo.services.calcutalor.Calculator;
import com.codenjoy.dojo.services.calcutalor.CalculatorFactory;
import com.codenjoy.dojo.tetris.model.*;

import java.util.List;

public class AI {
    private final Glass glass;
    private final Elements currentFigure;
    private final CalculatorFactory factory;

    public AI(Glass glass, Elements currentFigure) {
        this.glass = glass;
        this.currentFigure = currentFigure;
        this.factory = new CalculatorFactory();
    }

    private int[][] glassToArray(Glass glass) {

        int[][] array = new int[18][18];

        List<Layer> layers = glass.getLayers();
        for (Layer layer : layers) {
            List<Cell> cells = layer.getCells();
            for (Cell cell : cells) {
                int layerNumber = layer.getLayerNumber();
                int x = cell.getX();
                if (cell.isFilled()) {
                    array[layerNumber][x] = 1;
                } else {
                    array[layerNumber][x] = 0;
                }
            }
        }
        return array;
    }

    public Result getResult() {
        int[][] ints = glassToArray(glass);
        char figureChar = currentFigure.ch();
        Calculator calculator = factory.create(figureChar);

        return calculator.calculate(ints);
    }
}