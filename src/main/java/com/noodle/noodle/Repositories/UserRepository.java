package com.noodle.noodle.Repositories;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByStatus(String status);
    User findOneById(Integer id);
    User findOneByUsername(String username);
}