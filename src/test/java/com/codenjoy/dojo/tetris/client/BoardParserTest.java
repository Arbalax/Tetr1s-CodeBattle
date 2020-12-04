package com.codenjoy.dojo.tetris.client;

import com.codenjoy.dojo.tetris.model.Glass;
import org.junit.Test;

import static com.codenjoy.dojo.services.BoardParser.parse;

public class BoardParserTest {

    String board = "............................................OO................OO....................................................................................................................................................................................................................................................................";

    @Test
    public void parseTest() {
        Glass glass = parse(board);

        System.out.println("Пушка!");
    }
}
