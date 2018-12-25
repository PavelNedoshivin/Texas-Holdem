package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player test = new Player("Pavel", 100500, new Table(), false);
    @Test
    void payBlind() {
        test.payBlind(500);
        assertEquals(new Integer(500), test.getCurrentBet());
    }

    @Test
    void fold() {
        test.fold();
        assertEquals(PlayerStatus.PLAYER_FOLD, test.getStatus());
    }

    @Test
    void check() {
        test.check();
        assertEquals(PlayerStatus.PLAYER_CHECK, test.getStatus());
    }

    @Test
    void call() {
        Table table = new Table();
        table.setCurrentBet(500);
        test.setTable(table);
        test.payBlind(200);
        test.call();
        assertEquals(new Integer(500), test.getCurrentBet());
    }

    @Test
    void bet() {
        test.bet(1000);
        assertEquals(new Integer(1000), test.getCurrentBet());
    }

    @Test
    void raise() {
        test.raise(1500);
        assertEquals(PlayerStatus.PLAYER_RAISE, test.getStatus());
    }

    @Test
    void allIn() {
        test.allIn();
        assertEquals(test.getBalance(), test.getCurrentBet());
    }

    @Test
    void pay() {
        test.allIn();
        test.pay();
        assertEquals(new Integer(0), test.getBalance());
    }

    @Test
    void calculateBestHand() {
        Hand res = new Hand();
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        res.setHand(list);
        res = res.getStraightFlush();
        test.calculateBestHand(res);
        assertEquals(new Integer(14), test.getBestHand().getMaxValue());
    }

    @Test
    void isNotPlaying() {
        test.fold();
        assertEquals(true, test.isNotPlaying());
    }

    @Test
    void isTurnAllowed() {
        test.fold();
        assertEquals(false, test.isTurnAllowed());
    }

    @Test
    void isAllIn() {
        test.allIn();
        assertEquals(true, test.isAllIn());
    }
}