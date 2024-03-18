package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Deliver {
    private String tradeSymbol;
    private String destinationSymbol;
    private int unitsRequired;
    private int unitsFulfilled;
}
