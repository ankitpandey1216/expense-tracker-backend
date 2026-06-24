package com.ankit.expense_tracker.repo;

import com.ankit.expense_tracker.entities.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense,Long> {
    @Query("SELECT e FROM Expense e WHERE e.group.groupId = :id")
    List<Expense> findExpensesByGroupId(Long id);

    @Query("SELECT SUM(e.expenseAmount) FROM Expense e WHERE e.paidBy.userId = :userId")
    BigDecimal getTotalExpenseOfUser(Long userId);

    @Query("SELECT e FROM Expense e WHERE e.group.groupId IN :groupIds")
    List<Expense> findAllExpensesByGroups(List<Long> groupIds);

    @Query("SELECT e FROM Expense e WHERE e.group.groupId IN :groupIds ORDER BY e.createdAt DESC")
    List<Expense> findRecentExpense(List<Long> groupIds, Pageable pageable);
}
