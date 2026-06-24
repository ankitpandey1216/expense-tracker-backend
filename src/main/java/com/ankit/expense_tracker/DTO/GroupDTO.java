package com.ankit.expense_tracker.DTO;

import com.ankit.expense_tracker.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long groupId;
    private String groupTitle;
    private Long creatorId;
    private String creatorName;
    private LocalDateTime createdAt;

}
