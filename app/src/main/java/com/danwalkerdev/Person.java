package com.danwalkerdev;

import com.danwalkerdev.processor.Named;

@Named
public enum Person implements HasName {
    BOB("Bob"),
    JIM("Jim");

    private final String name;

    Person(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
