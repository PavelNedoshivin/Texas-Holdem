package org.suai.poker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pot implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer amount;
	private List<Player> playerList;
	private List<Player> winnerList;

	public Pot(){
		setAmount(0);
		playerList = new ArrayList<>();
		winnerList = new ArrayList<>();
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}

	public List<Player> getWinnerList() {
		return winnerList;
	}

	public void setWinnerList(List<Player> winnerList) {
		this.winnerList = winnerList;
	}

	public void addPlayer(Player player){
		playerList.add(player);
	}

	public void cleanUpWinnerList(){
		winnerList.removeAll(winnerList);
	}
	
	public void addWinner(Player player){
		winnerList.add(player);
	}
}
