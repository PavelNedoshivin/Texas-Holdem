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
        Card result = test.getPlayerHandCard(0, 0);
        assertEquals(Card.C6, result);
    }

    @Test
    void getPlayerHand() {
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
        assertEquals(res, test.getPlayerHand(0));
    }

    @Test
    void getPlayerName() {
        Player p = new Player("Vasya", 100500, test, false);
        test.addPlayer(p);
        assertEquals("Vasya", test.getPlayerName(0));
    }

    @Test
    void getPlayerBalance() {
        Player p = new Player("Vasya", 100500, test, false);
        test.addPlayer(p);
        assertEquals(new Integer(100500), test.getPlayerBalance(0));
    }

    @Test
    void getPlayerOnTurn() {
        Player p = new Player("Vasya", 100500, test, false);
        test.addPlayer(p);
        assertEquals(p, test.getPlayerOnTurn());
    }

    @Test
    void getPlayer() {
        Player p = new Player("Vasya", 100500, test, false);
        test.addPlayer(p);
        assertEquals(p, test.getPlayer(0));
    }

    @Test
    void getTableHandSize() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        Hand hand = new Hand();
        hand.setHand(list);
        test.setTableHand(hand);
        assertEquals(new Integer(7), test.getTableHandSize());
    }

    @Test
    void getTableHandCard() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        Hand hand = new Hand();
        hand.setHand(list);
        test.setTableHand(hand);
        assertEquals(Card.C6, test.getTableHandCard(0));
    }

    @Test
    void payToPot() {
        test.payToPot();
        assertEquals(test.getCurrentBet(), test.getPot().get(0).getAmount());
    }
}