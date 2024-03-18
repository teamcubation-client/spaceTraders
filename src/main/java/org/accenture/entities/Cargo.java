package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Cargo {
    private int capacity;
    private int units;
    private Inventory[] inventory;
}
