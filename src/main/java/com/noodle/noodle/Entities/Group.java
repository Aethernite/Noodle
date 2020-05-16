package com.noodle.noodle.Entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Groups_T")
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_number", nullable = false)
    private int num;

    @OneToMany(mappedBy = "group")
    private Set<Student> students;

    public Group(Integer id, int num, Set<Student> students) {
        this.id = id;
        this.num = num;
        this.students = students;
    }

    public Group() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
