package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Point;
import org.accenture.entities.Trait;

@Getter
public class ListWaypointsResponse extends Point {
    private String orbits;
    private Trait[] traits;
}
