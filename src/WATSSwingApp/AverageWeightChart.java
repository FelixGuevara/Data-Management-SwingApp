/**
 * Author: Felix Guevara
 * Course: [CEN-3024C-13950]
 * Date: October 25, 2025,
 * Class: AverageWeightChart.java
 *
 * This class generates and displays a bar chart visualizing the average weight of animals
 * grouped by species using data from the AnimalManager. Utilizes JFreeChart to create
 * the chart and presents it in a modal dialog window.
 */

package WATSSwingApp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AverageWeightChart {

    private final AnimalManager animalManager;

    public AverageWeightChart(AnimalManager manager) {
        this.animalManager = manager;
    }

    /**
     * Method: AverageWeightChart.showChart
     * Purpose: Called from the main application frame to provide users with a graphical
     * representation of species-based weight analytics in the Wildlife Animal Tracking System.
     * Arguments: Component parent - The frame in which the dialog is displayed.
     * Return: void
     */
    public void showChart(Component parent) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, List<Double>> speciesWeights = new HashMap<>();
        for (WildAnimal animal : animalManager.getAllAnimals()) {
            speciesWeights.computeIfAbsent(animal.getSpecies(), k -> new ArrayList<>()).add(animal.getWeight());
        }

        for (Map.Entry<String, List<Double>> entry : speciesWeights.entrySet()) {
            String species = entry.getKey();
            List<Double> weights = entry.getValue();
            double average = weights.stream().mapToDouble(Double::doubleValue).average().orElse(0);
            dataset.addValue(average, "Average Weight", species);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Average Weight by Species",
                "Species",
                "Average Weight (kg)",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Average Weight Chart", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(parent);
        dialog.add(chartPanel);
        dialog.setVisible(true);
    }
}