package org.accenture.entities;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Cooldown {
    private String shipSymbol;
    private int totalSeconds;
    private int remainingSeconds;
    private ZonedDateTime expiration;
}
