package com.danwalkerdev;

import com.danwalkerdev.processor.Named;

@Named
public enum Incident implements HasName {
    ACCIDENT("accident"),
    THEFT("theft"),
    FIRE("fire");

    private final String name;

    Incident(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
