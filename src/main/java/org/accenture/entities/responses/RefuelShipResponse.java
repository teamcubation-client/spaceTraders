package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Agent;
import org.accenture.entities.Fuel;
import org.accenture.entities.Transaction;

@Getter
public class RefuelShipResponse {
    private Agent agent;
    private Fuel fuel;
    private Transaction transaction;
}
