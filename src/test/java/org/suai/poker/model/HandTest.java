package org.suai.poker.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.D6);
        list.add(Card.H6);
        list.add(Card.CA);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getPairSet();
        assertEquals(4, res.getHand().size());
    }

    @Test
    void getPair() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getPair();
        assertEquals(new Integer(6), res.getMaxValue());
    }

    @Test
    void getThreeOfAKind() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.D6);
        list.add(Card.CA);
        test.setHand(list);
        Hand res = test.getThreeOfAKind();
        assertEquals(new Integer(6), res.getMaxValue());
    }

    @Test
    void getFourOfAKind() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.D6);
        list.add(Card.H6);
        list.add(Card.CA);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getFourOfAKind();
        assertEquals(new Integer(6), res.getMaxValue());
    }

    @Test
    void getFullHouse() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.CA);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getFullHouse();
        assertEquals(new Integer(14), res.getMaxValue());
    }

    @Test
    void getTwoPair() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getTwoPair();
        assertEquals(new Integer(14), res.getMaxValue());
    }

    @Test
    void getFlush() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.C7);
        list.add(Card.C8);
        list.add(Card.CK);
        list.add(Card.CA);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getFlush();
        assertEquals(new Integer(14), res.getMaxValue());
    }

    @Test
    void getHighestSuitCount() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S6);
        list.add(Card.D6);
        list.add(Card.H6);
        list.add(Card.CA);
        list.add(Card.HA);
        list.add(Card.SA);
        test.setHand(list);
        assertEquals(new Integer(-1), test.getHighestSuitCount());
    }

    @Test
    void getStraight() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C10);
        list.add(Card.S6);
        list.add(Card.D6);
        list.add(Card.HJ);
        list.add(Card.CQ);
        list.add(Card.HK);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getStraight();
        assertEquals(new Integer(13), res.getMaxValue());
    }

    @Test
    void getStraightFlush() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getStraightFlush();
        assertEquals(new Integer(14), res.getMaxValue());
    }

    @Test
    void getRoyalFlush() {
        List<Card> list = new ArrayList<>();
        list.add(Card.C6);
        list.add(Card.S10);
        list.add(Card.D6);
        list.add(Card.SJ);
        list.add(Card.SQ);
        list.add(Card.SK);
        list.add(Card.SA);
        test.setHand(list);
        Hand res = test.getStraightFlush();
        assertEquals(5, res.getHand().size());
    }
}