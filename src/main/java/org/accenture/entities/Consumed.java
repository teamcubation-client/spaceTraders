package org.accenture.entities;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Consumed {
    private int amount;
    private ZonedDateTime timestamp;
}
