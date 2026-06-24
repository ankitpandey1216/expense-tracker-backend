package com.ankit.expense_tracker.repo;

import com.ankit.expense_tracker.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepo extends JpaRepository<Group,Long> {
}
