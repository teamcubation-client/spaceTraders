package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Fuel;
import org.accenture.entities.Nav;

@Getter
public class NavigateShipResponse {
    private Fuel fuel;
    private Nav nav;
}
