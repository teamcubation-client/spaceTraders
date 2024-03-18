package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Nav {
    private String systemSymbol;
    private String waypointSymbol;
    private Route route;
    private Status status;
    private FlightMode flightMode;

    private enum Status {
        IN_TRANSIT, IN_ORBIT, DOCKED
    }

    private enum FlightMode {
        DRIFT, STEALTH, CRUISE, BURN
    }
}
