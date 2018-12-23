package org.suai.poker.model;

public enum PlayerStatus {
	PLAYER_NORMAL(0, "Normal"),
	PLAYER_CHECK(1, "Check"),
	PLAYER_CALL(2,"Call"),
	PLAYER_BET(3,"Bet"),
	PLAYER_RAISE(4,"Raise"),
	PLAYER_FOLD(5, "Fold"), 
	PLAYER_ALLIN(6, "All in"),
	PLAYER_WINNER(7,"Winner"),
	PLAYER_LOST(8,"Lost"),
	PLAYER_BUSTED_OUT(9,"Busted out");

	private Integer id;
	private String name;

	private PlayerStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public String toString() {
		return name;
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
}
