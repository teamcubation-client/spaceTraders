package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Transaction {
    private String waypointSymbol;
    private String shipSymbol;
    private String tradeSymbol;
    private Type type;
    private int units;
    private int pricePerUnit;
    private int totalPrice;
    private ZonedDateTime timestamp;

    private enum Type {
        PURCHASE, SELL
    }
}
