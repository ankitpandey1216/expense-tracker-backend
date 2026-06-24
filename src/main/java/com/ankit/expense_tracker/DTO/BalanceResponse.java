package com.ankit.expense_tracker.DTO;

import lombok.Data;

import java.util.List;

@Data
public class BalanceResponse {
    private List<BalanceDTO> youOwe;
    private List<BalanceDTO> youAreOwed;
}
