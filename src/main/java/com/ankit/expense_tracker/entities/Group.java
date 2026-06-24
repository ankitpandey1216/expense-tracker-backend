package com.ankit.expense_tracker.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    @Column(nullable = false)
    private String groupTitle;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
