package com.ankit.expense_tracker.DTO;

import com.ankit.expense_tracker.entities.User;
import lombok.Data;

@Data
public class GroupCreationRequest {
    private String groupTitle;
    private Long createdBy;
}
