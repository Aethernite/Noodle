package com.noodle.noodle.Entities;

import com.noodle.noodle.Models.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Logs")
public class Log {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "date_string")
    private String date;
    @Column(name = "time_string")
    private String time;
    @Column(name = "event_context", nullable = false)
    private String context;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "activity", nullable = false)
    private String activity;
    @ManyToOne
    @JoinColumn(name="student_id", nullable=true)
    private Student student;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=true)
    private User user;
    @ManyToOne
    @JoinColumn(name="course_id", nullable=true)
    private Course course;
    @Column(name="plain_text",nullable=false)
    private String plaintext;

    public Integer getId() {
        return id;
    }

    public Log() {
    }

    public Log(Integer id, String date, String time, String context, String description, String activity, Student student, User user, Course course, String plaintext) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.context = context;
        this.description = description;
        this.activity = activity;
        this.student = student;
        this.user = user;
        this.course = course;
        this.plaintext = plaintext;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
