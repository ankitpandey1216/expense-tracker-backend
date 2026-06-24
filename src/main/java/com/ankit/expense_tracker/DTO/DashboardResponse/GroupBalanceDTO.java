package com.ankit.expense_tracker.DTO.DashboardResponse;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupBalanceDTO {
    private String groupName;
    private Long groupId;
    private BigDecimal owedToYou;
    private BigDecimal youOwe;
}
