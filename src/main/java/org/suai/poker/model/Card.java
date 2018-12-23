package org.suai.poker.model;

public enum Card {
	E0(-1, -1, -1, "ec"),

	C2(0, 0, 2, "c2"),
	C3(1, 0, 3, "c3"),
	C4(2, 0, 4, "c4"),
	C5(3, 0, 5, "c5"),
	C6(4, 0, 6,	"c6"),
	C7(5, 0, 7, "c7"),
	C8(6, 0, 8, "c8"),
	C9(7, 0, 9, "c9"),
	C10(8, 0, 10, "c10"),
	CJ(9, 0, 11, "cj"),
	CQ(10, 0, 12, "cq"),
	CK(11, 0, 13, "ck"),
	CA(12, 0, 14, "c1"),

	D2(13, 1, 2, "d2"),
	D3(14, 1, 3, "d3"),
	D4(15, 1, 4, "d4"),
	D5(16, 1, 5, "d5"),
	D6(17, 1, 6, "d6"),
	D7(18, 1, 7, "d7"),
	D8(19, 1, 8, "d8"),
	D9(20, 1, 9, "d9"),
	D10(21, 1, 10, "d10"),
	DJ(22, 1, 11, "dj"),
	DQ(23, 1, 12, "dq"),
	DK(24, 1, 13, "dk"),
	DA(25, 1, 14, "d1"),

	S2(26, 2, 2, "s2"),
	S3(27, 2, 3, "s3"),
	S4(28, 2, 4, "s4"),
	S5(29, 2, 5, "s5"),
	S6(30, 2, 6, "s6"),
	S7(31, 2, 7, "s7"),
	S8(32, 2, 8, "s8"),
	S9(33, 2, 9, "s9"),
	S10(34, 2, 10, "s10"),
	SJ(35, 2, 11, "sj"),
	SQ(36, 2, 12, "sq"),
	SK(37, 2, 13, "sk"),
	SA(38, 2, 14, "s1"),

	H2(39, 3, 2, "h2"),
	H3(40, 3, 3, "h3"),
	H4(41, 3, 4, "h4"),
	H5(42, 3, 5, "h5"),
	H6(43, 3, 6, "h6"),
	H7(44, 3, 7, "h7"),
	H8(45, 3, 8, "h8"),
	H9(46, 3, 9, "h9"),
	H10(47, 3, 10, "h10"),
	HJ(48, 3, 11, "hj"),
	HQ(49, 3, 12, "hq"),
	HK(50, 3, 13, "hk"),
	HA(51, 3, 14, "h1");
	
	private Integer id;
	private Integer suit;
	private Integer value;
	private String imagePath;

	private Card(Integer id, Integer suit, Integer value,
			String imagePath) {
		this.id = id;
		this.suit = suit;
		this.value = value;
		this.imagePath = imagePath;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSuit() {
		return suit;
	}

	public void setSuit(Integer suit) {
		this.suit = suit;
	}

	public Integer getValue() { return value; }

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName(){
		String suitString = "";
		String valueString = "";
		if(suit == -1 || value == -1){
			return "Error";
		}
		switch(suit){
		case 0:
			suitString = "Clubs";
			break;
		case 1:
			suitString = "Diamonds";
			break;
		case 2:
			suitString = "Spades";
			break;
		case 3:
			suitString = "Hearts";
			break;
		}
		switch(value){
		default:
			valueString = value.toString();
			break;
		case 11:
			valueString = "Jack";
			break;
		case 12:
			valueString = "Queen";
			break;
		case 13:
			valueString = "King";
			break;
		case 14:
			valueString = "Ace";
			break;
		}
		return valueString + " of " + suitString;
	}

	public String toString() {
		return getName();
	}

	public static Card getCard(Integer num) {
		for (Card cards : Card.values()) {
			if (cards.getId() == num) {
				return cards;
			}
		}
		return null;
	}
}
