package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Ship {
    private String symbol;
    private Nav nav;
    private Cooldown cooldown;
    private Cargo cargo;
    private Fuel fuel;
}
