package com.ankit.expense_tracker.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AddMemberRequest {
    private List<Long> userIds;
}
