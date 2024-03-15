package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Contract {
    private String id;
    private String factionSymbol;
    private Type type;
    private Terms terms;
    private boolean accepted;
    private boolean fulfilled;
    private ZonedDateTime expiration;
    private ZonedDateTime deadlineToAccept;

    private enum Type {
        PROCUREMENT, TRANSPORT, SHUTTLE
    }
}
