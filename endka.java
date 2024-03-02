package org.example;

import java.sql.*;
import java.util.Scanner;

interface CarRepository {
    void createCar(String brand, String model, int age, double price) throws SQLException;
    void showCars() throws SQLException;
    void updateCar(int id, String brand, String model, int age, double price) throws SQLException;
    void deleteCar(int id) throws SQLException;
}

class DatabaseCarRepository implements CarRepository {
    private final Connection conn;

    public DatabaseCarRepository(Connection conn) {
        this.conn = conn;
    }

    public void createCar(String brand, String model, int age, double price) throws SQLException {
        String sql = "INSERT INTO cars (brandCar, modelCar, ageCar, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, model);
            preparedStatement.setInt(3, age);
            preparedStatement.setDouble(4, price);
            preparedStatement.executeUpdate();
            System.out.println("The Car was successfully added.");
        }
    }


    public void showCars() throws SQLException {
        String sql = "SELECT id, brandCar, modelCar, ageCar, price FROM cars";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("brandCar");
                String model = resultSet.getString("modelCar");
                int age = resultSet.getInt("ageCar");
                double price = resultSet.getDouble("price");
                System.out.println("ID: " + id + ", Brand: " + brand + ", Model: " + model + ", Age: " + age + ", Price: $" + price);
            }
        }
    }


    public void updateCar(int id, String brand, String model, int age, double price) throws SQLException {
        String sql = "UPDATE cars SET brandCar = ?, modelCar = ?, ageCar = ?, price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, model);
            preparedStatement.setInt(3, age);
            preparedStatement.setDouble(4, price);
            preparedStatement.setInt(5, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("The car has been successfully updated.");
            } else {
                System.out.println("The car with the specified ID was not found.");
            }
        }
    }

    public void deleteCar(int id) throws SQLException {
        String sql = "DELETE FROM cars WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("The Car was successfully deleted.");
            } else {
                System.out.println("The Car with the specified ID was not found.");
            }
        }
    }
}

public class CarManagementSystem {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "ernar2005")) {
            Scanner scanner = new Scanner(System.in);
            CarRepository carRepository = new DatabaseCarRepository(conn);
            boolean exit = false;
            while (!exit) {
                printMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    createCar(carRepository, scanner);;
                } else if (choice == 2) {
                    carRepository.showCars();
                } else if (choice == 3) {
                    updateCar(carRepository, scanner);
                } else if (choice == 4) {
                    deleteCar(carRepository, scanner);
                } else if (choice == 5) {
                    exit = true;
                } else {
                    System.out.println("Invalid input. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("1. Create a Car");
        System.out.println("2. Show all Cars");
        System.out.println("3. Update Car");
        System.out.println("4. Delete Car");
        System.out.println("5. Exit");
        System.out.print("selected action: ");
    }

    private static void createCar(CarRepository carRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter the brand of the Car: ");
        String brand = scanner.nextLine();
        System.out.print("Enter the model of the Car: ");
        String model = scanner.nextLine();
        System.out.print("Enter the age of the Car: ");
        int age = scanner.nextInt();
        System.out.print("Enter the price of the Car: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        carRepository.createCar(brand, model, age, price);
    }

    private static void updateCar(CarRepository carRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter the Car ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter a new brand of the Car: ");
        String brand = scanner.nextLine();
        System.out.print("Enter a new model of the Car: ");
        String model = scanner.nextLine();
        System.out.print("Enter the new age of the Car: ");
        int age = scanner.nextInt();
        System.out.print("Enter the new price of the Car: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        carRepository.updateCar(id, brand, model, age, price);
    }

    private static void deleteCar(CarRepository carRepository, Scanner scanner) throws SQLException {
        System.out.print("Enter the Car ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        carRepository.deleteCar(id);
    }
}
