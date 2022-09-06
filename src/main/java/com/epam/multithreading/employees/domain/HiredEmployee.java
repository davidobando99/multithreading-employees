package com.epam.multithreading.employees.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HiredEmployee {

    private String id;
    private String name;
    private String title;

    @JsonCreator
    public HiredEmployee(@JsonProperty("id") String id,@JsonProperty("name") String name,@JsonProperty("title") String title) {
        this.name = name;
        this.title = title;
        this.id = id;
    }

    public HiredEmployee(){

    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "HiredEmployee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
