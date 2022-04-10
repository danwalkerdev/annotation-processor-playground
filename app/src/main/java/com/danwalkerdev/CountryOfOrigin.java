package com.danwalkerdev;

import com.danwalkerdev.processor.Named;

@Named
public enum CountryOfOrigin implements HasName {
    ENGLAND("england"),
    SCOTLAND("scotland"),
    WALES("wales"),
    NORTHERNIRELAND("northernireland");

    private final String name;

    CountryOfOrigin(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
