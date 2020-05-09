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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time", nullable = false)
    private Date dateTime;
    @Column(name = "information", nullable = false)
    private String information;
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

    public Integer getId() {
        return id;
    }

    public Log() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
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
