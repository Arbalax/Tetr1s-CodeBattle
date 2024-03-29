package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.bot.AI;
import com.codenjoy.dojo.client.AbstractJsonSolver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Command;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.model.*;

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

    }

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String getAnswer(Board board) {
        Elements currentFigureType = board.getCurrentFigureType();
        Glass glass = parse(board.getData());
        ai = new AI(glass, currentFigureType);

        List<Layer> layers = glass.getLayers();
        for (Layer layer : layers) {
            List<Cell> cells = layer.getCells();
            for (Cell cell : cells) {
                if (cell.isFilled()) {
                    System.out.print("O\t");
                } else {
                    System.out.print(".\t");
                }
            }
            System.out.println();
        }
        Result result = ai.getResult();
        List<Command> commands = getCommands(result, board);

        List<String> stringList = commands.stream().map(Command::toString).collect(toList());
        return String.join(", ", stringList);
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // скопируйте сюда адрес из браузера, на который перейдете после регистрации/логина
                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/mrh0ha95mk48npzxtchb?code=3236589108730196731&gameName=tetris",
//                "http://codebattle2020.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/b777ine3y0mz8ntv8hru?code=5352453488097993399",
                new YourSolver(),
                new Board());
    }
}
