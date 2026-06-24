package com.ankit.expense_tracker.service.strategy;

import com.ankit.expense_tracker.DTO.AddExpenseRequest;
import com.ankit.expense_tracker.entities.Expense;
import com.ankit.expense_tracker.entities.SplitExpense;

import java.util.List;

public interface SplitStrategy {
    List<SplitExpense> calulateSplits(Expense expense, AddExpenseRequest addExpenseRequest);
}
