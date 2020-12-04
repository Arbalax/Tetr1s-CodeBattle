package com.codenjoy.dojo.services;

import com.codenjoy.dojo.tetris.model.Cell;
import com.codenjoy.dojo.tetris.model.Glass;
import com.codenjoy.dojo.tetris.model.Layer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardParser {
    public static Glass parse(String data) {
        List<Layer> layers = new ArrayList<>();
        int layerNumber = 1;
        for (String layerData : data.split("(?<=\\G.{18})")) {
            Layer layer = new Layer(layerNumber);
            int x = 0;
            List<Cell> cells = new LinkedList<>();
            for (String cellData : layerData.split("(?<=\\G.)")) {
                Cell cell;
                if (cellData.equals(".")) {
                    cell = new Cell(false, x);
                } else {
                    cell = new Cell(true, x);
                }
                cells.add(cell);
                x++;
            }

            layer.setCells(cells);
            layers.add(layer);
            layerNumber++;
        }
        return new Glass(layers);
    }
}
