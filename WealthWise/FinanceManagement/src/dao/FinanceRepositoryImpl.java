package dao;

import entity.Expense;
import entity.User;
import util.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceRepositoryImpl implements IFinanceRepository {

    @Override
    public boolean createUser(User user) {
        String query = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createExpense(Expense expense) {
        String query = "INSERT INTO Expenses (user_id, amount, category_id, date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, expense.getUserId());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setInt(3, expense.getCategoryId());
            pstmt.setDate(4, new java.sql.Date(expense.getDate().getTime()));
            pstmt.setString(5, expense.getDescription());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        String deleteExpensesQuery = "DELETE FROM Expenses WHERE user_id = ?";
        String deleteUserQuery = "DELETE FROM Users WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement deleteExpensesStmt = conn.prepareStatement(deleteExpensesQuery);
             PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery)) {

            deleteExpensesStmt.setInt(1, userId);
            deleteExpensesStmt.executeUpdate();

            deleteUserStmt.setInt(1, userId);
            int rowsDeleted = deleteUserStmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteExpense(int expenseId) {
        String query = "DELETE FROM Expenses WHERE expense_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, expenseId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM Expenses WHERE user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("expense_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        rs.getDate("date"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    @Override
    public boolean updateExpense(int userId, Expense expense) {
        String query = "UPDATE Expenses SET amount = ?, category_id = ?, date = ?, description = ? WHERE expense_id = ? AND user_id = ?";

        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, expense.getAmount());
            pstmt.setInt(2, expense.getCategoryId());
            pstmt.setDate(3, new java.sql.Date(expense.getDate().getTime()));
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt(5, expense.getExpenseId());
            pstmt.setInt(6, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public User validateUser(String username, String password) {
        String query = "SELECT * FROM Users WHERE username = ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String hashedInputPassword = new User().setPassword(password); // Hash input
                
                if (storedPassword.equals(hashedInputPassword)) {
                    return new User(rs.getInt("user_id"), rs.getString("username"), storedPassword, rs.getString("email"));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Authentication failed
    }
    
    @Override
    public List<Expense> getExpensesByDateRange(int userId, Date startDate, Date endDate) {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM Expenses WHERE user_id = ? AND date BETWEEN ? AND ?";
        
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setInt(1, userId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                expenses.add(new Expense(
                    rs.getInt("expense_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getInt("category_id"),
                    rs.getDate("date"),
                    rs.getString("description")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

}
