package org.accenture.entities;

import lombok.Getter;

@Getter
public class Agent {
    private String accountId;
    private String symbol;
    private String headquarters;
    private int credits;
    private String startingFaction;
    private int shipCount;
}
