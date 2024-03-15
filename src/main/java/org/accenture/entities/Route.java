package org.accenture.entities;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Route {
    private Point destination;
    private Point origin;
    private ZonedDateTime departureTime;
    private ZonedDateTime arrival;
}
