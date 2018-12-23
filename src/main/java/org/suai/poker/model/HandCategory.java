package org.suai.poker.model;

public enum HandCategory {
	UNKNOWN(0, "Unknown"),
	HIGH_CARD(1, "High Card"),
	PAIR(2, "Pair"),
	TWO_PAIR(3, "Two Pair"),
	THREE_OF_A_KIND(4, "Three of a Kind"),
	STRAIGHT(5,	"Straight"),
	FLUSH(6, "Flush"),
	FULL_HOUSE(7, "Full House"),
	FOUR_OF_A_KIND(8, "Four of a Kind"),
	STRAIGHT_FLUSH(9, "Straight Flush"),
	ROYAL_FLUSH(10, "Royal Flush");

	private Integer id;
	private String name;
	
	private HandCategory(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
