package com.codenjoy.dojo.tetris.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Layer {
    private List<Cell> cells;
    private int layerNumber;

    public Layer(int layerNumber) {
        this.layerNumber = layerNumber;
    }
}
