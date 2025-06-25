package org.portfolio.com.backend.repositories;

import org.portfolio.com.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u where upper(trim(u.name)) like %?1%")
    List<User> searchByName(String name);
}
