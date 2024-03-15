package org.accenture.entities;

import lombok.Getter;

@Getter
public class Fuel {
    private int current;
    private int capacity;
    private Consumed consumed;
}
