package app;

import dao.FinanceRepositoryImpl;
import dao.IFinanceRepository;
import entity.Expense;
import entity.User;
import exception.ExpenseNotFoundException;
import exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FinanceApp {
    private static final IFinanceRepository repository = new FinanceRepositoryImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== WealthWise ====");
            System.out.println("1. Add User");
            System.out.println("2. Add Expense");
            System.out.println("3. Delete User");
            System.out.println("4. Delete Expense");
            System.out.println("5. Update Expense");
            System.out.println("6. View All Expenses");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    addExpense();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    deleteExpense();
                    break;
                case 5:
                    updateExpense();
                    break;
                case 6:
                    viewAllExpenses();
                    break;
                case 7:
                    System.out.println("Exiting application...");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        User user = new User(0, username, password, email);
        if (repository.createUser(user)) {
            System.out.println("User added successfully.");
        } else {
            System.out.println("Error adding user.");
        }
    }

    private static void addExpense() {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(0, userId, amount, categoryId, new Date(), description);
        if (repository.createExpense(expense)) {
            System.out.println("Expense added successfully.");
        } else {
            System.out.println("Error adding expense.");
        }
    }

    private static void deleteUser() {
        System.out.print("Enter User ID to delete: ");
        int userId = scanner.nextInt();

        if (repository.deleteUser(userId)) {
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("Error deleting user. Ensure they exist.");
        }
    }

    private static void deleteExpense() {
        System.out.print("Enter Expense ID to delete: ");
        int expenseId = scanner.nextInt();

        if (repository.deleteExpense(expenseId)) {
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Error deleting expense.");
        }
    }

    private static void updateExpense() {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();
        System.out.print("Enter Expense ID to update: ");
        int expenseId = scanner.nextInt();
        System.out.print("Enter new amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter new Category ID: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();

        Expense expense = new Expense(expenseId, userId, amount, categoryId, new Date(), description);
        if (repository.updateExpense(userId, expense)) {
            System.out.println("Expense updated successfully.");
        } else {
            System.out.println("Error updating expense.");
        }
    }

    private static void viewAllExpenses() {
        System.out.print("Enter User ID: ");
        int userId = scanner.nextInt();

        List<Expense> expenses = repository.getAllExpenses(userId);
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            System.out.println("Expenses:");
            for (Expense expense : expenses) {
                System.out.printf("ID: %d | Amount: %.2f | Category: %d | Date: %s | Description: %s%n",
                        expense.getExpenseId(), expense.getAmount(), expense.getCategoryId(),
                        expense.getDate(), expense.getDescription());
            }
        }
    }
    
    private static void viewExpensesByDate() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String start = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String end = scanner.nextLine();
        
        try {
            Date startDate = java.sql.Date.valueOf(start);
            Date endDate = java.sql.Date.valueOf(end);
            
            List<Expense> expenses = repository.getExpensesByDateRange(currentUser.getUserId(), startDate, endDate);
            if (expenses.isEmpty()) {
                System.out.println("No expenses found in this date range.");
            } else {
                System.out.println("\n==== Expense Report ====");
                for (Expense exp : expenses) {
                    System.out.printf("ID: %d | Amount: %.2f | Category: %d | Date: %s | Description: %s%n",
                        exp.getExpenseId(), exp.getAmount(), exp.getCategoryId(), exp.getDate(), exp.getDescription());
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format! Please use YYYY-MM-DD.");
        }
    }

}
