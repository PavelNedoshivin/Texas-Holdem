package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck test = new Deck();
    @Test
    void shuffle() {
        List<Card> list = test.getDeck();
        Card card = list.get(0);
        test.shuffle();
        list = test.getDeck();
        assertEquals(false, card.equals(list.get(0)));
    }

    @Test
    void getCard() {
        assertEquals(Card.E0, test.getCard(-10));
    }

    @Test
    void draw() {
        List<Card> list = test.getDeck();
        int size = list.size() - 1;
        test.draw();
        list = test.getDeck();
        assertEquals(size, list.size());
    }
}