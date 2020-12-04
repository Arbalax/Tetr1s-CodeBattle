package com.codenjoy.dojo.tetris.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {
    private boolean isFilled;
    private int x;

    public Cell(boolean isFilled, int x) {
        this.isFilled = isFilled;
        this.x = x;
    }
}
