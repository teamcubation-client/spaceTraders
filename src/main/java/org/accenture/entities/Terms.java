package org.accenture.entities;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Terms {
    private ZonedDateTime deadline;
    private Payment payment;
    private Deliver[] deliver;
}
