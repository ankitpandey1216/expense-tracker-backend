package com.ankit.expense_tracker.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GroupDetailDTO {
    private Long groupId;
    private String groupName;
    private List<UserDTO> members;
    private List<ExpenseDTO> expenses;
    private BalanceResponse balances;
}
