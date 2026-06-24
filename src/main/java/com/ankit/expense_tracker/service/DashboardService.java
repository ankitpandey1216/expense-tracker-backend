package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.DTO.DashboardResponse.DashboardResponse;
import com.ankit.expense_tracker.DTO.DashboardResponse.GroupBalanceDTO;
import com.ankit.expense_tracker.DTO.DashboardResponse.RecentExpenseDTO;
import com.ankit.expense_tracker.entities.Expense;
import com.ankit.expense_tracker.entities.Group;
import com.ankit.expense_tracker.entities.SplitExpense;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;

    private final GroupMemberRepo groupMemberRepo;

    private final SplitExpenseRepo splitExpenseRepo;

    @Autowired
    public DashboardService(ExpenseRepo expenseRepo,UserRepo userRepo,GroupMemberRepo groupMemberRepo,SplitExpenseRepo splitExpenseRepo){
        this.expenseRepo = expenseRepo;
        this.userRepo = userRepo;
        this.groupMemberRepo = groupMemberRepo;
        this.splitExpenseRepo = splitExpenseRepo;
    }

    @Transactional
    public DashboardResponse getDashboardData(Long userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
        List<Group> groups = groupMemberRepo.findAllGroup(userId);
        if(groups.isEmpty()){
            return new DashboardResponse();
        }
        List<Long> groupIds = new ArrayList<>();
        for(Group group: groups){
            groupIds.add(group.getGroupId());
        }
        List<Expense> expenses = expenseRepo.findAllExpensesByGroups(groupIds);
        Pageable pageable = PageRequest.of(0,10);
        List<Expense> recent = expenseRepo.findRecentExpense(groupIds,pageable);
        List<RecentExpenseDTO> recentExpenseDTOS = new ArrayList<>();
        for(Expense expense: recent){
            RecentExpenseDTO recentExpenseDTO = new RecentExpenseDTO();
            recentExpenseDTO.setGroupName(expense.getGroup().getGroupTitle());
            recentExpenseDTO.setCreatedAt(expense.getCreatedAt());
            recentExpenseDTO.setAmountPaid(expense.getExpenseAmount());
            recentExpenseDTO.setPaidByName(expense.getPaidBy().getName());
            recentExpenseDTO.setPaidByUserId(expense.getPaidBy().getUserId());
            recentExpenseDTO.setDescription(expense.getDescription());
            recentExpenseDTOS.add(recentExpenseDTO);
        }
        List<Long> expenseIds = new ArrayList<>();
        for(Expense expense: expenses){
            expenseIds.add(expense.getExpenseId());
        }
        List<SplitExpense> splitExpenses = splitExpenseRepo.findAllSplitsByExpenseIds(expenseIds);
        DashboardResponse dashboardResponse = getGroupBalance(groups,expenses,splitExpenses,userId);
        dashboardResponse.setRecentExpenses(recentExpenseDTOS);

        return dashboardResponse;
    }

    private DashboardResponse getGroupBalance(List<Group> groups, List<Expense> expenses,List<SplitExpense> splitExpenses,Long userId){
        Map<Group,List<Expense>> expenseMap = new HashMap<>();
        Map<Expense,List<SplitExpense>> expenseSplitMap = new HashMap<>();
        for(Expense expense: expenses){
            if(expenseMap.containsKey(expense.getGroup())){
                expenseMap.get(expense.getGroup()).add(expense);
            }else {
                expenseMap.put(expense.getGroup(),new ArrayList<>());
                expenseMap.get(expense.getGroup()).add(expense);
            }
        }
        for(SplitExpense splitExpense: splitExpenses){
            if(expenseSplitMap.containsKey(splitExpense.getExpenseId())){
                expenseSplitMap.get(splitExpense.getExpenseId()).add(splitExpense);
            }else{
                expenseSplitMap.put(splitExpense.getExpenseId(),new ArrayList<>());
                expenseSplitMap.get(splitExpense.getExpenseId()).add(splitExpense);
            }
        }

        List<GroupBalanceDTO> groupBalanceDTOS = new ArrayList<>();
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal owedToUser = BigDecimal.ZERO;
        BigDecimal userOwe = BigDecimal.ZERO;
        for(Group group: groups){
            BigDecimal amountOwed = BigDecimal.ZERO, amountYouOwe = BigDecimal.ZERO;
            GroupBalanceDTO groupBalanceDTO = new GroupBalanceDTO();
            groupBalanceDTO.setGroupId(group.getGroupId());
            groupBalanceDTO.setGroupName(group.getGroupTitle());
            List<Expense> groupExpenses = null;
            if(expenseMap.containsKey(group)){
                groupExpenses = expenseMap.get(group);
            }else{
                groupBalanceDTO.setYouOwe(amountYouOwe);
                groupBalanceDTO.setOwedToYou(amountOwed);
                groupBalanceDTOS.add(groupBalanceDTO);
                continue;
            }
            for(Expense expense: groupExpenses){
                List<SplitExpense> expenseSplits = expenseSplitMap.get(expense);
                Long paidBy = expense.getPaidBy().getUserId();
                if(Objects.equals(paidBy,userId)){
                    totalExpense = totalExpense.add(expense.getExpenseAmount());
                }
                for(SplitExpense splitExpense: expenseSplits){
                    if(!Objects.equals(paidBy,userId) && Objects.equals(splitExpense.getUserId().getUserId(), userId)){
                        amountYouOwe = amountYouOwe.add(splitExpense.getAmount());
                        userOwe = userOwe.add(splitExpense.getAmount());
                    }
                    else if(Objects.equals(paidBy, userId) && !Objects.equals(splitExpense.getUserId().getUserId(),userId)){
                        amountOwed = amountOwed.add(splitExpense.getAmount());
                        owedToUser = owedToUser.add(splitExpense.getAmount());
                    }
                }
            }
            groupBalanceDTO.setYouOwe(amountYouOwe);
            groupBalanceDTO.setOwedToYou(amountOwed);
            groupBalanceDTOS.add(groupBalanceDTO);
        }

        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setGroupBalances(groupBalanceDTOS);
        dashboardResponse.setTotalGroups(groups.size());
        dashboardResponse.setOwedAmount(owedToUser);
        dashboardResponse.setTotalYouOwe(userOwe);
        dashboardResponse.setTotalExpense(totalExpense);

        return  dashboardResponse;


    }

}
