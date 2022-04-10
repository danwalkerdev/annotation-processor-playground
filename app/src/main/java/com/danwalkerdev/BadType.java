package com.danwalkerdev;

import com.danwalkerdev.processor.Named;

@Named
public class BadType implements HasName {

    @Override
    public String getName() {
        return "Foo";
    }
}
