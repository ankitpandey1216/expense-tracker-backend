package com.ankit.expense_tracker.repo;

import com.ankit.expense_tracker.entities.SplitExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitExpenseRepo extends JpaRepository<SplitExpense,Long> {
    @Query("SELECT se FROM SplitExpense se WHERE se.expenseId.expenseId = :id")
    List<SplitExpense> findByExpenseId(Long id);

    @Query("SELECT se FROM SplitExpense se WHERE se.expenseId.expenseId IN :ids")
    List<SplitExpense> findAllSplitsByExpenseIds(List<Long> ids);
}
