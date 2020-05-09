package com.noodle.noodle.Entities;



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
    @Column(name = "groupnum", nullable = false)
    private int group;
    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();
    @OneToMany(mappedBy = "student")
    private Set<Log> logs;

    public Student(Integer id, String name, String facnum, int group) {
        this.id = id;
        this.name = name;
        setFacnum(facnum);
        this.group = group;
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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
