package com.noodle.noodle.Entities;

import org.springframework.context.annotation.Bean;

public class Identification {
    private int id;

    public Identification(int id) {
        this.id = id;
    }

    public Identification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
