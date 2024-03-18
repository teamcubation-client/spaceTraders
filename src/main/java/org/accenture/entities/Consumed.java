package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Consumed {
    private int amount;
    private ZonedDateTime timestamp;
}
