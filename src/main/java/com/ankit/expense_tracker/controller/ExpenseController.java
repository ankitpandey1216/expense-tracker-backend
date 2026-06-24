package com.ankit.expense_tracker.controller;

import com.ankit.expense_tracker.DTO.AddExpenseRequest;
import com.ankit.expense_tracker.DTO.BalanceResponse;
import com.ankit.expense_tracker.DTO.ExpenseDTO;
import com.ankit.expense_tracker.DTO.SplitExpenseDTO;
import com.ankit.expense_tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups/{groupId}/expense")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@PathVariable Long groupId, @RequestBody AddExpenseRequest expenseRequest){
        expenseService.addExpense(groupId,expenseRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getGroupExpenses(@PathVariable Long groupId){
        List<ExpenseDTO> expenseDTOS = expenseService.getGroupExpenses(groupId);
        return ResponseEntity.status(HttpStatus.OK).body(expenseDTOS);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<List<SplitExpenseDTO>> getExpenseSplits(@PathVariable Long expenseId){
        List<SplitExpenseDTO> response = expenseService.getExpenseSplits(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BalanceResponse> getUserBalance(@PathVariable Long userId,@PathVariable Long groupId){
        BalanceResponse response = expenseService.getUserBalance(userId,groupId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
