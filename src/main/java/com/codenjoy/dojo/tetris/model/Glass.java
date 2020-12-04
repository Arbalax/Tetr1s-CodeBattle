package com.codenjoy.dojo.tetris.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Glass {
    private List<Layer> layers;

    public Glass(List<Layer> layers) {
        this.layers = layers;
    }
}
