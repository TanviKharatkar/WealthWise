package com.TestingOne.FinanceManagementTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dao.FinanceRepositoryImpl;
import dao.IFinanceRepository;
import entity.Expense;
import entity.User;

public class AppTest {
    private Connection connection;
    private IFinanceRepository repository;

    @BeforeEach
    public void setUp() throws SQLException {
        // Create an in-memory H2 database
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "");
        repository = new FinanceRepositoryImpl();

        // Create necessary tables
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE Users (user_id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) UNIQUE, password VARCHAR(255), email VARCHAR(255) UNIQUE)");
            stmt.execute("CREATE TABLE Expenses (expense_id INT AUTO_INCREMENT PRIMARY KEY, user_id INT, amount DOUBLE, category_id INT, date DATE, description VARCHAR(255))");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testAddUser() {
        User user = new User(0, "testuser", "password123", "testuser@example.com");
        assertTrue(repository.createUser(user));

        // Verify the user was added
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM Users WHERE username = 'testuser'");
            rs.next();
            assertEquals(1, rs.getInt(1)); // Expecting 1 user
        } catch (SQLException e) {
            fail("SQLException should not be thrown");
        }
    }

    @Test
    public void testAddExpense() {
        User user = new User(0, "testuser", "password123", "testuser@example.com");
        repository.createUser(user);

        Expense expense = new Expense(0, 1, 500.0, 2, new java.util.Date(), "Lunch expense");
        assertTrue(repository.createExpense(expense));

        // Verify the expense was added
        try (Statement stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM Expenses WHERE description = 'Lunch expense'");
            rs.next();
            assertEquals(1, rs.getInt(1)); // Expecting 1 expense
        } catch (SQLException e) {
            fail("SQLException should not be thrown");
        }
    }

    @Test
    public void testGetAllExpenses() {
        User user = new User(0, "testuser", "password123", "testuser@example.com");
        repository.createUser(user);

        Expense expense = new Expense(0, 1, 500.0, 2, new java.util.Date(), "Lunch expense");
        repository.createExpense(expense);

        List<Expense> expenses = repository.getAllExpenses(1);
        assertFalse(expenses.isEmpty(), "Expenses list should not be empty.");
        assertEquals(1, expenses.size(), "There should be one expense.");
    }

    @Test
    public void testAddUserException() {
        User user1 = new User(0, "testuser", "password123", "testuser@example.com");
        User user2 = new User(0, "testuser", "password123", "testuser@example.com");

        repository.createUser(user1);

        Exception exception = assertThrows(Exception.class, () -> repository.createUser(user2));
        assertTrue(exception.getMessage().contains("Duplicate entry"), "Exception message should contain 'Duplicate entry'");
    }
}
