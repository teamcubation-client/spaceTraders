package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@ToString
public class Route {
    private Point destination;
    private Point origin;
    private ZonedDateTime departureTime;
    private ZonedDateTime arrival;
}
