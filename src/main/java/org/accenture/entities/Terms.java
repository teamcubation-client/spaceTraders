package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Terms {
    private ZonedDateTime deadline;
    private Payment payment;
    private Deliver[] deliver;
}
