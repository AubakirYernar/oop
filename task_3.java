package org.example;

import java.sql.*;
import java.util.Scanner;

public class ListOfBooks {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "ernar2005";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                printMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (choice == 1) {
                    createBook(conn, scanner);
                } else if (choice == 2) {
                    showBooks(conn);
                } else if (choice == 3) {
                    updateBooks(conn, scanner);
                } else if (choice == 4) {
                    deleteBooks(conn, scanner);
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
        System.out.println("1. Create a Book");
        System.out.println("2. Show all Library");
        System.out.println("3. Update Book");
        System.out.println("4. Delete Book");
        System.out.println("5. Exit");
        System.out.print("selected action: ");
    }

    private static void createBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the name of the Book: ");
        String nameBook = scanner.nextLine();
        System.out.print("Enter the author of the Book: ");
        String authorBook = scanner.nextLine();
        System.out.print("Enter the year of publication: ");
        double ageBook = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        String sql = "INSERT INTO list_books (nameBook, authorBook, ageBook) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, nameBook);
            preparedStatement.setString(2, authorBook);
            preparedStatement.setDouble(3, ageBook);
            preparedStatement.executeUpdate();
            System.out.println("The Book was successfully added.");
        }
    }

    private static void showBooks(Connection conn) throws SQLException {
        String sql = "SELECT id, nameBook, authorBook, ageBook FROM list_books";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameBook = resultSet.getString("nameBook");
                String authorBook = resultSet.getString("authorBook");
                double ageBook = resultSet.getDouble("ageBook");
                System.out.println("ID: " + id + ", Name of the Book: " + nameBook + ", author of the Book:  " + authorBook + ", the year of publication: " + ageBook);
            }
        }
    }

    private static void updateBooks(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the Book ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter a new name of the Book: ");
        String nameBook = scanner.nextLine();
        System.out.print("Enter a new author of the Book: ");
        String authorBook = scanner.nextLine();
        System.out.print("Enter the year of publication: ");
        double ageBook = scanner.nextDouble();
        scanner.nextLine();
        String sql = "UPDATE list_books SET nameBook = ?, authorBook = ?, ageBook = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, nameBook);
            preparedStatement.setString(2, authorBook);
            preparedStatement.setDouble(3, ageBook);
            preparedStatement.setInt(4, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("The computer has been successfully updated.");
            } else {
                System.out.println("The computer with the specified ID was not found.");
            }
        }
    }

    private static void deleteBooks(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the book ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        String sql = "DELETE FROM list_books WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("The Book was successfully deleted.");
            } else {
                System.out.println("The Book with the specified ID was not found.");
            }
        }
    }
}
