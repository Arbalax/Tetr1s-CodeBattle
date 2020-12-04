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

        if (result.getX() < x) {
            for (int i = 0; i < Math.abs(result.getX() - x); i++) {
                commands.add(LEFT);
            }
        }
        if (result.getX() > x) {
            for (int i = 0; i < Math.abs(result.getX() - x); i++) {
                commands.add(RIGHT);
            }
        }

        if (result.getRotation() == 1) {
            commands.add(ROTATE_CLOCKWISE_90);
        }
        if (result.getRotation() == 2) {
            commands.add(ROTATE_CLOCKWISE_180);
        }
        if (result.getRotation() == 3) {
            commands.add(ROTATE_CLOCKWISE_270);
        }

        return commands;
    }

}
