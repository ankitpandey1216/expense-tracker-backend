package com.ankit.expense_tracker.repo;

import com.ankit.expense_tracker.entities.Group;
import com.ankit.expense_tracker.entities.GroupMember;
import com.ankit.expense_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMemberRepo extends JpaRepository<GroupMember,Long> {
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.user.userId = :id")
    List<Group> findAllGroup(Long id);
    @Query("SELECT gm.user.userId FROM GroupMember gm WHERE gm.group.groupId = :id")
    List<Long> findAllMemberIds(Long id);

    @Query("SELECT gm.user FROM GroupMember gm WHERE gm.group.groupId = :groupId")
    List<User> findMembersByGroupId(@Param("groupId") Long groupId);
}
