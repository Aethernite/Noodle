package com.noodle.noodle.Repositories;

import com.noodle.noodle.Entities.Group;
import com.noodle.noodle.Entities.Log;
import com.noodle.noodle.Entities.Student;
import com.noodle.noodle.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findAll();
    Group findOneByNum(Integer num);
}