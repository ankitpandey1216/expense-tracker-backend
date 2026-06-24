package com.ankit.expense_tracker.service.strategy;

import com.ankit.expense_tracker.DTO.AddExpenseRequest;
import com.ankit.expense_tracker.entities.Expense;
import com.ankit.expense_tracker.entities.SplitExpense;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class EqualSplitStrategy implements SplitStrategy{
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<SplitExpense> calulateSplits(Expense expense, AddExpenseRequest addExpenseRequest) {
        int totalMembers = addExpenseRequest.getUserIds().size();
        List<User> splitWith = userRepo.findAllById(addExpenseRequest.getUserIds());
        if(totalMembers != splitWith.size()){
            throw new RuntimeException("Some of the members not found");
        }
        BigDecimal totalAmount = addExpenseRequest.getAmount();
        BigDecimal equalShare = totalAmount.divide(BigDecimal.valueOf(totalMembers),2, RoundingMode.DOWN);
        BigDecimal remainingAmount = totalAmount.subtract(equalShare.multiply(BigDecimal.valueOf(totalMembers)));
        List<SplitExpense> splits = new ArrayList<>();
        for(int i = 0;i < totalMembers;i++){
            BigDecimal amount = equalShare;
            if(remainingAmount.compareTo(BigDecimal.ZERO) > 0){
                amount = amount.add(new BigDecimal("0.01"));
                remainingAmount = remainingAmount.subtract(new BigDecimal("0.01"));
            }
            SplitExpense splitExpense = new SplitExpense();
            splitExpense.setAmount(amount);
            splitExpense.setUserId(splitWith.get(i));
            splitExpense.setSettled(false);
            splitExpense.setExpenseId(expense);
            splits.add(splitExpense);
        }
        return splits;
    }
}
