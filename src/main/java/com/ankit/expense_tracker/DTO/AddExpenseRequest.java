package com.ankit.expense_tracker.DTO;

import com.ankit.expense_tracker.entities.SplitType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class AddExpenseRequest {
    private BigDecimal amount;
    private Long paidBy;
    private String description;
    private SplitType splitType;
    private List<Long> userIds;
    private Map<Long,BigDecimal> customSplits;
}
