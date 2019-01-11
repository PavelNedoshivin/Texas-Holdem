package org.suai.poker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a table where players play. Table contains a deck and a hand, too.
 * In addition, current bet is considered as reference to check that all players
 * pay up specific amount. Small blind is defined, big blind is simply double of
 * small blind. Pots are not fully implemented, Dealer position is defined and 
 * it's used to define positions of small and big blind players and position of
 * player taking turn. Fold counter is used to check amount of players who folded
 * so that in case of 1 player remaining, he wins.
 *
 *
 */
public class Table implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Player> playerList;
	private Hand tableHand;
	private List<Pot> pot;
	private Deck tableDeck;
	private Integer currentBet;
	private Integer smallBlind;
	private Player winner;
	private Integer dealerPos;
	private Integer turnPos;
	private Integer blindSmallPos;
	private Integer blindBigPos;
	private Integer currentTurn;

	/**
	 * Initializes the table. Defining positions and other stuff is required.
	 */
	public Table() {
		this.playerList = new ArrayList<>();
		this.tableHand = new Hand();
		this.pot = new ArrayList<>();
		this.tableDeck = new Deck();
		this.currentBet = 0;
		this.smallBlind = 0;
		this.winner = null;
		this.dealerPos = -1;
		this.turnPos = 0;
		this.blindSmallPos = 0;
		this.blindBigPos = 0;
		this.currentTurn = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Table)) {
			return false;
		}
		Table obj = (Table)o;
		if (playerList.equals(obj.playerList) && tableHand.equals(obj.tableHand) && pot.equals(obj.pot) &&
				tableDeck.equals(obj.tableDeck) && currentBet.equals(obj.currentBet) && smallBlind.equals(obj.smallBlind) &&
				dealerPos.equals(obj.dealerPos) && turnPos.equals(obj.turnPos) &&
				blindSmallPos.equals(obj.blindSmallPos) && blindBigPos.equals(obj.blindBigPos) &&
				currentTurn.equals(obj.currentTurn)) {
			return true;
		}
		return false;
	}

	// Getters and setters

	public List<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}

	public Hand getTableHand() {
		return tableHand;
	}

	public void setTableHand(Hand tableHand) {
		this.tableHand = tableHand;
	}

	public List<Pot> getPot() {
		return pot;
	}

	public void setPot(List<Pot> pot) {
		this.pot = pot;
	}

	public Deck getTableDeck() {
		return tableDeck;
	}

	public void setTableDeck(Deck tableDeck) {
		this.tableDeck = tableDeck;
	}

	public Integer getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(Integer currentBet) {
		this.currentBet = currentBet;
	}

	public Integer getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(Integer smallBlind) {
		this.smallBlind = smallBlind;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public Integer getDealerPos() {
		return dealerPos;
	}

	public void setDealerPos(Integer dealerPos) {
		this.dealerPos = dealerPos;
	}

	public Integer getTurnPos() {
		return turnPos;
	}

	public void setTurnPos(Integer turnPos) {
		this.turnPos = turnPos;
	}

	public Integer getBlindSmallPos() {
		return blindSmallPos;
	}

	public void setBlindSmallPos(Integer blindSmallPos) {
		this.blindSmallPos = blindSmallPos;
	}

	public Integer getBlindBigPos() {
		return blindBigPos;
	}

	public void setBlindBigPos(Integer blindBigPos) {
		this.blindBigPos = blindBigPos;
	}

	public Integer getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Integer currentTurn) {
		this.currentTurn = currentTurn;
	}

	// End of getters and setters

	// Player-related methods

	/**
	 * Adds a player to table.
	 *
	 * @param player
	 *            Player to add to table.
	 */
	public void addPlayer(Player player) {
		playerList.add(player);
	}

	/**
	 * Returns an amount of players.
	 *
	 * @return Amount of players.
	 */
	public Integer getPlayerSize() {
		return playerList.size();
	}

	/**
	 * Returns amount of cards of specific player hand.
	 *
	 * @param playerPos
	 *            Position of player on table.
	 * @return Amount of cards of specific player hand.
	 */
	public Integer getPlayerHandSize(Integer playerPos) {
		return getPlayer(playerPos).getHand().getHand().size();
	}

	/**
	 * Returns a card from specific player hand.
	 *
	 * @param playerPos
	 *            Position of player on table.
	 * @param cardPos
	 *            Position of card in player's hand.
	 * @return A card from specific player hand.
	 */
	public Card getPlayerHandCard(Integer playerPos, Integer cardPos) {
		return getPlayer(playerPos).getHand().getHand().get(cardPos);
	}

	/**
	 * Returns a hand from player.
	 *
	 * @param playerPos
	 *            Position of player on table.
	 * @return
	 */
	public Hand getPlayerHand(Integer playerPos) {
		return getPlayer(playerPos).getHand();
	}

	/**
	 * Returns a string of player's name.
	 *
	 * @param playerPos
	 *            Position of player on table.
	 * @return String of player's name.
	 */
	public String getPlayerName(Integer playerPos) {
		return getPlayer(playerPos).getName();
	}

	/**
	 * Returns a number of player's balance.
	 *
	 * @param playerPos
	 *            Position of player on table.
	 * @return Number of player's balance.
	 */
	public Integer getPlayerBalance(Integer playerPos) {
		return getPlayer(playerPos).getBalance();
	}

	/**
	 * Returns a player which plays this turn.
	 *
	 * @return Player which plays this turn.
	 */
	public Player getPlayerOnTurn() {
		return getPlayer(turnPos);
	}

	/**
	 * Returns a player on specific position.
	 *
	 * @param playerPos
	 *            Position of player.
	 * @return Player from specified position.
	 */
	public Player getPlayer(Integer playerPos) {
		return playerList.get(playerPos);
	}

	// End of player-related methods

	// Table hand methods

	/**
	 * Returns amount of cards of table hand.
	 *
	 * @return Amount of cards of table hand.
	 */
	public Integer getTableHandSize() {
		return tableHand.getHand().size();
	}

	/**
	 * Returns a card from table hand.
	 *
	 * @return A card from specific table hand.
	 */
	public Card getTableHandCard(Integer cardPos) { return tableHand.getHand().get(cardPos); }

	// End of table hand methods

	// Pot methods

	/**
	 * At the end of the turn when this method is called, all players put their
	 * money to pot.
	 */
	public void payToPot() {
		Integer potAmount = 0;
		pot.add(new Pot());
		for (Player player : playerList) {
			potAmount += player.getCurrentBet();
			player.pay();
			// Player is eligible for getting pot only if he didn't fold or
			// busted out. Not bothered to use iterators for pots in this case,
			// just preforming action on latest added pot. Note: Might be broken.
			if (!player.isFold() && !player.isBustedOut() && !player.isAllInLowerBet()) {
				pot.get(pot.size() - 1).addPlayer(player);
			}
			// If player busted out or folded, he is removed from all pots.
			for (Pot potTotal : pot) {
				if (player.isNotPlaying()) {
					potTotal.getPlayerList().remove(player);
				}
			}
		}
		// If next pot created has identical amount of players, the pot will be
		// transferred to previous one. TODO: This is a serious mess, make it readable.
		if (pot.size() > 1	&& pot.get(pot.size() - 1).getPlayerList().containsAll(pot.get(pot.size() - 2).getPlayerList())) {
			pot.get(pot.size() - 2).setAmount(pot.get(pot.size() - 2).getAmount() + potAmount);
			pot.remove(pot.get(pot.size() - 1));
		}
		// Otherwise, create a new pot.
		else {
			pot.get(pot.size() - 1).setAmount(potAmount);
		}
	}

	/**
	 * Pays player the winning amount.
	 *
	 * @param player
	 *            Player who gets the pot.
	 * @param pot
	 *            Pot which is given to player.
	 */
	public void payToPlayer(Player player, Pot pot) {
		// In case of tie, winner list size should prevent overflow of given cash due to division.
		// Note: This might be broken at some point in case of non-pair division.
		player.setBalance(player.getBalance() + pot.getAmount() / (pot.getWinnerList().isEmpty() ? 1 : pot.getWinnerList().size()));
	}

	// End of pot methods

	// Turn methods

	/**
	 * Gives turn to next player.
	 */
	public void nextPlayerTurn() {
		// Everyone else folded our busted out? The last player should win.
		if (getFoldCounter() + getBustedOutCounter() == playerList.size() - 1) {
			for(Player player : playerList){
				if(!player.isBustedOut() && !player.isFold()){
					// TODO: Remove winner variable and replace it with PlayerStatus if possible.
					winner = player;
					winner.setStatus(PlayerStatus.PLAYER_WINNER);
					payToPot();
					for (Pot potTotal : pot) {
						payToPlayer(winner, potTotal);
					}
					break;
				}
			}
		} else {
			cycleTurnUntilPlayerFound();
		}
	}

	/**
	 * Preforms a next turn and calls specific method for specific turn.
	 */
	public void nextTableTurn() {
		if (currentTurn != 0) {
			payToPot();
			currentBet = 0;
			blindBigPos = -1;
			turnPos = dealerPos;
			cycleTurnUntilPlayerFound();
			for (Player player : playerList) {
				player.setCurrentBet(0);
				if (player.isTurnAllowed()) {
					player.setStatus(PlayerStatus.PLAYER_NORMAL);
				}
			}
		}
		switch (currentTurn) {
		case 0:
			preFlop();
			break;
		case 1:
			flop();
			break;
		case 2:
			turn();
			break;
		case 3:
			river();
			break;
		case 4:
			showdown();
			break;
		}
		currentTurn++;
	}

	/**
	 * Cycles player turn position until there is a player which can preform turn.
	 */
	public void cycleTurnUntilPlayerFound(){
		// If all players went all in, turn cycle is skipped due to showdown.
		if (!isAllIn()) {
			do{
				cycleTurnPos(1);
				// Preventive purpose to avoid infinite loop. Though, not yet perfect.
				if (getFoldCounter()+getBustedOutCounter()+getAllInCounter() >= playerList.size()) {
					break;
				}
			// Cycle turn position until you get a player which is not all in or he's still playing.
			}while (getPlayerOnTurn().isAllIn() || getPlayerOnTurn().isNotPlaying());
		}
	}

	/**
	 * Cycles through turns.
	 * @param amount Amount of positions to change.
	 */
	public void cycleTurnPos(Integer amount){
		turnPos += amount;
		if (turnPos > playerList.size() - 1) {
			turnPos = turnPos - playerList.size();
		}
		else if(turnPos < 0){
			turnPos = turnPos + playerList.size();
		}
	}

	// End of turn methods

	// Hand comparison methods

	/**
	 * Compares hand strength of 2 player's hands.
	 * @param player1 Player 1 hand to check.
	 * @param player2 Player 2 hand to compare.
	 * @return 1 if player 1 has better hand, -1 if player 2 has better hand. Otherwise, comparing with max values.
	 */
	public Integer compareHand(Player player1, Player player2){
		if(player1.getBestHand().getId().getId() > player2.getBestHand().getId().getId()){
			return 1;
		}
		else if(player1.getBestHand().getId().getId() < player2.getBestHand().getId().getId()) {
			return -1;
		}
		else{
			return compareMaxValue(player1, player2);
		}
	}

	/**
	 * Compares max value of hand of 2 player's hands.
	 * @param player1 Player 1 hand to check.
	 * @param player2 Player 2 hand to compare.
	 * @return 1 if player 1 has better max value, -1 if player 2 has better max value. Otherwise, comparing with second max values.
	 */
	public Integer compareMaxValue(Player player1, Player player2){
		if(player1.getBestHand().getMaxValue() > player2.getBestHand().getMaxValue()){
			return 1;
		}
		else if(player1.getBestHand().getMaxValue() < player2.getBestHand().getMaxValue()) {
			return -1;
		}
		else{
			return compareMaxValue2(player1, player2);
		}
	}

	/**
	 * Compares second max value of hand of 2 player's hands.
	 * @param player1 Player 1 hand to check.
	 * @param player2 Player 2 hand to compare.
	 * @return 1 if player 1 has better second max value, -1 if player 2 has better second max value. Otherwise, comparing with kickers.
	 */
	public Integer compareMaxValue2(Player player1, Player player2){
		if(player1.getBestHand().getMaxValue2() > player2.getBestHand().getMaxValue2()){
			return 1;
		}
		else if(player1.getBestHand().getMaxValue2() < player2.getBestHand().getMaxValue2()) {
			return -1;
		}
		else{
			return compareKickers(player1, player2);
		}
	}

	/**
	 * Compares kickers of 2 player's hands.
	 * @param player1 Player 1 hand to check.
	 * @param player2 Player 2 hand to compare.
	 * @return 1 if player 1 has better kicker, -1 if player 2 has better kicker. Otherwise, return 0 as a tie.
	 */
	public Integer compareKickers(Player player1, Player player2){
		for (Integer i = 0; i < player1.getKicker().getHand().size(); i++) {
			if (player1.getKicker().getHand().get(i).getValue() > player2.getKicker().getHand().get(i).getValue()) {
				return 1;
			} else if (player1.getKicker().getHand().get(i).getValue() < player2.getKicker().getHand().get(i).getValue()) {
				return -1;
			}
		}
		return 0;
	}

	// End of hand comparison methods

	// Misc getters

	/**
	 * Returns an amount of folded players.
	 * @return Integer of amount of folded players.
	 */
	public Integer getFoldCounter(){
		Integer counter = 0;
		for(Player player : playerList){
			if (player.isFold()){
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Returns an amount of busted out players.
	 * @return Integer of amount of busted out players.
	 */
	public Integer getBustedOutCounter(){
		Integer counter = 0;
		for(Player player : playerList){
			if (player.isBustedOut()){
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Returns an amount of players who went all in.
	 * @return Integer of amount of players who went all in.
	 */
	public Integer getAllInCounter(){
		Integer counter = 0;
		for(Player player : playerList){
			if (player.isAllIn()){
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Returns an integer of big blind which is just 2x small blind value.
	 *
	 * @return Integer of big blind (2x small blind).
	 */
	public Integer getBigBlind() {
		return smallBlind * 2;
	}

	// End of misc getters

	// Condition methods

	/**
	 * Returns condition that all players are ready to go to next turn.
	 *
	 * @return Did all players who play did bet enough?
	 */
	public Boolean isCurrentBetValid() {
		for (Player player : playerList) {
			if (player.isTurnAllowed()) {
				if (player.getStatus() == PlayerStatus.PLAYER_NORMAL || player.getCurrentBet() < currentBet) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns condition that all players have done all in. (It should return
	 * false if player is not all in, fold or busted out or that player has
	 * higher balance than current bet, because then he still needs to preform
	 * turn.)
	 *
	 * @return Did all players went all in?
	 */
	public Boolean isAllIn() {
		for (Player player : playerList) {
			if (player.isTurnAllowed()	|| player.getBalance() > currentBet) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns condition that everyone busted out except one player, which means
	 * game over.
	 *
	 * @return Is it game over?
	 */
	public Boolean isGameOver() {
		return getBustedOutCounter() >= playerList.size() - 1;
	}

	/**
	 * Returns condition for check.
	 *
	 * @return Is check allowed?
	 */
	public Boolean isCheckAllowed() {
		if (currentBet == 0 || (smallBlind * 2) == currentBet
				&& turnPos == blindBigPos) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns condition for call.
	 *
	 * @return Is call allowed?
	 */
	public Boolean isCallAllowed() {
		if (!isCheckAllowed()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns condition for bet.
	 *
	 * @return Is bet allowed?
	 */
	public Boolean isBetAllowed() {
		if (currentBet == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns condition for raise.
	 *
	 * @return Is raise allowed?
	 */
	public Boolean isRaiseAllowed() {
		if (!isBetAllowed()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Used in case when player shouldn't be able to call (current bet is higher than player's balance, therefore, all in is forced).
	 * @return Is player unable to call?
	 */
	public boolean isPlayerUnableToCall(){
		return currentBet >= getPlayerOnTurn().getBalance();
	}

	/**
	 * Checks for end of current turn. Note: This is used for button disable toggle. Disables action buttons to prevent unexpected action.
	 * @return Is this turn done?
	 */
	public boolean isTurnDone(){
		// Add following condition to force thinking on CPU players: "|| table.getPlayerOnTurn().getComputer()"
		return isNextTurnReady() || isRoundOver();
	}

	/**
	 * Checks for showdown turn.
	 * @return Is this turn showdown?
	 */
	public boolean isShowdown(){
		return currentTurn == 5;
	}

	/**
	 * Checks for end of the round.
	 * @return Is this turn showdown or is there a winner?
	 */
	public boolean isRoundOver(){
		return isShowdown() || winner != null;
	}

	/**
	 * Checks conditions for next turn.
	 * @return Are all players ready to go to next turn?
	 */
	public boolean isNextTurnReady(){
		return isCurrentBetValid() || isAllIn();
	}
	// End of condition methods

	/**
	 * Declare a winner.
	 * TODO: Not sure how sidepots affect winning.
	 */
	public void declareWinner() {
		for (Pot potCurrent : pot) {
			for (Player player : playerList) {
				if (!player.isFold() && !player.isBustedOut()) {
					// Player can win only if he didn't fold or get busted out.
					// By default, winner is not defined. First player is treated as winner temporarily.
					if (winner == null) {
						winner = player;
						potCurrent.cleanUpWinnerList();
						potCurrent.addWinner(player);
					}
					if(compareHand(player, winner) == 1){
						winner = player;
						potCurrent.cleanUpWinnerList();
						potCurrent.addWinner(player);
					}
					else if (compareHand(player, winner) == 0){
						potCurrent.addWinner(player);
					}
				}
			}
			// There players (in case it's a tie) get all from pot (might be split).
			for (Player player : potCurrent.getWinnerList()) {
				payToPlayer(player, potCurrent);
				player.setStatus(PlayerStatus.PLAYER_WINNER);
			}
		}

		// Player gets busted out if he runs out of money, otherwise, he loses this round.
		for (Player player : playerList) {
			if (player.getBalance() <= 0) {
				player.setStatus(PlayerStatus.PLAYER_BUSTED_OUT);
			}
			else if (player.getStatus() != PlayerStatus.PLAYER_WINNER && player.getStatus() != PlayerStatus.PLAYER_FOLD){
				player.setStatus(PlayerStatus.PLAYER_LOST);
			}
		}
	}

	/**
	 * Resets round back to default, keeping players and their balance intact.
	 */
	public void resetRound() {
		for (Player player : playerList) {
			player.setBestHand(new Hand());
			player.setCurrentBet(0);
			player.setDealer(false);
			player.setHand(new Hand());
			player.setKicker(new Hand());
			if (!player.isBustedOut()) {
				player.setStatus(PlayerStatus.PLAYER_NORMAL);
			}
		}
		tableHand = new Hand();
		pot = new ArrayList<>();
		tableDeck = new Deck();
		currentBet = 0;
		smallBlind = 0;
		winner = null;
		turnPos = 0;
		blindSmallPos = 0;
		blindBigPos = 0;
		currentTurn = 0;
	}

	
	/**
	 * Defines positions of dealer, players who pay small and big blinds and player who starts first.
	 */
	public void setPositions() {
		dealerPos = setPos(++dealerPos);
		blindSmallPos = setPos(dealerPos + 1);
		blindBigPos = setPos(blindSmallPos + 1);
		turnPos = setPos(blindBigPos + 1);
	}
	
	/**
	 * Sets the position to specific player eligible for play (Player is not eligible if he's busted out).
	 * @param pos Current position index.
	 * @return Position index of eligible player.
	 */
	public Integer setPos(Integer pos){
		pos = cyclePos(pos);
		while (getPlayer(pos).isBustedOut()) {
			pos = cyclePos(++pos);
		}
		return pos;
	}

	/**
	 * Prevents the position value to go out of bounds.
	 * @param pos Current position value.
	 * @return If the position value is bigger than amount of array, substract with array size. (Should go back to 0.)
	 */
	public Integer cyclePos(Integer pos) {
		if (pos > playerList.size() - 1) {
			pos = pos - playerList.size();
		}
		return pos;
	}

	/**
	 * Checks all hands for best hands.
	 */
	public void checkBestHands() {
		for (Player player : playerList) {
			player.calculateBestHand(tableHand);
		}
	}
	
	/**
	 * 1st player after the dealer pays small blind, 2nd after the dealer pay
	 * big blind, 3rd player starts the turn. Everyone gets 2 cards.
	 */
	public void preFlop() {
		smallBlind = 50;
		playerList.get(blindSmallPos).payBlind(smallBlind);
		playerList.get(blindBigPos).payBlind(getBigBlind());
		currentBet = getBigBlind();
		Integer startPos = blindSmallPos;
		// Drawing should start from player who paid small blind.
		for(Integer i=0;i<playerList.size();i++){
			if(!getPlayer(startPos).isBustedOut()){
			getPlayer(startPos).getHand().draw(tableDeck, 2);
			}
			startPos++;
			if (startPos > playerList.size() - 1) {
				startPos = startPos - playerList.size();
			}
		}
		checkBestHands();
	}

	/**
	 * Draw 3 cards to table.
	 */
	public void flop() {
		tableHand.draw(tableDeck, 3);
		checkBestHands();
		currentBet = 0;
	}

	/**
	 * Draw 1 card to table.
	 */
	public void turn() {
		tableHand.draw(tableDeck, 1);
		checkBestHands();
		currentBet = 0;
	}

	/**
	 * Draw 1 card to table.
	 */
	public void river() {
		tableHand.draw(tableDeck, 1);
		checkBestHands();
		currentBet = 0;
	}

	/**
	 * Players who did not fold show their cards to see who won. 
	 */
	public void showdown() {
		declareWinner();
	}
}
