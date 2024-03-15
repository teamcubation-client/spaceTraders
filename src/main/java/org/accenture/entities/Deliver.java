package org.accenture.entities;

import lombok.Getter;

@Getter
public class Deliver {
    private String tradeSymbol;
    private String destinationSymbol;
    private int unitsRequired;
    private int unitsFulfilled;
}
