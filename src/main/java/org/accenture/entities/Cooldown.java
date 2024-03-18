package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Cooldown {
    private String shipSymbol;
    private int totalSeconds;
    private int remainingSeconds;
    private ZonedDateTime expiration;
}
