package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Fuel {
    private int current;
    private int capacity;
    private Consumed consumed;
}
