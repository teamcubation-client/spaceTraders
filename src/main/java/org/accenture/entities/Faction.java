package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Faction {
    private String symbol;
    private String name;
    private String description;
    private String headquarters;
    private Trait[] traits;
    private boolean isRecruiting;

    private enum Symbol {
        COSMIC, VOID, GALACTIC, QUANTUM, DOMINION, ASTRO, CORSAIRS, OBSIDIAN, AEGIS, UNITED, SOLITARY, COBALT, OMEGA,
        ECHO, LORDS, CULT, ANCIENTS, SHADOW, ETHEREAL
    }
}
