package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    private Hand test = new Hand();
    @Test
    void calculateHand() {
        test.setMaxValue(15);
        test.setMaxValue2(10);
        Hand test2 = new Hand();
        test2.setMaxValue(15);
        test2.setMaxValue2(12);
        Hand res = test.calculateHand(test2);
        assertEquals(HandCategory.HIGH_CARD, res.getId());
    }

    @Test
    void getPairSet() {
    }

    @Test
    void getPair() {
    }

    @Test
    void getThreeOfAKind() {
    }

    @Test
    void getFourOfAKind() {
    }

    @Test
    void getFullHouse() {
    }

    @Test
    void getTwoPair() {
    }

    @Test
    void getFlush() {
    }

    @Test
    void getHighestSuitCount() {
    }

    @Test
    void getStraight() {
    }

    @Test
    void getStraightFlush() {
    }

    @Test
    void getRoyalFlush() {
    }
}