package org.suai.poker.model;

import java.io.Serializable;
import java.util.*;

public class Hand implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Card> hand;
	private Integer maxValue;
	private Integer maxValue2;
	private HandCategory id;

	public Hand() {
		this.hand = new ArrayList<>();
		this.maxValue = 0;
		this.maxValue2 = 0;
		this.id = HandCategory.UNKNOWN;
	}

	public Hand(List<Card> hand) {
		this.hand = hand;
		this.maxValue = 0;
		this.maxValue2 = 0;
		this.id = HandCategory.UNKNOWN;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Hand)) {
			return false;
		}
		Hand obj = (Hand)o;
		if (hand.equals(obj.hand) && maxValue.equals(obj.maxValue) && maxValue2.equals(obj.maxValue2)) {
			return true;
		}
		return false;
	}

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(List<Card> hand) {
		this.hand = hand;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getMaxValue2() {
		return maxValue2;
	}

	public void setMaxValue2(Integer maxValue2) {
		this.maxValue2 = maxValue2;
	}

	public HandCategory getId() {
		return id;
	}

	public void setId(HandCategory id) {
		this.id = id;
	}

	public static Hand calculateHand(Hand hand) {
		Hand bestHand = new Hand();
		bestHand.setId(HandCategory.HIGH_CARD);
		for (Card card : hand.getHand()) {
			if (card.getValue() > bestHand.getMaxValue()) {
				bestHand.setMaxValue(card.getValue());
			}
		}
		for (Card card : hand.getHand()) {
			if (card.getValue() > bestHand.getMaxValue2()
					&& card.getValue() != bestHand.getMaxValue()) {
				bestHand.setMaxValue2(card.getValue());
			}
		}

		if (!hand.getPair().getHand().isEmpty()) {
			bestHand = hand.getPair();
			bestHand.setId(HandCategory.PAIR);
		}

		if (!hand.getTwoPair().getHand().isEmpty()) {
			bestHand = hand.getTwoPair();
			bestHand.setId(HandCategory.TWO_PAIR);
		}

		if (!hand.getThreeOfAKind().getHand().isEmpty()) {
			bestHand = hand.getThreeOfAKind();
			bestHand.setId(HandCategory.THREE_OF_A_KIND);
		}

		if (!hand.getStraight().getHand().isEmpty()) {
			bestHand = hand.getStraight();
			bestHand.setId(HandCategory.STRAIGHT);
		}

		if (!hand.getFlush().getHand().isEmpty()) {
			bestHand = hand.getFlush();
			bestHand.setId(HandCategory.FLUSH);
		}

		if (!hand.getFullHouse().getHand().isEmpty()) {
			bestHand = hand.getFullHouse();
			bestHand.setId(HandCategory.FULL_HOUSE);
		}

		if (!hand.getFourOfAKind().getHand().isEmpty()) {
			bestHand = hand.getFourOfAKind();
			bestHand.setId(HandCategory.FOUR_OF_A_KIND);
		}

		if (!hand.getStraightFlush().getHand().isEmpty()) {
			bestHand = hand.getStraightFlush();
			bestHand.setId(HandCategory.STRAIGHT_FLUSH);
		}

		if (!hand.getRoyalFlush().getHand().isEmpty()) {
			bestHand = hand.getRoyalFlush();
			bestHand.setId(HandCategory.ROYAL_FLUSH);
		}
		return bestHand;
	}

	public Hand getPairSet() {
		Set<Card> pairSet = new HashSet<>();
		List<Card> pair = new ArrayList<>();

		for (Integer i = 0; i < hand.size(); i++) {
			for (Integer j = 1; j < hand.size(); j++) {
				if ((hand.get(i).getValue() == hand.get(j).getValue())
						&& (hand.get(i).getSuit() != hand.get(j).getSuit())) {
					pairSet.add(hand.get(i));
					pairSet.add(hand.get(j));
				}
			}
		}
		pair.addAll(pairSet);
		if (pair.size() >= 6) {
			Integer max = 0, max2 = 0, counter = 0, counter2 = 0, counter3 = 0;
			Boolean removeLow = false, removeMedium = false, removeHigh = false;
			for (Card card : pair) {
				if (card.getValue() > max) {
					max = card.getValue();
				}
			}
			for (Card card : pair) {
				if (card.getValue() > max2 && card.getValue() != max) {
					max2 = card.getValue();
				}
			}
			for (Card card : pair) {
				if (card.getValue() == max) {
					counter++;
				} else if (card.getValue() == max2) {
					counter2++;
				} else {
					counter3++;
				}
			}
			if ((counter == counter2 && counter2 == counter3)
					|| (counter > counter3 || counter2 > counter3)
					|| counter == 4 || counter2 == 4) {
				removeLow = true;
			}
			if ((counter3 > counter2) || counter == 4 || counter3 == 4) {
				removeMedium = true;
			}
			if (counter2 == 4 || counter3 == 4) {
				removeHigh = true;
			}
			List<Card> toRemove = new ArrayList<>();
			toRemove.addAll(pair);
			for (Card card : toRemove) {
				if ((removeLow && (card.getValue() != max && card.getValue() != max2))
						|| (removeMedium && card.getValue() == max2)
						|| (removeHigh && card.getValue() == max)) {
					pair.remove(card);
				}
			}
		}
		return new Hand(pair);
	}

	public Hand getPair() {
		Hand check = getPairSet();
		if (check.getHand().size() == 2) {
			check.setMaxValue(check.getHand().get(0).getValue());
			return check;
		} else {
			return new Hand();
		}
	}

	public Hand getThreeOfAKind() {
		Hand check = getPairSet();
		if (check.getHand().size() == 3) {
			check.setMaxValue(check.getHand().get(0).getValue());
			return check;
		} else {
			return new Hand();
		}
	}

	public Hand getFourOfAKind() {
		Hand check = getPairSet();
		if (check.getHand().size() == 4
				&& check.getTwoPair().getHand().isEmpty()) {
			check.setMaxValue(check.getHand().get(0).getValue());
			return check;
		} else {
			return new Hand();
		}
	}

	public Hand getFullHouse() {
		Hand check = getPairSet();
		Integer counter = 0;
		if (check.getHand().size() == 5) {
			for (Card card : check.getHand()) {
				if (card.getValue() > check.getMaxValue()) {
					check.setMaxValue(card.getValue());
				}
			}
			for (Card card : check.getHand()) {
				if (card.getValue() > check.getMaxValue2()
						&& card.getValue() != check.getMaxValue()) {
					check.setMaxValue2(card.getValue());
				}
			}
			for (Card card : check.getHand()) {
				if (card.getValue() == check.getMaxValue()) {
					counter++;
				}
			}
			if (counter != 3) {
				Integer max = check.getMaxValue();
				check.setMaxValue(check.getMaxValue2());
				check.setMaxValue2(max);
			}
			return check;
		}

		else {
			return new Hand();
		}
	}

	public Hand getTwoPair() {
		Hand check = getPairSet();
		Boolean twoPair = false;
		if (check.getHand().size() == 4) {
			for (Integer i = 0; i < check.getHand().size() - 1; i++) {
				if (check.getHand().get(i).getValue() != check.getHand()
						.get(i + 1).getValue()) {
					for (Card card : check.getHand()) {
						if (card.getValue() > check.getMaxValue()) {
							check.setMaxValue(card.getValue());
						}
					}
					for (Card card : check.getHand()) {
						if (card.getValue() > check.getMaxValue2()
								&& card.getValue() != check.getMaxValue()) {
							check.setMaxValue2(card.getValue());
						}
					}
					twoPair = true;
					break;
				}
			}
			if (!twoPair) {
				return new Hand();
			} else {
				return check;
			}
		}
		else {
			return new Hand();
		}
	}

	public Hand getFlush() {
		List<Card> check = new ArrayList<>();
		Integer maxSuit = getHighestSuitCount();
		if (maxSuit == -1) {
			return new Hand();
		}
		for (Card card : hand) {
			if (maxSuit == card.getSuit()) {
				check.add(card);
			}
		}
		Collections.sort(check);
		while (check.size() > 5) {
			check.remove(0);
		}
		Hand checkHand = new Hand(check);
		checkHand.setMaxValue(check.get(4).getValue());
		checkHand.setMaxValue2(check.get(3).getValue());
		return checkHand;
	}

	public Integer getHighestSuitCount() {
		Integer counter0 = 0, counter1 = 0, counter2 = 0, counter3 = 0, maxCount = 4, maxSuit = null;
		List<Integer> counterList = new ArrayList<>();
		for (Integer i = 0; i < 4; i++) {
			counterList.add(0);
		}
		for (Card card : hand) {
			if (card.getSuit() == 0) {
				counterList.set(0, ++counter0);
			}
			if (card.getSuit() == 1) {
				counterList.set(1, ++counter1);
			}
			if (card.getSuit() == 2) {
				counterList.set(2, ++counter2);
			}
			if (card.getSuit() == 3) {
				counterList.set(3, ++counter3);
			}
		}
		for (Integer i = 0; i < counterList.size(); i++) {
			if (counterList.get(i) > maxCount) {
				maxCount = counterList.get(i);
				maxSuit = i;
			}
		}
		if (maxCount < 5) {
			return -1;
		} else {
			return maxSuit;
		}
	}

	public Hand getStraight() {
		List<Card> check = new ArrayList<>();
		Set<Integer> valueSet = new HashSet<>();
		List<Integer> values = new ArrayList<>();
		Integer counter = 0, pos = 0, maxCounter = 0;
		for (Card card : hand) {
			valueSet.add(card.getValue());
		}
		values.addAll(valueSet);
		Collections.sort(values);
		for (Integer number : values) {
			for (Integer i = 0; i < hand.size(); i++) {
				if (number == hand.get(i).getValue()) {
					check.add(hand.get(i));
				}
			}
		}
		for (Integer g = 0; g < values.size() - 4; g++) {
			counter = 0;
			pos = 0;
			for (Integer i = g; i < values.size() - 1; i++) {
				if ((values.get(i) + 1) == (values.get(i + 1))) {
					counter++;
				} else {
					if (counter > maxCounter && g > pos) {
						maxCounter = counter;
						pos = g;
					}
					break;
				}
				maxCounter = counter;
				pos = g;
			}
		}
		if (maxCounter < 4) {
			return new Hand();
		}
		for (Integer i = (pos + 5); i < hand.size(); i++) {
			check.remove(pos + 5);
		}
		for (Integer i = 0; i < pos; i++) {
			check.remove(0);
		}

		Hand checkHand = new Hand(check);
		checkHand.setMaxValue(check.get(4).getValue());
		checkHand.setMaxValue2(check.get(3).getValue());
		return checkHand;
	}

	public Hand getStraightFlush() {
		if (getFlush().getStraight().getHand().isEmpty()) {
			return new Hand();
		} else {
			return getFlush().getStraight();
		}
	}

	public Hand getRoyalFlush() {
		Integer pos = 0;
		if (getFlush().getHand().isEmpty() || hand.size() < 5) {
			return new Hand();
		}
		Hand check = getFlush();
		for (Integer i = 10; i < check.getHand().size() + 10; i++) {
			if (check.getHand().get(pos).getValue() != i) {
				return new Hand();
			}
			pos++;
		}
		return check;
	}

	public void draw(Deck deck, Integer amount) {
		for (Integer i = 0; i < amount; i++) {
			hand.add(deck.draw());
		}
	}

	public Integer getSize() {
		return hand.size();
	}

	public Card getCard(Integer cardPos) {
		return hand.get(cardPos);
	}

	public String getCardImagePath(Integer cardPos) {
		return hand.get(cardPos).getImagePath();
	}

	public String toString() {
		String list = "";
		if (hand.isEmpty()) {
			return "Empty Hand";
		}
		for (Card card : hand) {
			list = list + card.getName() + " ";
		}
		return list;
	}

	public static Hand CombineHands(Hand hand1, Hand hand2) {
		List<Card> list = new ArrayList<>();
		list.addAll(hand1.getHand());
		list.addAll(hand2.getHand());
		Hand hand = new Hand(list);
		return hand;
	}
}
