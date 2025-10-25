/**
 * Author: Felix Guevara
 * Course: [CEN-3024C-13950]
 * Date: October 8, 2025,
 * Class: AnimalManager.java
 *
 * This class manages a collection of WildAnimal objects. It provides functionality
 * to add, remove, display, and load WildAnimals from a file.
 * This class serves as the core logic handler for the Wildlife Animal Tracking System
 */

package WATSSwingApp;
import java.util.*;
import java.io.*;

public class AnimalManager {

    /**
     * List to store all animal records
     */
    private List<WildAnimal> animals = new ArrayList<>();
    /**
     * Set used to keep all unique IDs to animals
     */
    private Set<Integer> usedIds = new HashSet<>();
    /**
     * List to store all valid Health Statuses
     */
    private final List<String> validHealthStatuses = Arrays.asList("Healthy", "Injured", "Sick", "Unknown");


    /**
     * Method: addAnimalManually
     * Purpose: Adds a new animal manually using user input.
     * Arguments: Scanner - the scanner instance for reading user input from the console
     * Return: boolean - true if success, false otherwise
     */
    public boolean addAnimalManually(Scanner scanner) {
        boolean returnVal = false;
        try {
            System.out.print("Tag ID (unique integer): ");
            int id = Integer.parseInt(scanner.nextLine());
            if (usedIds.contains(id)) {
                System.out.println("[ERROR] Tag ID already exists.");
                return returnVal;
            }

            System.out.print("Species: ");
            String species = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Age (non-negative): ");
            int age = Integer.parseInt(scanner.nextLine());
            if (age < 0) throw new IllegalArgumentException("Age must be non-negative.");

            System.out.print("Gender: ");
            String gender = scanner.nextLine();

            System.out.print("Weight (positive): ");
            double weight = Double.parseDouble(scanner.nextLine());
            if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");

            System.out.print("Health Status (Healthy/Injured/Sick/Unknown): ");
            String health = scanner.nextLine();
            if (!validHealthStatuses.contains(health)) {
                throw new IllegalArgumentException("Invalid health status.");
            }

            animals.add(new WildAnimal(id, species, name, age, gender, weight, health));
            usedIds.add(id);
            returnVal = true;
            System.out.println("[SUCCESS] Animal added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid number format.");
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return returnVal;
    }

    /**
     * Method: addAnimal
     * Purpose: Adds a new animal record.
     * Arguments: WildAnimal anima - the animal record to be added
     * Return: boolean - true if success, false otherwise
     */
    public boolean addAnimal(WildAnimal animal) {
        boolean returnVal = true;
        // Validate unique ID
        if (usedIds.contains(animal.getId())) {
            returnVal = false;
            throw new IllegalArgumentException("Tag ID already exists");
        }

        // Validate age
        if (animal.getAge() < 0) {
            returnVal = false;
            throw new IllegalArgumentException("Age must be non-negative");
        }

        // Validate weight
        if (animal.getWeight() <= 0) {
            returnVal = false;
            throw new IllegalArgumentException("Weight must be positive");
        }

        // Validate health status
        if (!validHealthStatuses.contains(animal.getHealthStatus())) {
            returnVal = false;
            throw new IllegalArgumentException("Invalid health status");
        }

        // Add animal to list and track ID
        animals.add(animal);
        usedIds.add(animal.getId());

        return returnVal;
    }

    /**
     * Method: uploadAnimalsFromFile
     * Purpose: Uploads animals from a CSV file (format: species,name,age,gender,weight)
     * Arguments: String filename - the file name to load animal data from
     * Return: Return: boolean - true if success, false otherwise
     */
    public List<String> uploadAnimalsFromFile(String filename) {
        List<String> messages = new ArrayList<>();
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 7) {
                    messages.add("[WARNING] Skipping line due to missing fields: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    if (usedIds.contains(id)) {
                        messages.add("[WARNING] Duplicate Tag ID skipped: " + id);
                        continue;
                    }

                    String species = parts[1].trim();
                    String name = parts[2].trim();
                    int age = Integer.parseInt(parts[3].trim());
                    if (age < 0) throw new IllegalArgumentException("Age must be non-negative.");

                    String gender = parts[4].trim();
                    double weight = Double.parseDouble(parts[5].trim());
                    if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");

                    String health = parts[6].trim();
                    if (!validHealthStatuses.contains(health)) {
                        throw new IllegalArgumentException("Invalid health status: " + health);
                    }

                    animals.add(new WildAnimal(id, species, name, age, gender, weight, health));
                    usedIds.add(id);
                    count++;

                } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid number format.");
                } catch (IllegalArgumentException e) {
                    System.out.println("[ERROR] " + e.getMessage());
                }
            }

            messages.add("[SUCCESS] " + count + " animals uploaded successfully.");

        } catch (IOException e) {
            messages.add("[ERROR] File error: " + e.getMessage());
        }

        return messages;
    }

    /**
     * Method: displayAllAnimals
     * Purpose: Displays all animals currently stored in the system.
     * Arguments: None
     * Return: void
     */
    public void displayAllAnimals() {
        if (animals.isEmpty()) {
            System.out.println("[INFO] No animals to display.");
        } else {
            animals.forEach(System.out::println);
        }
    }

    /**
     * Method: updateAnimal
     * Purpose: Updates an existing animal's details by Tag ID.
     * Arguments: Scanner - the scanner instance for reading user input from the console
     * Return: boolean - true if success, false otherwise
     */
    public boolean updateAnimal(Scanner scanner) {
        boolean returnVal = false;
        try {
            System.out.print("Enter Animal Tag ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            WildAnimal animal = findAnimalById(id);
            if (animal == null) {
                System.out.println("[ERROR] Animal not found.");
                return returnVal;
            }

            System.out.print("New Species: ");
            animal.setSpecies(scanner.nextLine());
            System.out.print("New Name: ");
            animal.setName(scanner.nextLine());

            System.out.print("New Age: ");
            int age = Integer.parseInt(scanner.nextLine());
            if (age < 0) throw new IllegalArgumentException("Age must be non-negative.");
            animal.setAge(age);

            System.out.print("New Gender: ");
            animal.setGender(scanner.nextLine());

            System.out.print("New Weight: ");
            double weight = Double.parseDouble(scanner.nextLine());
            if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
            animal.setWeight(weight);

            System.out.print("New Health Status (Healthy/Injured/Sick/Unknown): ");
            String health = scanner.nextLine();
            if (!validHealthStatuses.contains(health)) {
                throw new IllegalArgumentException("Invalid health status.");
            }
            animal.setHealthStatus(health);

            returnVal = true;
            System.out.println("[SUCCESS] Animal updated successfully.");
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
        return returnVal;
    }

    /**
     * Method: updateAnimal
     * Purpose: Updates an existing animal's details by Tag ID.
     * Arguments: int id - Tag ID, WildAnimal updatedAnimal - animal record to be updated
     * Return: boolean - true if success, false otherwise
     */
    public boolean updateAnimal(int id, WildAnimal updatedAnimal) {
        boolean returnVal = true;
        WildAnimal existingAnimal = findAnimalById(id);
        if (existingAnimal == null) {
            returnVal = false;
            throw new IllegalArgumentException("Animal with ID " + id + " not found.");
        }

        // Validate age
        if (updatedAnimal.getAge() < 0) {
            returnVal = false;
            throw new IllegalArgumentException("Age must be non-negative");
        }

        // Validate weight
        if (updatedAnimal.getWeight() <= 0) {
            returnVal = false;
            throw new IllegalArgumentException("Weight must be positive");
        }

        // Validate health status
        if (!validHealthStatuses.contains(updatedAnimal.getHealthStatus())) {
            returnVal = false;
            throw new IllegalArgumentException("Invalid health status");
        }

        // Apply updates
        existingAnimal.setSpecies(updatedAnimal.getSpecies());
        existingAnimal.setName(updatedAnimal.getName());
        existingAnimal.setAge(updatedAnimal.getAge());
        existingAnimal.setGender(updatedAnimal.getGender());
        existingAnimal.setWeight(updatedAnimal.getWeight());
        existingAnimal.setHealthStatus(updatedAnimal.getHealthStatus());

        return returnVal;
    }

    /**
     * Method: deleteAnimal
     * Purpose: Deletes an animal by Tag ID.
     * Arguments: Scanner - the scanner instance for reading user input from the console
     * Return: boolean - true if success, false otherwise
     */
    public boolean deleteAnimal(Scanner scanner) {
        boolean returnVal = false;
        try {
            System.out.print("Enter Animal Tag ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            WildAnimal animal = findAnimalById(id);
            if (animal != null) {
                animals.remove(animal);
                usedIds.remove(id);
                returnVal = true;
                System.out.println("[SUCCESS] Animal deleted successfully.");
            } else {
                System.out.println("[ERROR] Animal not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid ID format.");
        }
        return returnVal;
    }

    /**
     * Method: deleteAnimal
     * Purpose: Deletes an animal by Tag ID.
     * Arguments: int id - the tag id for the record to be deleted
     * Return: boolean - true if success, false otherwise
     */
    public boolean deleteAnimal(int id) {
        boolean returnVal = true;
        WildAnimal animal = findAnimalById(id);
        if (animal != null) {
            animals.remove(animal);
            usedIds.remove(id);
        } else {
            returnVal = false;
            throw new IllegalArgumentException("Animal with ID " + id + " not found.");
        }

        return returnVal;
    }

    /**
     * Method: calculateAverageWeightBySpecies
     * Purpose: Calculates and displays the average weight of animals by species.
     * Arguments: Scanner - the scanner instance for reading user input from the console
     * Return: void
     */
    public void calculateAverageWeightBySpecies(Scanner scanner) {
        System.out.print("Enter species: ");
        String species = scanner.nextLine();
        double totalWeight = 0;
        int count = 0;
        for (WildAnimal a : animals) {
            if (a.getSpecies().equalsIgnoreCase(species)) {
                totalWeight += a.getWeight();
                count++;
            }
        }

        if (count > 0) {
            System.out.printf("[INFO] Average weight for species '%s': %.2f\n", species, totalWeight / count);
        } else {
            System.out.println("[INFO] No animals found for that species.");
        }
    }

    /**
     * Method: calculateAverageWeightBySpecies
     * Purpose: Calculates and displays the average weight of animals by species.
     * Arguments: String species - the animal species to use
     * Return: double - calculated average weight
     */
    public double calculateAverageWeightBySpecies(String species) {
        double totalWeight = 0;
        double average = 0;

        int count = 0;

        for (WildAnimal animal : animals) {
            if (animal.getSpecies().equalsIgnoreCase(species)) {
                totalWeight += animal.getWeight();
                count++;
            }
        }

        if (count > 0) {
            average = totalWeight / count;
            System.out.printf("[INFO] Average weight for species '%s': %.2f%n", species, average);
        } else {
            System.out.println("[INFO] No animals found for species: " + species);
        }

        return average;
    }

    /**
     * Method: findAnimalById
     * Purpose: Helper method to find an animal by its Tag ID.
     * Arguments: int id - the ID of the animal to find
     * Return: WildAnimal object found
     */
    WildAnimal findAnimalById(int id) {
        return animals.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
    }

    /**
     * Method: getAllAnimals
     * Purpose: Helper method to get a list of current WildAnimals.
     * Arguments: N/A
     * Return: List of WildAnimals
     */
    public List<WildAnimal> getAllAnimals() {

        return new ArrayList<>(animals);
    }
}