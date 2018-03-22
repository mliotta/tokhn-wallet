package io.tokhn.view;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.tokhn.core.Token;
import io.tokhn.node.Network;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;

public class BalancesController {
	
	@FXML
	private PieChart pieChart;
	
	private ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	
	public void setBalanceData(Map<Network, Token> balances) {
		List<Data> data = balances.entrySet().stream()
			.map(entry -> new PieChart.Data(entry.getKey().toString(), entry.getValue().getDecimalValue()))
			.collect(Collectors.toList());
		pieChartData.setAll(data);
		
		pieChartData.forEach(d -> d.nameProperty().bind(Bindings.concat(d.getName(), " ", d.pieValueProperty())));
	}
	
	@FXML
    private void initialize() {
		Network.getAll().forEach(n -> pieChartData.add(new PieChart.Data(n.toString(), Double.NaN)));
		pieChart.setData(pieChartData);
	}
}