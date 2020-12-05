package com.codenjoy.dojo.services;

import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.model.Result;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Command.*;

public class AIResultHandler {
    public static List<Command> getCommands(Result result, Board board) {
        List<Command> commands = new LinkedList<>();
        int x = board.getCurrentFigurePoint().getX();

        int resultX = result.getX();
        if (resultX < x) {
            for (int i = 0; i < Math.abs(resultX - x); i++) {
                commands.add(LEFT);
            }
        }
        if (resultX > x) {
            for (int i = 0; i < Math.abs(resultX - x); i++) {
                commands.add(RIGHT);
            }
        }

        int rotation = result.getRotation();

        if (rotation == 1) {
            commands.add(ROTATE_CLOCKWISE_90);
        }
        if (rotation == 2) {
            commands.add(ROTATE_CLOCKWISE_180);
        }
        if (rotation == 3) {
            commands.add(ROTATE_CLOCKWISE_270);
        }

        commands.add(DOWN);

        return commands;
    }
}
