package com.noodle.noodle.Entities;



import com.noodle.noodle.Models.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Students")
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "facnum", nullable = false)
    private String facnum;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "courses_students", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "student")
    private Set<Log> logs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group group;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester", nullable = false)
    private Semester semester;

    public Set<Log> getLogs() {
        return logs;
    }

    public void setLogs(Set<Log> logs) {
        this.logs = logs;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public enum Semester{
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH,
        SIXTH,
        SEVENTH,
        EIGHTH
    }

    public Student(Integer id, String name, String facnum, Group group) {
        this.id = id;
        this.name = name;
        setFacnum(facnum);
        this.group = group;
    }

    public Student() {
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacnum() {
        return facnum;
    }

    public void setFacnum(String facnum) {
        if(facnum.length() == 9){
            this.facnum=facnum;
        }
        else{
            throw new IllegalArgumentException("Invalid faculty number size");
        }
    }
    public boolean isSelectedSemester(Semester semester){
        return this.semester.equals(semester);
    }
    public Group getGroup() {
        return group;
    }
}
