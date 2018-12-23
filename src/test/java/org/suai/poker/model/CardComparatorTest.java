package org.suai.poker.model;

import static org.junit.jupiter.api.Assertions.*;

class CardComparatorTest {

    private CardComparator test = new CardComparator();
    @org.junit.jupiter.api.Test
    void compare() {
        Card o1 = Card.S6;
        Card o2 = Card.C6;
        assertEquals(-1, test.compare(o1, o2));
    }
}