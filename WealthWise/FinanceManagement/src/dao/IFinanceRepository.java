package dao;

import entity.Expense;
import entity.User;
import java.util.List;

public interface IFinanceRepository {
    boolean createUser(User user);
    boolean createExpense(Expense expense);
    boolean deleteUser(int userId);
    boolean deleteExpense(int expenseId);
    List<Expense> getAllExpenses(int userId);
    boolean updateExpense(int userId, Expense expense);
    
    // New method for authentication
    User validateUser(String username, String password);
    List<Expense> getExpensesByDateRange(int userId, Date startDate, Date endDate);

}
