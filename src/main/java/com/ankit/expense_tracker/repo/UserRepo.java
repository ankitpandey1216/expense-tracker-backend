package com.ankit.expense_tracker.repo;

import com.ankit.expense_tracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findUserByEmail(String email);

    @Query("""
    SELECT u
    FROM User u
    WHERE LOWER(u.name) LIKE LOWER(CONCAT(:query, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT(:query, '%'))
    ORDER BY u.name
    """)
    List<User> searchUser(String query);
}
