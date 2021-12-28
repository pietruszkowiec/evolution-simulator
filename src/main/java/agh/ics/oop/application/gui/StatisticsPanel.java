package agh.ics.oop.application.gui;

import agh.ics.oop.simulation.AbstractWorldMap;
import agh.ics.oop.simulation.Genotype;
import agh.ics.oop.simulation.ThreadedSimulationEngine;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import java.util.List;

public class StatisticsPanel {
    private final ThreadedSimulationEngine engine;
    private final AbstractWorldMap map;
    private final XYChart.Series<String, Number> animalNumSeries;
    private final XYChart.Series<String, Number> grassNumSeries;
    private final XYChart.Series<String, Number> avgEnergyLevelSeries;
    private final XYChart.Series<String, Number> avgLifeLengthSeries;
    private final XYChart.Series<String, Number> avgChildrenCntSeries;
    private final Label dominantGenotypeLabel;
    private final Label evolutionInfoLabel;
    private static final int chartWidth = 200;
    private static final int chartHeight = 200;
    private final boolean moveStatistics;


    public StatisticsPanel(ThreadedSimulationEngine engine,
                           AbstractWorldMap map,
                           boolean moveStatistics) {
        this.engine = engine;
        this.map = map;
        this.moveStatistics = moveStatistics;
        this.animalNumSeries = new XYChart.Series<>();
        this.grassNumSeries = new XYChart.Series<>();
        this.avgEnergyLevelSeries = new XYChart.Series<>();
        this.avgLifeLengthSeries = new XYChart.Series<>();
        this.avgChildrenCntSeries = new XYChart.Series<>();
        this.dominantGenotypeLabel
                = new Label(Genotype.generateRandomGenotype().toString());

        if (this.map.isMagicEvolution()) {
            this.evolutionInfoLabel = new Label("\nMagic evolution : "
                    + map.getMagicEvolutionCnt() + " / 3");

        } else {
            this.evolutionInfoLabel = new Label("\nNormal evolution");
        }
    }

    private void slimSeries() {
        this.animalNumSeries.getData().remove(0);
        this.grassNumSeries.getData().remove(0);
        this.avgEnergyLevelSeries.getData().remove(0);
        this.avgLifeLengthSeries.getData().remove(0);
        this.avgChildrenCntSeries.getData().remove(0);
    }

    public void updateMagicEvolutionCnt() {
        if (this.map.isMagicEvolution()) {
            this.evolutionInfoLabel.setText("\nMagic evolution : "
                    + map.getMagicEvolutionCnt() + " / 3");
        }
    }

    public void updateSeries() {
        String day = this.engine.getDay() + "";

        this.animalNumSeries.getData().add(
                new XYChart.Data<>(day, this.map.getAnimalCnt()));

        this.grassNumSeries.getData().add(
                new XYChart.Data<>(day, this.map.getGrassCnt()));

        this.avgEnergyLevelSeries.getData().add(
                new XYChart.Data<>(day, this.map.getAvgEnergy()));

        this.avgLifeLengthSeries.getData().add(
                new XYChart.Data<>(day, this.map.getAvgLifeLength()));

        this.avgChildrenCntSeries.getData().add(
                new XYChart.Data<>(day, this.map.getAvgChildrenCnt()));

        if (this.map.getAnimalsSize() > 0) {
            this.dominantGenotypeLabel.setText(this.map.getDominantGenotype().toString());
        }

        if (moveStatistics && avgChildrenCntSeries.getData().size() > 20) {
            slimSeries();
        }
    }

    private LineChart<String, Number> makeLineChart(XYChart.Series<String, Number> series) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Day");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.getData().add(series);
        lineChart.setPrefSize(chartWidth, chartHeight);
        lineChart.setMinSize(chartWidth, chartHeight);
        lineChart.setMaxSize(chartWidth, chartHeight);
        lineChart.setStyle("-fx-font-size: " + 9 + "px;");
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);
        lineChart.setLegendVisible(false);

        return lineChart;
    }

    public VBox makeAllCharts() {
        LineChart<String, Number> animalGrassNumChart = makeLineChart(this.animalNumSeries);
        this.animalNumSeries.setName("Animals");
        this.grassNumSeries.setName("Grass");
        animalGrassNumChart.getData().addAll(List.of(this.grassNumSeries));
        animalGrassNumChart.setTitle("Num of animals/grass");
        animalGrassNumChart.setLegendVisible(true);

        LineChart<String, Number> avgEnergyLevelChart = makeLineChart(this.avgEnergyLevelSeries);
        avgEnergyLevelChart.setTitle("Avg energy level");

        LineChart<String, Number> avgLifeLengthChart = makeLineChart(this.avgLifeLengthSeries);
        avgLifeLengthChart.setTitle("Avg life length");

        LineChart<String, Number> avgChildrenCntChart = makeLineChart(this.avgChildrenCntSeries);
        avgChildrenCntChart.setTitle("Avg num of children");

        HBox upperHBox = new HBox(animalGrassNumChart, avgEnergyLevelChart);
        HBox lowerHBox = new HBox(avgLifeLengthChart, avgChildrenCntChart);

        VBox mainVBox;
        mainVBox = new VBox(this.evolutionInfoLabel, upperHBox, lowerHBox);
        mainVBox.setAlignment(Pos.CENTER);
        this.evolutionInfoLabel.setAlignment(Pos.CENTER);
        this.evolutionInfoLabel.setTextAlignment(TextAlignment.CENTER);
        return mainVBox;
    }

    public VBox makeDominantGenotypeVBox() {
        Label dominantGenotypeTitle = new Label("Dominant\ngenotype");
        dominantGenotypeTitle.setTextAlignment(TextAlignment.CENTER);
        dominantGenotypeTitle.setAlignment(Pos.CENTER);
        this.dominantGenotypeLabel.setAlignment(Pos.CENTER);
        this.dominantGenotypeLabel.setStyle("-fx-font-family: \"Courier New\";");

        VBox dominantGenotypeVBox = new VBox(dominantGenotypeTitle,
                this.dominantGenotypeLabel);
        dominantGenotypeVBox.setAlignment(Pos.CENTER);

        return dominantGenotypeVBox;
    }
}
