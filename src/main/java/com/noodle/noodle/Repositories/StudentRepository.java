package com.noodle.noodle.Repositories;

import com.noodle.noodle.Entities.Course;
import com.noodle.noodle.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findOneById(Integer id);
    Student findOneByName(String name);
}
