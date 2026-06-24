package com.ankit.expense_tracker.DTO.DashboardResponse;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RecentExpenseDTO {
    private Long paidByUserId;
    private String paidByName;
    private BigDecimal amountPaid;
    private LocalDateTime createdAt;
    private String groupName;
    private String description;

}
