package com.cango.myapplication.bean;

/**
 * Created by cango on 2017/3/13.
 */

public class Fruit {
    private int id;

    public Fruit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
