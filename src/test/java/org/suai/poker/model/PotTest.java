package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PotTest {

    private Pot test = new Pot();
    @Test
    void addPlayer() {
        test.addPlayer(new Player("Vasya", 20000, new Table(), false));
        assertEquals(1, test.getPlayerList().size());
    }

    @Test
    void cleanUpWinnerList() {
        List<Player> list = new ArrayList<>();
        list.add(new Player("Pavel", 100500, new Table(), false));
        test.setWinnerList(list);
        int size = test.getWinnerList().size() - 1;
        test.cleanUpWinnerList();
        assertEquals(size, test.getWinnerList().size());
    }

    @Test
    void addWinner() {
        test.addWinner(new Player("Petrovich", 9000, new Table(), false));
        assertEquals(1, test.getWinnerList().size());
    }
}