package com.danwalkerdev;

import org.junit.jupiter.api.Test;

import static com.danwalkerdev.test.NamedFactory.*;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProcessingTest {

    @Test
    void testGeneratedClassWorks() {
        Person person = person("Jim");
        Incident incident = incident("accident");
        CountryOfOrigin countryOfOrigin = countryOfOrigin("england");

        assertSame(Person.JIM, person);
        assertSame(Incident.ACCIDENT, incident);
        assertSame(CountryOfOrigin.ENGLAND, countryOfOrigin);
    }
}
