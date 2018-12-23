package org.suai.poker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Card> deck;

	public Deck() {
		this.deck = generate();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Deck)) {
			return false;
		}
		Deck obj = (Deck)o;
		if (deck.equals(obj.deck)) {
			return true;
		}
		return false;
	}

	private List<Card> generate() {
		List<Card> deck = new ArrayList<>();
		for (int i = 0; i < 52; i++) {
			deck.add(Card.getCard(i));
		}
		Collections.shuffle(deck);
		return deck;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}

	public Card getCard(Integer pos) {
		try {
			return deck.get(pos);
		} catch (Exception e) {
			return Card.E0;
		}
	}

	public Card draw() {
		Card card = getCard(0);
		if (!deck.isEmpty()) {
			deck.remove(0);
		}
		return card;
	}

	public String toString() {
		String list = deck.size() + " cards in deck: ";
		for (Card card : deck) {
			list += card.getName() + " ";
		}
		return list;
	}
}