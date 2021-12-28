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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class StatisticsPanel {
    private final ThreadedSimulationEngine engine;
    private final AbstractWorldMap map;
    public XYChart.Series<String, Number> animalNumSeries;
    public XYChart.Series<String, Number> grassNumSeries;
    public XYChart.Series<String, Number> avgEnergyLevelSeries;
    public XYChart.Series<String, Number> avgLifeLengthSeries;
    public XYChart.Series<String, Number> avgChildrenCntSeries;
    public Label dominantGenotypeLabel;
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
    }

    public void slimSeries() {
        this.animalNumSeries.getData().remove(0);
        this.grassNumSeries.getData().remove(0);
        this.avgEnergyLevelSeries.getData().remove(0);
        this.avgLifeLengthSeries.getData().remove(0);
        this.avgChildrenCntSeries.getData().remove(0);
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
        this.dominantGenotypeLabel.setText(this.map.getDominantGenotype().toString());

        if (moveStatistics && avgChildrenCntSeries.getData().size() > 20) {
            slimSeries();
        }
    }

    public LineChart<String, Number> makeLineChart(XYChart.Series<String, Number> series) {
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
//        LineChart animalNumChart = makeLineChart(this.animalNumSeries);
//        animalNumChart.setTitle("Number of animals");

//        LineChart grassNumChart = makeLineChart(this.grassNumSeries);
//        grassNumChart.setTitle("Number of grass");

        LineChart<String, Number> animalGrassNumChart = makeLineChart(this.animalNumSeries);
        this.animalNumSeries.setName("Animals");
        this.grassNumSeries.setName("Grass");
        animalGrassNumChart.getData().addAll(this.grassNumSeries);
        animalGrassNumChart.setTitle("Num of animals/grass");
        animalGrassNumChart.setLegendVisible(true);

        LineChart<String, Number> avgEnergyLevelChart = makeLineChart(this.avgEnergyLevelSeries);
        avgEnergyLevelChart.setTitle("Avg energy level");

        LineChart<String, Number> avgLifeLengthChart = makeLineChart(this.avgLifeLengthSeries);
        avgLifeLengthChart.setTitle("Avg life length");

        LineChart<String, Number> avgChildrenCntChart = makeLineChart(this.avgChildrenCntSeries);
        avgChildrenCntChart.setTitle("Avg num of children");

        Label dominantGenotypeTitleLabel = new Label("Dominant\ngenotype");
        dominantGenotypeTitleLabel.setTextAlignment(TextAlignment.CENTER);

        VBox dominantGenotypeVBox = new VBox(dominantGenotypeTitleLabel, this.dominantGenotypeLabel);
        dominantGenotypeTitleLabel.setAlignment(Pos.CENTER);
        this.dominantGenotypeLabel.setAlignment(Pos.CENTER);
        this.dominantGenotypeLabel.setStyle("-fx-font-family: \"Courier New\";");

//        dominantGenotypeVBox.setAlignment(Pos.CENTER);
//        this.dominantGenotypeLabel.setFont(new Font(8));
//        BorderPane dominantGenotypePane = new BorderPane();
//        dominantGenotypePane.setCenter(dominantGenotypeVBox);
//        dominantGenotypePane.setTop(dominantGenotypeTitleLabel);
        BorderPane.setAlignment(dominantGenotypeVBox, Pos.CENTER);

//        HBox upperHBox = new HBox(animalNumChart, grassNumChart, avgEnergyLevelChart);
//        HBox midHBox = new HBox(avgLifeLengthChart, avgChildrenCntChart, dominantGenotypeVBox);

        HBox upperHBox = new HBox(animalGrassNumChart, avgEnergyLevelChart);
        HBox midHBox = new HBox(avgLifeLengthChart, avgChildrenCntChart);

//        HBox lowerHBox = new HBox(avgChildrenCntChart, dominantGenotypeVBox);

//        VBox statisticsVBox = new VBox(
//                dominantGenotypeVBox,
//                animalNumChart,
//                grassNumChart,
//                avgEnergyLevelChart,
//                avgLifeLengthChart,
//                avgChildrenCntChart);

        return new VBox(upperHBox, midHBox);

    }

    public VBox makeDominantGenotypeVBox() {
        Label dominantGenotypeLabel = new Label("Dominant\ngenotype");
        dominantGenotypeLabel.setTextAlignment(TextAlignment.CENTER);
        return new VBox(dominantGenotypeLabel, this.dominantGenotypeLabel);
    }
}
