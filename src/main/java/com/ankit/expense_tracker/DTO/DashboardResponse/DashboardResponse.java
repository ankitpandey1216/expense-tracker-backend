package com.ankit.expense_tracker.DTO.DashboardResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardResponse {
    private Integer totalGroups;
    private BigDecimal totalExpense;
    private BigDecimal owedAmount;
    private BigDecimal totalYouOwe;
    private List<RecentExpenseDTO> recentExpenses;
    private List<GroupBalanceDTO> groupBalances;


}
