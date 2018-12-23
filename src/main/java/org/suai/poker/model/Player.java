package org.suai.poker.model;

import java.io.Serializable;
import java.util.Collections;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer balance;
	private Table table;
	private Hand hand;
	private PlayerStatus status;
	private Boolean dealer;
	private Boolean computer;
	private Integer currentBet;
	private Hand bestHand;
	private Hand kicker;

	public Player(String name, Integer balance, Table table, Boolean computer) {
		this.name = name;
		this.balance = balance;
		this.table = table;
		this.hand = new Hand();
		this.status = PlayerStatus.PLAYER_NORMAL;
		this.dealer = false;
		this.computer = computer;
		this.currentBet = 0;
		this.bestHand = new Hand();
		this.kicker = new Hand();
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Player)) {
			return false;
		}
		Player obj = (Player)o;
		if (name.equals(obj.name) && balance.equals(obj.balance) && dealer.equals(obj.dealer) && computer.equals(obj.computer) &&
				currentBet.equals(obj.currentBet)) {
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public Boolean getDealer() {
		return dealer;
	}

	public void setDealer(Boolean dealer) {
		this.dealer = dealer;
	}

	public Boolean getComputer() {
		return computer;
	}

	public void setComputer(Boolean computer) {
		this.computer = computer;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public Integer getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(Integer currentBet) {
		this.currentBet = currentBet;
	}

	public Hand getBestHand() {
		return bestHand;
	}

	public void setBestHand(Hand bestHand) {
		this.bestHand = bestHand;
	}

	public Hand getKicker() {
		return kicker;
	}

	public void setKicker(Hand kicker) {
		this.kicker = kicker;
	}

	public void payBlind(Integer amount) {
		currentBet = amount;
	}

	public void fold() {
		status = PlayerStatus.PLAYER_FOLD;
		table.nextPlayerTurn();
	}

	public void check() {
		status = PlayerStatus.PLAYER_CHECK;
		table.nextPlayerTurn();
	}

	public void call() {
		currentBet = table.getCurrentBet();
		status = PlayerStatus.PLAYER_CALL;
		table.nextPlayerTurn();
	}

	public void bet(Integer amount) {
		currentBet = amount;
		status = PlayerStatus.PLAYER_BET;
		table.setCurrentBet(amount);
		table.nextPlayerTurn();
	}

	public void raise(Integer amount) {
		currentBet = amount;
		status = PlayerStatus.PLAYER_RAISE;
		table.setCurrentBet(amount);
		table.nextPlayerTurn();
	}

	public void allIn() {
		currentBet = balance;
		status = PlayerStatus.PLAYER_ALLIN;
		if (balance < table.getCurrentBet()) {

		} else {
			table.setCurrentBet(balance);
		}
		table.nextPlayerTurn();
	}

	public void pay() {
		balance -= currentBet;
	}

	public void calculateBestHand(Hand tableHand) {
		kicker = new Hand();
		bestHand = Hand.calculateHand(Hand.CombineHands(hand, tableHand));
		Hand check = new Hand();
		check.getHand().addAll(Hand.CombineHands(hand, tableHand).getHand());
		for (Card card : bestHand.getHand()) {
			if (check.getHand().contains(card)) {
				check.getHand().remove(card);
			}
		}
		Collections.sort(bestHand.getHand(), new CardComparator());
		Collections.sort(check.getHand(), new CardComparator());
		for (Card card : check.getHand()) {
			if (bestHand.getHand().size() < 5) {
				bestHand.getHand().add(card);
				kicker.getHand().add(card);
			} else
				break;
		}

	}

	public Boolean isNotPlaying() {
			return isFold() || isBustedOut();
	}
	
	public Boolean isTurnAllowed() {
		return !(isFold() || isBustedOut() || isAllIn());
	}
	
	public Boolean isAllIn(){
		return status == PlayerStatus.PLAYER_ALLIN;		
	}
	
	public Boolean isFold(){
		return status == PlayerStatus.PLAYER_FOLD;
	}

	public Boolean isBustedOut(){
		return status == PlayerStatus.PLAYER_BUSTED_OUT;
	}
	
	public Boolean isAllInLowerBet(){
		return isAllIn() && currentBet < table.getCurrentBet();
	}

	/**
	 * AI Action. For now, random.
	 * TODO: Add some thinking like bluffing, checking hand and so on.
	 */
	public void think() {
		switch ((int) (Math.random() * 3)) {
		case 0:
			if (table.getCurrentBet() > 0) {
				fold();
			} else {
				check();
			}
			break;
		case 1:
			if (table.getCurrentBet() > 0) {
				Integer raiseValue = (int) (Math.random() * 5) * 100 + table.getCurrentBet();
				if (table.getCurrentBet() + raiseValue >= balance) {
					allIn();
				} else {
					raise(table.getCurrentBet() + raiseValue);
				}
			} else {
				bet((int) (Math.random() * 5) * 100 + table.getBigBlind());
			}
			break;
		case 2:
			if(table.getCurrentBet() >= balance){
				allIn();
			}
			else if (table.getCurrentBet() > 0) {
				call();
			} else {
				check();
			}
			break;
		}
	}

}
