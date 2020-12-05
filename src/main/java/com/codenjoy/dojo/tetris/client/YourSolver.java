package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.bot.AI;
import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Command;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.Elements;
import com.codenjoy.dojo.tetris.model.Glass;
import com.codenjoy.dojo.tetris.model.Result;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.services.AIResultHandler.getCommands;
import static com.codenjoy.dojo.services.BoardParser.parse;
import static java.util.stream.Collectors.toList;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

/**
 * User: your name
 * Это твой алгоритм AI для игры. Реализуй его на свое усмотрение.
 * Обрати внимание на {@see YourSolverTest} - там приготовлен тестовый
 * фреймворк для тебя.
 */
public class YourSolver extends AbstractJsonSolver<Board> {
    private Dice dice;
    private AI ai;

    public YourSolver() {
        this.ai = new AI();
    }

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
//        List<Command> answerList = getAnswerList(board);
        Elements currentFigureType = board.getCurrentFigureType();
        Glass glass = parse(board.getData());
        int[][] ints = ai.glassToArray(glass);
        Result result = null;

        if (currentFigureType.ch() == 'I') {
            result = ai.calcI(ints);
        }
        if (currentFigureType.ch() == 'J') {
            result = ai.calcJ(ints);
        }
        if (currentFigureType.ch() == 'L') {
            result = ai.calcL(ints);
        }
        if (currentFigureType.ch() == 'O') {
            result = ai.calcO(ints);
        }
        if (currentFigureType.ch() == 'S') {
            result = ai.calcS(ints);
        }
        if (currentFigureType.ch() == 'T') {
            result = ai.calcT(ints);
        }
        if (currentFigureType.ch() == 'Z') {
            result = ai.calcZ(ints);
        }

        List<Command> commands = getCommands(result, board);


        List<String> stringList = commands.stream().map(Command::toString).collect(toList());
        return String.join(", ", stringList);
    }

    private List<Command> getAnswerList(Board board) {
        System.out.println(board.getGlass().getAt(board.getCurrentFigurePoint()));
        List<Command> result = new ArrayList<>();
        result.add(Command.LEFT);
        result.add(Command.random(dice));
        result.add(Command.ROTATE_CLOCKWISE_180);

        return result;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/b777ine3y0mz8ntv8hru?code=5352453488097993399",
                new YourSolver(),
                new Board());
    }

}
