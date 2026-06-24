package com.ankit.expense_tracker.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private String userName;
    private BigDecimal amount;
}
