/**
 * Author: Felix Guevara
 * Course: [CEN-3024C-13950]
 * Date: October 25, 2025,
 * Class: WATSMainFrame.java
 *
 * This is the main JFrame of the Wildlife Animal Tracking System program.
 * Encapsulates the main graphical user interface (GUI) frame for the application.
 * Sets up the primary window and integrates the provided AnimalManager instance
 * Using the AnimalManager object, the frame gains access to all CRUD operations/functionalities,
 * enabling seamless interaction between the user interface and the underlying data management logic.
 */

package WATSSwingApp;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class WATSMainFrame extends JFrame {

    /**
     * Create an instance of AnimalManager to manage animal records
     */
    private final AnimalManager animalManager;
    /**
     * Create an instance of JTable for Animal records
     */
    private JTable animalTable;
    /**
     * Create an instance of DefaultTableModel to define table columns
     */
    private DefaultTableModel tableModel;
    /**
     * Create an instance of JPanel for the main content panel
     */
    private final JPanel mainPanel;


    /**
     * Method: WATSMainFrame
     * Purpose: Constructs the main application frame for the Wildlife Animal Tracking System (WATS),
     * integrating the provided AnimalManager instance to enable data operations and analytics.
     * Arguments: AnimalManager manager - the shared instance responsible for managing animal records.
     * Return: none
     */
    public WATSMainFrame(AnimalManager manager) {
        //passing a reference to the AnimalManager object
        this.animalManager = manager;

        setTitle("Wildlife Animal Tracking System (WATS)");
        setSize(1250, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(230, 230, 250));

        JButton btnImportCSV = new JButton("Upload Animal Records");
        btnImportCSV.addActionListener(e -> importCSVFile());
        sidebar.add(btnImportCSV);

        JButton btnAnimals = new JButton("Add Animal Record");
        btnAnimals.addActionListener(e -> {
            AddAnimalDialog dialog = new AddAnimalDialog(WATSMainFrame.this, animalManager);
            dialog.setVisible(true);
            refreshAnimalTable();
        });
        sidebar.add(btnAnimals);

        //Create all sidebar JButtons
        JButton btnEditAnimal = new JButton("Update Animal Record");
        btnEditAnimal.addActionListener(e -> openEditAnimalDialog());
        sidebar.add(btnEditAnimal);

        JButton btnDeleteAnimal = new JButton("Delete Animal Record");
        btnDeleteAnimal.addActionListener(e -> handleDeleteAnimal());
        sidebar.add(btnDeleteAnimal);

        JButton btnAverageWeight = new JButton("Average Weight by Species");
        btnAverageWeight.addActionListener(e -> showAverageWeightDialog());
        sidebar.add(btnAverageWeight);

        JButton btnShowChart = new JButton("Show Average Weight Chart");
        btnShowChart.addActionListener(e -> {
            AverageWeightChart chart = new AverageWeightChart(animalManager);
            chart.showChart(this);
        });
        sidebar.add(btnShowChart);

        // Create main content panel
        mainPanel = new JPanel(new BorderLayout());
        setupAnimalTable();
        refreshAnimalTable();

        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Method: setupAnimalTable
     * Purpose: Initializes the animal table with predefined column headers and sets up
     * a read-only table model. Applies center alignment to all columns for better readability,
     * Arguments: none
     * Return: void
     */
    private void setupAnimalTable() {
        String[] columnNames = {
                "Tag ID", "Name", "Species", "Age", "Gender", "Weight", "Health Status"
        };

        //Override isCellEditable in the DefaultTableModel for read-only cells
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are read-only
            }
        };

        animalTable = new JTable(tableModel);
        animalTable.setRowHeight(30);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Apply right alignment to all columns
        for (int i = 0; i < animalTable.getColumnCount(); i++) {
            animalTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(animalTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setupContextMenu();
    }

    /**
     * Method: setupContextMenu
     * Purpose: Configures the context menu for the animal table, providing options
     * to update or delete selected animal records. Each menu item is linked to its
     * respective action handler.
     * Arguments: none
     * Return: void
     */
    private void setupContextMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Update Animal");
        JMenuItem deleteItem = new JMenuItem("Delete Animal");

        editItem.addActionListener(e -> openEditAnimalDialog());
        deleteItem.addActionListener(e -> handleDeleteAnimal());

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        animalTable.setComponentPopupMenu(popupMenu);
    }

    /**
     * Method: refreshAnimalTable
     * Purpose: Clears the current table data and repopulates it with the latest list
     * of wild animals retrieved from the AnimalManager.
     * Arguments: none
     * Return: void
     */
    private void refreshAnimalTable() {
        tableModel.setRowCount(0);
        List<WildAnimal> animals = animalManager.getAllAnimals();
        for (WildAnimal animal : animals) {
            Object[] row = {
                    animal.getId(),
                    animal.getName(),
                    animal.getSpecies(),
                    animal.getAge(),
                    animal.getGender(),
                    animal.getWeight(),
                    animal.getHealthStatus(),
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Method: importCSVFile
     * Purpose: Opens a file chooser dialog to allow the user to select a CSV file containing animal data.
     * The selected file is processed by the AnimalManager to import records. Displays a detailed report
     * of any errors or warnings encountered during import, and refreshes the animal table upon completion.
     * Arguments: none
     * Return: void
     */
    private void importCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV File");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            List<String> messages = animalManager.uploadAnimalsFromFile(filePath);

            boolean hasErrors = messages.stream().anyMatch(msg ->
                    msg.startsWith("[ERROR]") || msg.startsWith("[WARNING]")
            );

            if (hasErrors) {
                StringBuilder report = new StringBuilder();
                for (String msg : messages) {
                    report.append(msg).append("\n");
                }

                JTextArea textArea = new JTextArea(report.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));

                JOptionPane.showMessageDialog(this, scrollPane, "Import Report", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "CSV data imported successfully!");
            }

            refreshAnimalTable();
        }
    }

    /**
     * Method: openEditAnimalDialog
     * Purpose: Opens a dialog window to update the details of a selected animal record.
     * Validates that the table contains data and that a row is selected before launching
     * the UpdateAnimalDialog. Refreshes the table after the dialog is closed.
     * Arguments: none
     * Return: void
     */
    private void openEditAnimalDialog() {

        if (animalTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No animal records available to update.", "Empty Table", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = animalTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an animal to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tagId = (int) tableModel.getValueAt(selectedRow, 0);
        UpdateAnimalDialog dialog = new UpdateAnimalDialog(this, animalManager, tagId);
        dialog.setVisible(true);
        refreshAnimalTable();
    }

    /**
     * Method: handleDeleteAnimal
     * Purpose: Handles the deletion of a selected animal record from the table.
     * Prompts the user for confirmation before proceeding with the deletion using
     * the AnimalManager. Displays appropriate messages based on the outcome or any errors.
     * Arguments: none
     * Return: void
     */
    private void handleDeleteAnimal() {

        if (animalTable.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No animal records available to delete.", "Empty Table", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = animalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int tagId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete animal with Tag ID " + tagId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = animalManager.deleteAnimal(tagId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Animal deleted successfully.");
                    refreshAnimalTable();
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Delete Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Method: showAverageWeightDialog
     * Purpose: Prompts the user to enter a species name and calculates the average weight
     * of all animals belonging to that species using the AnimalManager. Displays the result
     * in a dialog box or shows an error message if the calculation fails.
     * Arguments: none
     * Return: void
     */
    private void showAverageWeightDialog() {
        String species = JOptionPane.showInputDialog(this, "Enter species name:", "Average Weight", JOptionPane.QUESTION_MESSAGE);

        if (species != null && !species.trim().isEmpty()) {
            try {
                double average = animalManager.calculateAverageWeightBySpecies(species.trim());
                String message = average > 0
                        ? String.format("Average weight for species '%s': %.2f kg", species, average)
                        : "No animals found for species: " + species;

                JOptionPane.showMessageDialog(this, message, "Average Weight", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
