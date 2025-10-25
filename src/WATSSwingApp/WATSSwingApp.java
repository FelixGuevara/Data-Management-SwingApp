/**
 * Author: Felix Guevara
 * Course: [CEN-3024C-13950]
 * Date: October 25, 2025,
 * Class: WATSSwingApp.java
 *
 * This is the main class of the Wildlife Animal Tracking System program.
 * It provides a Swing GUI for WATS Application for users to interact with the system.
 * Users can add, remove, display, and load Wild-Animals from a file.
 * The program demonstrates basic file I/O, object-oriented design, and input validation.
 */
package WATSSwingApp;

import javax.swing.*;

public class WATSSwingApp {

    public WATSSwingApp() {
        initApp();
    }

    /**
     * Method: initApp
     * Purpose: Initializes the shared instance of the AnimalManager object
     * and the main JFrame of the application.
     * Arguments: none
     * Return: void
     */
    private void initApp() {
        // Using one shared instance of AnimalManager.
        AnimalManager manager = new AnimalManager();
        // Define JFrame and UI components: JPanel, JTable, JButton, JDialogs
        WATSMainFrame mainFrame = new WATSMainFrame(manager);
        mainFrame.setVisible(true);
    }

    /**
     * Method: main
     * Purpose: Entry point of the program. Starts the menu loop.
     * Arguments: String[] args - command-line arguments (not used)
     * Return: void
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WATSSwingApp::new);
    }

}
