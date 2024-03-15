package org.accenture.entities;

import lombok.Getter;

@Getter
public class Cargo {
    private int capacity;
    private int units;
    private Inventory[] inventory;
}
