package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.DTO.*;
import com.ankit.expense_tracker.entities.*;
import com.ankit.expense_tracker.repo.*;
import com.ankit.expense_tracker.service.factory.SplitStrategyFactory;
import com.ankit.expense_tracker.service.strategy.SplitStrategy;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SplitExpenseRepo splitExpenseRepo;
    @Autowired
    private SplitStrategyFactory splitStrategyFactory;
    @Autowired
    private GroupMemberRepo groupMemberRepo;

    @Transactional
    public void addExpense(Long groupId, AddExpenseRequest expenseRequest) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group Not fount with groupId: "+ groupId));
        User paidBy = userRepo.findById(expenseRequest.getPaidBy()).orElseThrow(() -> new RuntimeException("User not found with id: " + expenseRequest.getPaidBy()));
        Expense expense = new Expense();
        expense.setExpenseAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setSplitType(expenseRequest.getSplitType());
        Expense savedExpense = expenseRepo.save(expense);
        SplitStrategy splitStrategy = splitStrategyFactory.getSplitStrategy(expenseRequest.getSplitType());
        List<SplitExpense> splitExpenses = splitStrategy.calulateSplits(savedExpense,expenseRequest);
        splitExpenseRepo.saveAll(splitExpenses);
    }


    public List<ExpenseDTO> getGroupExpenses(Long groupId) {
        groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        List<Expense> response = expenseRepo.findExpensesByGroupId(groupId);
        return response.stream().map(e -> {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setExpenseId(e.getExpenseId());
            expenseDTO.setDescription(e.getDescription());
            expenseDTO.setPaidBy(e.getPaidBy().getName());
            expenseDTO.setPaidById(e.getPaidBy().getUserId());
            expenseDTO.setSplitType(e.getSplitType());
            expenseDTO.setAmount(e.getExpenseAmount());
            expenseDTO.setCreatedAt(e.getCreatedAt());
            return expenseDTO;
        }).collect(Collectors.toList());
    }

    public List<SplitExpenseDTO> getExpenseSplits(Long expenseId) {
        List<SplitExpense> splitExpenses = splitExpenseRepo.findByExpenseId(expenseId);
        List<SplitExpenseDTO> splitExpenseDTOS = splitExpenses.stream().map(se -> {
            SplitExpenseDTO splitExpenseDTO = new SplitExpenseDTO();
            splitExpenseDTO.setSplitExpenseId(se.getSplitExpenseId());
            splitExpenseDTO.setAmountOwed(se.getAmount());
            splitExpenseDTO.setBorrowerName(se.getUserId().getName());
            return splitExpenseDTO;
        }).collect(Collectors.toList());
        return splitExpenseDTOS;
    }

    public BalanceResponse getUserBalance(Long userId, Long groupId) {
        List<Expense> expenses = expenseRepo.findExpensesByGroupId(groupId);

        Map<Expense,List<SplitExpense>> expenseSplitExpenseMap;
        List<Long> expenseIds = new ArrayList<>();
        for(Expense expense: expenses){
            expenseIds.add(expense.getExpenseId());
        }
        List<SplitExpense> splits = splitExpenseRepo.findAllSplitsByExpenseIds(expenseIds);
        Map<Long, Expense> expenseLookup =
                expenses.stream().collect(Collectors.toMap(Expense::getExpenseId, e -> e));

        expenseSplitExpenseMap = splits.stream()
                .collect(Collectors.groupingBy(
                        split -> expenseLookup.get(split.getExpenseId().getExpenseId())
                ));
        Map<String, BigDecimal> balanceMap = new HashMap<>();
        for (Map.Entry<Expense,List<SplitExpense>> entry: expenseSplitExpenseMap.entrySet()){
            Expense currentExpense = entry.getKey();
            // user paid
            if(Objects.equals(currentExpense.getPaidBy().getUserId(), userId)){
                List<SplitExpense> splitExpenses = entry.getValue();
                for(SplitExpense splitExpense: splitExpenses){
                    if(!Objects.equals(splitExpense.getUserId().getUserId(),userId)){
                        balanceMap.put(splitExpense.getUserId().getName(),balanceMap.getOrDefault(splitExpense.getUserId().getName(),BigDecimal.ZERO).add(splitExpense.getAmount()));
                    }
                }
            }
            // paid by others
            else {
                List<SplitExpense> splitExpenses = entry.getValue();
                for(SplitExpense splitExpense: splitExpenses){
                    if(Objects.equals(splitExpense.getUserId().getUserId(),userId)){
                        balanceMap.put(currentExpense.getPaidBy().getName(),balanceMap.getOrDefault(currentExpense.getPaidBy().getName(),BigDecimal.ZERO).subtract(splitExpense.getAmount()));
                    }
                }
            }
        }
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setYouAreOwed(new ArrayList<>());
        balanceResponse.setYouOwe(new ArrayList<>());
        for(Map.Entry<String,BigDecimal> entry: balanceMap.entrySet()){
            String userName = entry.getKey();
            BigDecimal amount = entry.getValue();
            BalanceDTO balanceDTO = new BalanceDTO();
            if(amount.compareTo(BigDecimal.ZERO) < 0){
                balanceDTO.setUserName(userName);
                balanceDTO.setAmount(amount.abs());
                balanceResponse.getYouOwe().add(balanceDTO);
            }else {
                balanceDTO.setUserName(userName);
                balanceDTO.setAmount(amount);
                balanceResponse.getYouAreOwed().add(balanceDTO);
            }
        }

        return balanceResponse;

    }
}
