# 🐾 Data Management System GUI Version
The Wildlife Animal Tracking System (WATS) 

## 📘 Project Overview

The Wildlife Animal Tracking System (WATS) is a JFC/Swing-based Java GUI application developed by Felix Guevara, designed to help researchers and conservationists manage wildlife animal data efficiently. It allows users to manually enter animal records, upload data from CSV files, and perform various operations such as updating, deleting, and analyzing animal information.

## ✨ Features

-  Add animal records manually via 
-	Upload animal data from CSV files
-	Display all animal records
-	Update and delete animal records
-	Calculate average weight by species
-	Input validation and error handling
-	Supports health status tracking


## ⚙ Technologies Used

- Java SE 17+
- IntelliJ IDEA 2025
- Standard Java libraries (java.util, java.io)

## ⚙ Setup Instructions

1. Open IntelliJ IDEA 2025.
2. Create a new Java project or import the existing source files:
   - `WildAnimal.java`
   - `AnimalManager.java`
   - `WATSSwingApp.java`
   - `WATSMainFrame.java`
   - `AddAnimalDialog.java`
   - `UpdateAnimalDialog.java`
   - `NumericDocumentFilter.java`
   - `DecimalDocumentFilter.java`
   - `AverageWeightChart.java`
3. Ensure Java SDK 17 or later is configured.
4. Place any animal data files (e.g., `animals.csv`) in the project directory.
5. Run `WATSSwingApp.java` to launch the application.

## ⚙ Usage

- Follow the on-screen sidebar menu to upload, add, update, delete animal records.
- Use the "Upload Animal Records" option to bulk import animals from a file.
- Input constraints:
  - Tag ID must be unique integer.
  - Age and Weight must be non-negative.
  - Valid Health Status: Healthy, Injured, Sick or Unknown

## 📄 CSV Format
* The CSV file should contain the following columns in this order:

```
ID,Species,Name,Age,Gender,Weight,HealthStatus
101,Lion,Simba,5,Male,190.5,Healthy
...
```

## 👤 Author

**Felix Guevara**  
Date: October 26, 2025  
Course: [CEN-3024C-13950]

---
