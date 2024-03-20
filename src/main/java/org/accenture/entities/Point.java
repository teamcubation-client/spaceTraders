package org.accenture.entities;

import lombok.Getter;

@Getter
public class Point {
    private String symbol;
    private Type type;
    private String systemSymbol;
    private int x;
    private int y;
    public String getType(){
        return this.type.name();
    }

    private enum Type {
        PLANET, GAS_GIANT, MOON, ORBITAL_STATION, JUMP_GATE, ASTEROID_FIELD, ASTEROID, ENGINEERED_ASTEROID,
        ASTEROID_BASE, NEBULA, DEBRIS_FIELD, GRAVITY_WELL, ARTIFICIAL_GRAVITY_WELL, FUEL_STATION
    }
}


