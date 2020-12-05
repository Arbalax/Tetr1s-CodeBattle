package com.codenjoy.dojo.services;

import com.codenjoy.dojo.tetris.model.Cell;
import com.codenjoy.dojo.tetris.model.Glass;
import com.codenjoy.dojo.tetris.model.Layer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class BoardParser {
    public static Glass parse(String data) {
        JSONObject source = new JSONObject(data);
        JSONArray layersJson = source.getJSONArray("layers");
        String[] layersStrings = layersJson.toList().toArray(new String[0]);

        List<Layer> layers = new LinkedList<>();
        int layerNumber = 0;
        for (String layerData : layersStrings[0].split("(?<=\\G.{18})")) {
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
