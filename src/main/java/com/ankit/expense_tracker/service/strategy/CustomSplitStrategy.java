package com.ankit.expense_tracker.service.strategy;

import com.ankit.expense_tracker.DTO.AddExpenseRequest;
import com.ankit.expense_tracker.entities.Expense;
import com.ankit.expense_tracker.entities.SplitExpense;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CustomSplitStrategy implements SplitStrategy{
    @Autowired
    private UserRepo userRepo;
    @Override
    public List<SplitExpense> calulateSplits(Expense expense, AddExpenseRequest addExpenseRequest) {
        BigDecimal amount = BigDecimal.ZERO;
        Map<Long,BigDecimal> splits = addExpenseRequest.getCustomSplits();
        List<Long> userIds = new ArrayList<>();
        for(Map.Entry<Long,BigDecimal> entry: splits.entrySet()){
            amount = amount.add(entry.getValue());
            userIds.add(entry.getKey());
        }
        if(amount.compareTo(expense.getExpenseAmount()) != 0){
            throw new RuntimeException("The amount is not adding up to the total amount");
        }

        List<User> participants = userRepo.findAllById(userIds);
        List<SplitExpense> splitExpenses = new ArrayList<>();
        for(User user : participants){

            SplitExpense splitExpense = new SplitExpense();
            splitExpense.setExpenseId(expense);
            splitExpense.setUserId(user);
            splitExpense.setAmount(splits.get(user.getUserId()));
            splitExpense.setSettled(false);
            splitExpenses.add(splitExpense);
        }
        return splitExpenses;
    }
}
