package org.suai.poker.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import org.suai.poker.model.Player;
import org.suai.poker.model.Pot;
import org.suai.poker.model.Table;

import java.util.ArrayList;
import java.util.List;

public class StatisticsController{
	
	public StatisticsController(){
	}
	
	@FXML
	private PieChart pie;
	
	public void setStatistics(Table table) {
		bakePie(table);
	}
	
	private void bakePie(Table table){
		List<PieChart.Data> pieData = new ArrayList<>();
		Integer balanceSum = 0;
		Integer potSum = 0;
		for (Player player : table.getPlayerList()) {
			if (player.getBalance() > 0) {
				pieData.add(new PieChart.Data(player.getName() + " ($" + (player.getBalance()-player.getCurrentBet()) + ")", player.getBalance()));
				balanceSum += player.getBalance() - player.getCurrentBet();
				potSum += player.getCurrentBet();
			}
		}
		if (table.getCurrentTurn() < 5) {
			for (Pot pot : table.getPot()) {
				potSum += pot.getAmount();
			}
			pieData.add(new PieChart.Data("In pot" + " ($" + potSum + ")", potSum));
		}

		ObservableList<PieChart.Data> pieChartData = FXCollections
				.observableArrayList(pieData);
		pie.setData(pieChartData);
		pie.setTitle("Balance Stats (Total: $" + (balanceSum + potSum) + ")");		
	}
}
