package org.suai.poker.model;

import org.junit.jupiter.api.Test;

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
    }

    @Test
    void pay() {
    }

    @Test
    void calculateBestHand() {
    }

    @Test
    void isNotPlaying() {
    }

    @Test
    void isTurnAllowed() {
    }

    @Test
    void isAllIn() {
    }
}