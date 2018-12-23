package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table test = new Table();
    @Test
    void addPlayer() {
        test.addPlayer(new Player("Vasya", 9000, test, false));
        assertEquals(1, test.getPlayerList().size());
    }

    @Test
    void getPlayerSize() {
        test.addPlayer(new Player("Vasya", 9000, test, false));
        assertEquals(new Integer(1), test.getPlayerSize());
    }

    @Test
    void getPlayerHandSize() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        Player p = new Player("Vasya", 100500, test, false);
        Hand res = new Hand();
        res.setHand(list);
        p.setHand(res);
        test.addPlayer(p);
        assertEquals(new Integer(7), test.getPlayerHandSize(0));
    }

    @Test
    void getPlayerHandCard() {
    }

    @Test
    void getPlayerHand() {
    }

    @Test
    void getPlayerName() {
    }

    @Test
    void getPlayerBalance() {
    }

    @Test
    void getPlayerOnTurn() {
    }

    @Test
    void getPlayer() {
    }

    @Test
    void getTableHandSize() {
    }

    @Test
    void getTableHandCard() {
    }

    @Test
    void payToPot() {
    }
}